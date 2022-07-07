package com.testesapi.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testesapi.domain.User;
import com.testesapi.domain.dto.UserDto;
import com.testesapi.exceptions.DataIntegratyViolationException;
import com.testesapi.exceptions.ObjectNotFoundException;
import com.testesapi.repository.UserRepository;
import com.testesapi.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Override
	public User findById(Integer id) {
		Optional<User> obj = userRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado"));
	}


	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}


	@Override
	public User create(UserDto userDto) {
		findByEmail(userDto);
		return userRepository.save(mapper.map(userDto, User.class));
	}
	
	public void findByEmail(UserDto userDto) {
		Optional<User> user = userRepository.findByEmail(userDto.getEmail());
		if (user.isPresent() && !userDto.getId().equals(user.get().getId())) {
			throw new DataIntegratyViolationException("E-mail já cadastrado");
		}
		
	}


	@Override
	public User update(UserDto userDto) {
		findByEmail(userDto);
		return userRepository.save(mapper.map(userDto, User.class));
	}


	@Override
	public void delete(Integer id) {
		findById(id);
		userRepository.deleteById(id);
	}

}
