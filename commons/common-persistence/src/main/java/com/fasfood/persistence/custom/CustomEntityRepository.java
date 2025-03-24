package com.fasfood.persistence.custom;

import java.util.List;

public interface CustomEntityRepository<E, Q, S> {

    List<E> search(Q query);

    Long count(Q query);

    List<S> statistics(Q query);

    List<E> autocomplete(Q query);

}

