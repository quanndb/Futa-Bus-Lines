package com.fasfood.common.query;

import com.fasfood.common.dto.PageDTO;

public interface QueryService<O, I, R> {
    O getById(I id);

    PageDTO<O> getList(R pagingRequest);
}
