package com.testesapi.resources.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.testesapi.exceptions.DataIntegratyViolationException;
import com.testesapi.exceptions.ObjectNotFoundException;

class ResourceExceptionHandlerTest {

	private static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";
	private static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
	@InjectMocks
	ResourceExceptionHandler exceptionHandler;
	
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void whenObjectNotFoundException_ThenReturnResponseEntity() {
		ResponseEntity<StandardError> response = exceptionHandler
				.objectNotFound(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO),
						new MockHttpServletRequest());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(OBJETO_NAO_ENCONTRADO,response.getBody().getError());
		assertEquals(StandardError.class, response.getBody().getClass());
		assertEquals(404, response.getBody().getStatus());
		assertNotEquals("/user/2", response.getBody().getPath());
		assertNotEquals(LocalDateTime.now(), response.getBody().getTimetamp());
		
	}

	@Test
	void whenEmailViolation_ThenReturnResponseEntity() {
		ResponseEntity<StandardError> response = exceptionHandler
				.violacaoEmail(new DataIntegratyViolationException(EMAIL_JA_CADASTRADO),
						new MockHttpServletRequest());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(EMAIL_JA_CADASTRADO, response.getBody().getError());
		assertEquals(StandardError.class, response.getBody().getClass());
		assertEquals(400, response.getBody().getStatus());
		
	}

}
