package com.testesapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Integer id;
	private String nome;
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//Só é utilizado para escrita
	private String password;
	
	
}
