package com.fasfood.common.mapper;

import java.util.List;

public interface DTOMapper<O, D, E> {
    O domainToDTO(D domain);

    List<O> domainToDTO(List<D> domains);

    O entityToDTO(E entity);

    List<O> entityToDTO(List<E> entities);

    D dtoToDomain(O dto);

    List<D> dtoToDomain(List<O> dtos);

    E dtoToEntity(O dto);

    List<E> dtoToEntity(List<O> dtos);
}