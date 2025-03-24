package com.fasfood.web.support;

import com.fasfood.common.mapper.EntityMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractDomainRepository<D, E, I> implements DomainRepository<D, I> {
    protected final JpaRepository<E, I> jpaRepository;
    protected final EntityMapper<D, E> mapper;

    protected AbstractDomainRepository(JpaRepository<E, I> jpaRepository, EntityMapper<D, E> mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    public Optional<D> findById(I id) {
        Optional<E> var10000 = this.jpaRepository.findById(id);
        EntityMapper<D, E> var10001 = this.mapper;
        Objects.requireNonNull(var10001);
        return var10000.map(var10001::toDomain).map(this::enrich);
    }

    public List<D> findAllByIds(List<I> ids) {
        Stream<E> var10001 = this.jpaRepository.findAllById(ids).stream();
        EntityMapper<D, E> var10002 = this.mapper;
        Objects.requireNonNull(var10002);
        return this.enrichList(var10001.map(var10002::toDomain).collect(Collectors.toList()));
    }

    @Transactional
    public D save(D domain) {
        this.saveAll(List.of(domain));
        return domain;
    }

    @Transactional
    public List<D> saveAll(List<D> domains) {
        List<E> entities = this.mapper.toEntity(domains);
        this.jpaRepository.saveAll(entities);
        return domains;
    }

    protected D enrich(D d) {
        List<D> ds = List.of(d);
        return (D)this.enrichList(ds).getFirst();
    }

    protected List<D> enrichList(List<D> ds) {
        return ds;
    }
}
