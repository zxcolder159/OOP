package ru.nsu.ermakov.distributed.protocol;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class ResultPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int taskId;
    private final boolean hasComposite; // true, если нашли хотя бы одно не-простое число

    public ResultPacket(int taskId, boolean hasComposite) {
        this.taskId = taskId;
        this.hasComposite = hasComposite;
    }

}