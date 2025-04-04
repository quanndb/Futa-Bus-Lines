package com.fasfood.common.mapper;

import java.util.List;

public interface DTOEntityMapper<O, E> {
    O entityToDTO(E entity);

    List<O> entityToDTO(List<E> entities);

    E dtoToEntity(O dto);

    List<E> dtoToEntity(List<O> dtos);
}
