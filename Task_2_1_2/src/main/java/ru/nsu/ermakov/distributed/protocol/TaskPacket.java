package ru.nsu.ermakov.distributed.protocol;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class TaskPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int taskId;
    private final long[] numbers;


    public TaskPacket(int taskId, long[] numbers) {
        this.taskId = taskId;
        this.numbers = numbers;
    }

}