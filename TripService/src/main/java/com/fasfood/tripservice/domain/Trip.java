package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.tripservice.domain.cmd.TripCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripTransitCreateOrUpdateCmd;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.exception.NotFoundError;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Trip extends Domain {
    private String code;
    private String name;
    private String description;
    private List<TripTransit> tripTransits;
    private List<TripDetails> tripDetails;

    // base
    public Trip(TripCreateOrUpdateCmd cmd) {
        super();
        this.code = cmd.getCode();
        this.name = cmd.getName();
        this.description = cmd.getDescription();
    }

    public Trip update(TripCreateOrUpdateCmd cmd) {
        this.code = cmd.getCode();
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        return this;
    }

    public Trip setTripTransit(List<TripTransitCreateOrUpdateCmd> cmds) {
        if (CollectionUtils.isEmpty(this.tripTransits)) this.tripTransits = new ArrayList<>();
        else this.tripTransits.forEach(Domain::delete);
        if (CollectionUtils.isEmpty(cmds)) return this;
        for (int i = 0; i < cmds.size(); i++) {
            this.tripTransits.add(new TripTransit(this.id, i, cmds.get(i)));
        }
        return this;
    }

    // assign
    public TripDetails createTripDetail(TripDetailsCreateOrUpdateCmd cmd) {
        if (CollectionUtils.isEmpty(this.tripDetails)) {
            TripDetails newOne = new TripDetails(this.id, cmd);
            this.tripDetails.add(newOne);
            return newOne;
        }
        TripDetails newOne = new TripDetails(this.id, cmd);
        this.checkAfterFromNow(newOne.getFromDate());
        this.checkTheSchedule(newOne);
        this.tripDetails.add(newOne);
        return newOne;
    }

    public TripDetails updateTripDetail(UUID id, TripDetailsCreateOrUpdateCmd cmd) {
        if (CollectionUtils.isEmpty(this.tripDetails)) {
            throw new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND, id);
        }
        TripDetails found = this.tripDetails.stream()
                .filter(details -> details.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND, id));
        found = found.update(cmd);
        this.checkTheSchedule(found);
        return found;
    }

    public void deleteTripDetail(UUID id) {
        if (CollectionUtils.isEmpty(this.tripDetails)) {
            throw new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND, id);
        }
        this.tripDetails.forEach(details -> {
            if (details.getId().equals(id)) {
                details.delete();
            }
        });
    }

    // utils
    public void enrichTripTransits(List<TripTransit> transits) {
        this.tripTransits = new ArrayList<>();
        if (CollectionUtils.isEmpty(transits)) return;
        this.tripTransits.addAll(transits);
    }

    public void enrichTripDetails(List<TripDetails> details) {
        this.tripDetails = new ArrayList<>();
        if (CollectionUtils.isEmpty(details)) return;
        this.tripDetails.addAll(details);
    }

    private void checkTheSchedule(TripDetails needToCheck) {
        if (needToCheck.getFromDate().isAfter(needToCheck.getToDate())) {
            throw new ResponseException(BadRequestError.FROM_MUST_BEFORE_TO, needToCheck.getFromDate() + " Đến " + needToCheck.getToDate());
        }
        if (!CollectionUtils.isEmpty(this.tripDetails) && TripStatus.ACTIVE.equals(needToCheck.getStatus())) {
            for (TripDetails details : this.tripDetails) {
                if (TripStatus.ACTIVE.equals(details.getStatus()) && !details.getId().equals(needToCheck.getId())) {
                    this.checkBetween(details.getFromDate(), details.getToDate(), needToCheck.getFromDate(), needToCheck.getToDate());
                }
            }
        }
    }

    private void checkBetween(LocalDate from, LocalDate to, LocalDate fromCheck, LocalDate toCheck) {
        boolean isOverlapping = !toCheck.isBefore(from) && !fromCheck.isAfter(to);
        if (isOverlapping) {
            throw new ResponseException(BadRequestError.SCHEDULE_ALREADY_EXISTED, from + " Đến " + to);
        }
    }

    private void checkAfterFromNow(LocalDate from) {
        if (LocalDate.now().isAfter(from)) {
            throw new ResponseException(BadRequestError.INVALID_DATE, from);
        }
    }
}
