package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Группа студентов с именем и списком студентов.
 */
@Getter
@AllArgsConstructor
public class Group {
	private final String name;
	private final List<Student> students;
}
