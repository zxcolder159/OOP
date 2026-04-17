package ru.nsu.ermakov.dsl;

import groovy.lang.Closure;
import ru.nsu.ermakov.student.Student;

import java.util.ArrayList;
import java.util.List;

public class ConfigBuilder {
	private List<Student> students = new ArrayList<>();
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

	public List<Student> getStudents() {
		return students;
	}

	public static class StudentData {
		public String fio;
		public String githubNick;
		public String repoUrl;
	}
}
