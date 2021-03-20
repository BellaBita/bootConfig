package com.bellavita.bootDemo.apps;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StudentProperties.class)
public class StudentConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public Student student(StudentProperties prop) {
		System.out.println("닝기리 002");
		Student s = new Student();
		s.setAge(prop.getAge());
		s.setName(prop.getName());
		return s;
	}

}
