package com.herokuapp.convenient.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/example")
public class ExampleController {

	@GetMapping
	String getName(@RequestBody PersonForm personForm) {
		return personForm.getFirstName() + personForm.getLastName();
	}

	@GetMapping(path = "echo")
	String getEcho() {
		System.out.println("echo");
		return "echo";
	}
	
	@PostMapping
	String getPostName(@RequestBody PersonForm personForm) {
		System.out.println(personForm.getFirstName());
		System.out.println("aa");
		return personForm.getFirstName() + personForm.getLastName();
	}
}
