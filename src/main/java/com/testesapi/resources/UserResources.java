package com.testesapi.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.testesapi.domain.User;
import com.testesapi.domain.dto.UserDto;
import com.testesapi.services.UserService;



@RestController
@RequestMapping("/user")
public class UserResources {
	
	private static final String ID = "/{id}";
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserService service;
	
	@GetMapping(ID)
	public ResponseEntity<UserDto> findById ( @PathVariable ("id") Integer id){
		return ResponseEntity.ok().body(mapper.map(service.findById(id),UserDto.class));
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> findAll(){
		List<User> userList = service.findAll();
		List<UserDto> userListDto = userList.stream().map(x -> (mapper.map(x, UserDto.class))).collect(Collectors.toList());
		return ResponseEntity.ok().body(userListDto);
	}
	
	@PostMapping
	public ResponseEntity<UserDto> create(@RequestBody UserDto userDto){
		//User user = service.create(userDto);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path(ID)
				.buildAndExpand(service.create(userDto).getId()).toUri();
		
		return ResponseEntity.created(uri).build();
		
	}
	
	@PutMapping(ID)
	public ResponseEntity<UserDto> update (@RequestBody UserDto userDto, @PathVariable Integer id){
		userDto.setId(id);
		User usuarioAtualizado = service.update(userDto);
		return ResponseEntity.ok().body(mapper.map(usuarioAtualizado, UserDto.class));
	}
	
	@DeleteMapping(ID)
	public ResponseEntity<UserDto> delete (@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
