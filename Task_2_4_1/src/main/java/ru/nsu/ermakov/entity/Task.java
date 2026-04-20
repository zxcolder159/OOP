package ru.nsu.ermakov.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Task {
	private final String name;
	private final LocalDateTime softDeadLine;
	private final LocalDateTime hardDeadline;
	private final byte maxScore;
}
