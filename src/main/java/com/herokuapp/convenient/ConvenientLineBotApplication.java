package com.herokuapp.convenient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Component;

//@EnableSpringConfigured
@SpringBootApplication
//@EntityScan(basePackageClasses = {ConvenientLineBotApplication.class, Jsr310JpaConverters.class})
public class ConvenientLineBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvenientLineBotApplication.class, args);
	}

}
