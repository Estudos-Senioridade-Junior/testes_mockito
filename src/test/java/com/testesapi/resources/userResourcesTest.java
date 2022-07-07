package com.testesapi.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import com.testesapi.domain.User;
import com.testesapi.domain.dto.UserDto;
import com.testesapi.services.UserService;

@SpringBootTest
class userResourcesTest {

	@InjectMocks
	private UserResources resource;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private UserService service;
	
	private User user;
	
	private UserDto userDto;
	
	private Optional<User> optionalUser;
	
	
	private static final String OBJETO_NÃO_ENCONTRADO = "Objeto não encontrado";

	private static final String PASSWORD 	= "123";

	private static final String EMAIL 		= "gigbby@gmail.com";

	private static final String NOME 		= "Patrick";

	private static final Integer ID 		= 1;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startUser();
	}
	
	private void startUser() {
		user = new User(ID, NOME, EMAIL, PASSWORD);
		userDto = new UserDto(ID, NOME, EMAIL, PASSWORD);
		optionalUser = Optional.of(new User(ID, NOME, EMAIL, PASSWORD));
	}
	
	@Test
	void whenFindById_ThenReturnSucess() {
		Mockito.when(service.findById(Mockito.anyInt())).thenReturn(user);
		Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDto);
		
		ResponseEntity<UserDto> response = resource.findById(ID);
		
		assertNotNull(response);
		assertEquals(response.getBody(), userDto);
		assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
		assertEquals(UserDto.class, response.getBody().getClass());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(EMAIL, response.getBody().getEmail());
		assertEquals(PASSWORD, response.getBody().getPassword());
		
	}

	@Test
	void whenFindAll_ThenReturnSucess() {
		Mockito.when(service.findAll()).thenReturn(List.of(user));
		Mockito.when(mapper.map(Mockito.any(), Mockito.any())).
			thenReturn(userDto);
	
		ResponseEntity<List<UserDto>> response = resource.findAll();
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(List.of(userDto),response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ArrayList.class, response.getBody().getClass());
		assertEquals(UserDto.class, response.getBody().get(0).getClass());
		
		assertEquals(ID, response.getBody().get(0).getId());
		assertEquals(NOME, response.getBody().get(0).getNome());
		assertEquals(EMAIL, response.getBody().get(0).getEmail());
		assertEquals(PASSWORD, response.getBody().get(0).getPassword());
	}

	@Test
	void whenCreate_ThenReturnCreated() {
		Mockito.when(service.create(Mockito.any())).thenReturn(user);
	
		ResponseEntity<UserDto> response = resource.create(userDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
 		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDto.class, response.getBody().getClass());
	}

	@Test
	void whenUpdate_ThenReturnSucess() {
		Mockito.when(service.update(Mockito.any())).thenReturn(user);
		Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDto);
		
		ResponseEntity<UserDto> response = resource.update(userDto, ID);//poderia passar any() any() também
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDto.class,response.getBody().getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(EMAIL, response.getBody().getEmail());
	}

	@Test
	void whenDelete_ThenReturnSucess() {
		Mockito.doNothing().when(service).delete(Mockito.anyInt());
		
		ResponseEntity<UserDto> response = resource.delete(ID);
		
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		Mockito.verify(service,times(1)).delete(ID);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

}
