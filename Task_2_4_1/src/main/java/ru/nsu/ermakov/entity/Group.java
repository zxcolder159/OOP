package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
public class Group {
	private final String name;
	private final List<Student> students;
}
