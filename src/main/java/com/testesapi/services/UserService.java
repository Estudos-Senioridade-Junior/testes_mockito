package com.testesapi.services;

import java.util.List;

import com.testesapi.domain.User;
import com.testesapi.domain.dto.UserDto;


public interface UserService {

	User findById(Integer id);
	
	List<User> findAll();
	
	User create(UserDto userDto);
	
	User update (UserDto userDto);
	
	void delete (Integer id);
}
