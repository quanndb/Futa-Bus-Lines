package com.fasfood.common.mapper;

import java.util.List;

public interface CmdMapper<C, R> {
    R requestFromCmd(C cmd);

    List<R> requestListFromCmdList(List<C> cmdList);

    C cmdFromRequest(R request);

    List<C> cmdListFromRequestList(List<R> requestList);
}
