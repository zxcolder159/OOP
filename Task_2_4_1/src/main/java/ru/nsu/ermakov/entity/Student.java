package ru.nsu.ermakov.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Student {

	private final String fio;
	private final String githubNick;
	private final String repoUrl;

}
