package com.fasfood.common.mapper;

public interface QueryMapper <Q, R>{
    Q queryFromRequest(R request);
}
