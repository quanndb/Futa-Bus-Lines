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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Trip extends Domain {
    private String code;
    private List<TripTransit> tripTransits;
    private List<TripDetails> tripDetails;

    // base
    public Trip(TripCreateOrUpdateCmd cmd) {
        super();
        this.code = cmd.getCode();
        this.tripTransits = this.createTripTransit(cmd.getTransits());
    }

    public Trip update(TripCreateOrUpdateCmd cmd) {
        this.code = cmd.getCode();
        this.tripTransits.forEach(Domain::delete);
        this.tripTransits.addAll(this.createTripTransit(cmd.getTransits()));
        return this;
    }

    private List<TripTransit> createTripTransit(List<TripTransitCreateOrUpdateCmd> cmds) {
        if (CollectionUtils.isEmpty(cmds)) return new ArrayList<>();
        List<TripTransit> tripTransits = new ArrayList<>();
        for (int i = 0; i < cmds.size(); i++) {
            tripTransits.add(new TripTransit(this.id, i, cmds.get(i)));
        }
        return tripTransits;
    }

    // assign
    public TripDetails createTripDetail(TripDetailsCreateOrUpdateCmd cmd) {
        if (CollectionUtils.isEmpty(this.tripDetails)) {
            TripDetails newOne = new TripDetails(this.id, cmd);
            this.tripDetails.add(newOne);
            return newOne;
        }
        this.checkDate(cmd.getFromAt(), cmd.getToAt());
        for (TripDetails details : this.tripDetails) {
            if (TripStatus.INACTIVE.equals(details.getStatus()) || TripStatus.INACTIVE.equals(cmd.getStatus())) {
                continue;
            }
            if (this.isBetween(details.getFromAt(), details.getToAt(), cmd.getFromAt())) {
                throw new ResponseException(BadRequestError.SCHEDULE_ALREADY_EXISTED, details.getFromAt() + "-" + details.getToAt());
            }
        }
        TripDetails newOne = new TripDetails(this.id, cmd);
        this.tripDetails.add(newOne);
        return newOne;
    }

    public TripDetails updateTripDetail(UUID id, TripDetailsCreateOrUpdateCmd cmd) {
        if (CollectionUtils.isEmpty(this.tripDetails)) {
            throw new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND, id);
        }
        this.checkDate(cmd.getFromAt(), cmd.getToAt());
        TripDetails found = null;
        for (TripDetails details : this.tripDetails) {
            if (details.getId().equals(id)) {
                found = details;
                continue;
            }
            if (TripStatus.INACTIVE.equals(details.getStatus()) || TripStatus.INACTIVE.equals(cmd.getStatus())) {
                continue;
            }
            if (this.isBetween(details.getFromAt(), details.getToAt(), cmd.getFromAt())) {
                throw new ResponseException(BadRequestError.SCHEDULE_ALREADY_EXISTED, details.getFromAt() + "-" + details.getToAt());
            }
        }
        if (Objects.isNull(found)) {
            throw new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND, id);
        }
        found = found.update(cmd);
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

    private boolean isBetween(Instant from, Instant to, Instant target) {
        return (target.equals(from) || target.isAfter(from)) &&
                (target.equals(to) || target.isBefore(to));
    }

    private void checkDate(Instant from, Instant to) {
        if (from.isAfter(to)) {
            throw new ResponseException(BadRequestError.FROM_MUST_BEFORE_TO, from + " - " + to);
        }
        if (Instant.now().isAfter(from)) {
            throw new ResponseException(BadRequestError.INVALID_DATE, from);
        }
    }
}
