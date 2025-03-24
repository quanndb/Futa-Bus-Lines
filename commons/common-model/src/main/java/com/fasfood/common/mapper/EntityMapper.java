package com.fasfood.common.mapper;

import java.util.List;

public interface EntityMapper<D, E> {
    D toDomain(E entity);

    List<D> toDomain(List<E> entities);

    E toEntity(D domain);

    List<E> toEntity(List<D> domains);
}
