package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Ассоциация студента и задачи с текущим статусом и баллом.
 */
@AllArgsConstructor
@Getter
@Setter
public class Assignment {
	private Status status;
	private int score;
	private Student student;
	private Task task;
}
