package ru.nsu.ermakov.dsl;

import groovy.lang.Closure;
import lombok.Getter;
import ru.nsu.ermakov.entity.Student;

import java.util.ArrayList;
import java.util.List;
@Getter
public class ConfigBuilder {
	private final List<Student> students = new ArrayList<>();
	public void students(Closure<?> closure) {
		closure.setDelegate(this);
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		closure.call();
	}

	public void student(Closure<?> closure) {
		StudentData data = new StudentData();
		closure.setDelegate(data);
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		closure.call();


		students.add(new Student(data.fio, data.githubNick, data.repoUrl));
	}

	public static class StudentData {
		public String fio;
		public String githubNick;
		public String repoUrl;
	}
}
