package com.bellavita.bootDemo.apps;

public class Student {
	
	String name = null;
	
	int age = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public String toString() {
		return String.format("Student{name='%s', age=%d}", name, age);
	}
	
}
