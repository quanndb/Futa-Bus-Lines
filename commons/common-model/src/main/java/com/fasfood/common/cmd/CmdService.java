package com.fasfood.common.cmd;

public interface CmdService<O, R, I> {
    O create(R request);

    O update(I id, R request);

    void delete(I id);
}
