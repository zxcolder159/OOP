package ru.nsu.ermakov.entity;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Описание задачи курса: идентификатор, имя, дедлайны и максимальный балл.
 */
@Getter
public class Task {
	private final String id;
	private final String name;
	private final LocalDateTime softDeadline;
	private final LocalDateTime hardDeadline;
	private final byte maxScore;

	/**
	 * Конструктор с явным id и именем.
	 */
	public Task(String id, String name, LocalDateTime softDeadline, LocalDateTime hardDeadline, byte maxScore) {
		String normalizedId = id == null || id.isBlank() ? null : id.trim();
		String normalizedName = name == null || name.isBlank() ? null : name.trim();

		if (normalizedId == null && normalizedName == null) {
			normalizedId = "unknown-task";
			normalizedName = "unknown-task";
		} else if (normalizedId == null) {
			normalizedId = normalizedName;
		} else if (normalizedName == null) {
			normalizedName = normalizedId;
		}

		this.id = normalizedId;
		this.name = normalizedName;
		this.softDeadline = softDeadline;
		this.hardDeadline = hardDeadline;
		this.maxScore = maxScore;
	}

	/**
	 * Удобный конструктор, когда id совпадает с именем.
	 */
	public Task(String name, LocalDateTime softDeadline, LocalDateTime hardDeadline, byte maxScore) {
		this(name, name, softDeadline, hardDeadline, maxScore);
	}
}
