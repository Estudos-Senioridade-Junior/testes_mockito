package com.testesapi.services.impl;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.testesapi.domain.User;
import com.testesapi.domain.dto.UserDto;
import com.testesapi.exceptions.DataIntegratyViolationException;
import com.testesapi.exceptions.ObjectNotFoundException;
import com.testesapi.repository.UserRepository;
import com.testesapi.resources.UserResources;


@SpringBootTest //Define que a classe é de teste
class UserServiceImplTest {

	private static final String OBJETO_NÃO_ENCONTRADO = "Objeto não encontrado";

	private static final String PASSWORD 	= "123";

	private static final String EMAIL 		= "gigbby@gmail.com";

	private static final String NOME 		= "Patrick";

	private static final Integer ID 		= 1;

	//Aqui precisa de real pq vai acessar os métodos dessa classe
	@InjectMocks // É a classe que estamos testando. O injectmock cria uma instância real do userserviceImpl, porém os demais serão de mentira
	private UserServiceImpl service;
		
	@Mock //Não cria instância real
	private UserRepository userRepository;
	
	@Mock
	private UserResources resource;
	
	@Mock
	private ModelMapper mapper;
	
	
	private User user;
	private UserDto userDto;
	private Optional<User> optionalUser;
	
	
	@BeforeEach //Antes de tudo faça isso
	void setUp() {
		MockitoAnnotations.openMocks(this);//Inicia os mocks que estamos fazendo menção nesta classe. O this inicia todos os mocks da classe
		startUser(); //Inicia uma base de Users
	}
	
	//Gerando valores para users
	private void startUser() {
		user = new User(ID, NOME, EMAIL, PASSWORD);
		userDto = new UserDto(1, NOME, EMAIL, PASSWORD);
		optionalUser = Optional.of(new User(1, NOME, EMAIL, PASSWORD));
	}
	
	@Test//FindById
	void whenFindById_ThenReturnUserInstance() {
			//Está com import estático
			when(userRepository.findById(anyInt())) //Quando eu chamar o repository.findById, passando qualquer inteiro
			.thenReturn(optionalUser);//Então retorne um optional de User
		
		//Criando o cenário que se espera
		User response = service.findById(ID); //CHamada real
		//Eu sei que meu método principal sempre retorna um User. Caso alguém altere p exemplo p UserDto, o teste acusa
		
		assertNotNull(response);//Assegure que a resposta não será nula
		assertEquals(User.class, response.getClass()); //Assegure que o retorno do método não será nulo
		assertEquals(ID, response.getId()); //Assegure que o ID será o mesmo
		
		//Tentar fazer o teste de todos os atributos
		assertEquals(NOME, response.getNome());
		assertEquals(EMAIL, response.getEmail());
		
		//Para tirar a repetição do assertions, podemos fazer uma importação estática
			
	}
	
	@Test
	void whenFindById_ThenReturnObjectNotFound() {
		//O que ele está fazendo ? Quando chamar um findById lança uma exceçção de objeto não encontrado
		when(userRepository.findById(anyInt()))
		.thenThrow(new ObjectNotFoundException(OBJETO_NÃO_ENCONTRADO));
		
		try {
			service.findById(ID);			
		}catch (Exception ex) {
			assertEquals(ObjectNotFoundException.class, ex.getClass());//Verifique se a excessão lançada é do mesmo tipo
			assertEquals(OBJETO_NÃO_ENCONTRADO, ex.getMessage());
		}
	}

	@Test
	void whenFindAll_ThenReturnListOfUsers() {
		when(userRepository.findAll())
		.thenReturn(List.of(user));
		
		List<User> response = service.findAll();
		assertNotNull(response);
		assertEquals(1,response.size());
		assertEquals(response.get(0).getId(), ID);
		assertEquals(response.get(0).getEmail(), EMAIL);
		assertEquals(response.get(0).getNome(), NOME);
		assertEquals(response.get(0).getPassword(), PASSWORD);
		
	}

	@Test
	void whenCreate_TheReturnSucess() {
		when(userRepository.save(any()))
		.thenReturn(user);
		
		User response = service.create(userDto);
		
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(EMAIL, response.getEmail());
		assertEquals(PASSWORD, response.getPassword());

	}
	
	@Test
	void whenCreate_ThenReturnDataIntegratyViolationException() {
		
		//O findByEmail está dentro do create. Então eu mocko o email passado
		//e simulo uma situação de erro (setId(2) para cair dentro da excessão e verificar 
		//se de fato a excessão está funcionando
		
		//Neste caso eu simulei o create normal com um ID diferente para lançar excessão. 
		//No update eu já vou lançar a excessão no thenReturn
		when (userRepository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2);
			service.create(userDto);
		} catch (Exception ex) {
			assertEquals(DataIntegratyViolationException.class, ex.getClass());
			assertEquals("E-mail já cadastrado", ex.getMessage());
			
		}
	}


	@Test
	void whenUpdate_ThenReturnSucess() {
		when(userRepository.save(any()))
		.thenReturn(user);

		User response = service.update(userDto);
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(EMAIL, response.getEmail());
		assertEquals(PASSWORD, response.getPassword());

	}
	
	@Test
	void whenUpdate_ThenReturnDataIntegratyViolationException() {
		
		//O findByEmail está dentro do create. Então eu mocko o email passado
		//e simulo uma situação de erro (setId(2) para cair dentro da excessão e verificar 
		//se de fato a excessão está funcionando
		when (userRepository.findByEmail(anyString()))
			.thenThrow(new DataIntegratyViolationException("E-mail já cadastrado"));
		
		try {
			
			service.create(userDto);
		} catch (Exception ex) {
			assertEquals(DataIntegratyViolationException.class, ex.getClass());
			assertEquals("E-mail já cadastrado", ex.getMessage());
			
		}
	}

	@Test
	void whenDelete_ThenSucess() {
		when (userRepository.findById(anyInt())).thenReturn(optionalUser);
		doNothing().when(userRepository).deleteById(anyInt());
		
		service.delete(ID);
		//Como esse méOtodo não retorna nada não tem como fazer validação
		//Então chamamos o verify para verificar quantas vezes o método foi chamado
		verify(userRepository, times(1)).deleteById(anyInt());
	}
	
	@Test
	void deleteWithObjecNotFound() {
		when (userRepository.findById(anyInt()))
			.thenThrow(new ObjectNotFoundException(OBJETO_NÃO_ENCONTRADO));
		
		try {
			service.delete(ID);
			
		}catch (Exception ex) {
			assertEquals(ObjectNotFoundException.class, ex.getClass());
			assertEquals(OBJETO_NÃO_ENCONTRADO,ex.getMessage());
		}
		
	}

}
