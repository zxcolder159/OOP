package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Контрольная точка в расписании курса с именем и датой.
 */
@AllArgsConstructor
@Getter
public class Checkpoint {
	private final String name;
	private final LocalDateTime date;
}
