package ru.nsu.ermakov.distributed.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class ResultPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int taskId;
    private final boolean allPrime;

}