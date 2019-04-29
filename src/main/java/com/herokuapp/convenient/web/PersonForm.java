package com.herokuapp.convenient.web;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonForm {
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
}
