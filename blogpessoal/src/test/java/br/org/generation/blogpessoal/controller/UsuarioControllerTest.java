package br.org.generation.blogpessoal.controller;

/**
 * Antes de rodar os testes, verifique se as importações estão corretas
 *  
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.org.generation.blogpessoal.model.Usuario;
import br.org.generation.blogpessoal.repository.UsuarioRepository;

/**
 * A anotação@TestMethodOrder(MethodOrderer.OrderAnnotation.class) habilita a opção
 * de forçar o Junit a executar todos os testes na ordem pré definida pela pessoa 
 * desenvolvedora através da anotação @Order(numero)
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
    
	/**
	 * 
	 *  Nas próximas 2 linhas após o comentário, foi injetado um objeto do tipo
	 *  TestRestTemplate que será utilizado para enviar uma requisição http para
	 *  a nossa API Rest, permitindo testar o funcionamento dos endpoints da nossa
	 *  classe UsuarioController
	 * 
	 */

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	/**
	 * Alteração: Foram criados 3 objetos Usuario:
	 * 1) usuario (Testar o método post) 
	 * 2) usuarioUpdate (Testar o método put)
	 * 3) usuarioAdmin (Criar o usuário para logar na API)
	 * 
	 * Injeção da classe Usuario Repository
	 */

	private Usuario usuario;
	private Usuario usuarioUpdate;
	private Usuario usuarioAdmin;

	@Autowired
	private UsuarioRepository usuarioRepository;

	
	@BeforeAll
	public void start() throws ParseException {

		LocalDate dataAdmin = LocalDate.parse("1990-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuarioAdmin = new Usuario(0L, "Administrador", "admin@email.com.br", "admin123", dataAdmin);

		/**
		 * Antes de iniciar os testes, verifica se o usuário admin não existe. 
		 * Se não existir, cria o usuario utilizando a estrutura TestRestTemplate
		 * 
		 * Importante: Não altere o usuario e a senha do usuario admin senão os testes
		 * irão falhar
		 */

		if(!usuarioRepository.findByUsuario(usuarioAdmin.getUsuario()).isPresent()) {
			
			/**
			 * Esta estrutura está detalhada no método Post
			 */

			HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioAdmin);
			testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
			
		}
		
		LocalDate dataPost = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuario = new Usuario(0L, "João da Silva dos Santos", "joao@email.com.br", "13465278", dataPost);
        
		/**
		 * Objeto usuarioUpdate utilizado para alterar dados do usuário
		 * Verifique se o id do usuário está correto no Banco de dados. Em nosso exemplo, estamos
		 * o id informado é 2
		 */

        LocalDate dataPut = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuarioUpdate = new Usuario(2L, "João da Silva dos Santos Souza", "joao@email.com.br", "joao123", dataPut);
	}

	/**
	 *  Teste do método Post - 1° teste
	 */

	@Test
	@Order(1)
    @DisplayName("✔ Cadastrar Usuário!")
	public void deveRealizarPostUsuario() {

		/**
		 * Cria uma requisição http utilizando o objeto usuario
		 * Semelhante ao que o Postman faz
		 */

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuario);

		/**
		 * Envia a requisição http através do método exchange da classe TestRestTemplate
		 * 
		 * Parâmetros:
		 * 
		 * Endpoint: caminho do endpoint
		 * Método: Post
		 * Requisição: request
		 * Claase de retorno: Usuario.class (objeto é do tipo Usuario)
		 */

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
		
		/**
		 * Verifica se a resposta da requisição (StatusCode) é igual a 201 (Created). 
		 * Em caso positivo, o teste passou!
		 */

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

	}

	/**
	 *  Teste do método Get - 2° teste
	 */
	
	@Test
	@Order(2)
    @DisplayName("👍 Listar todos os Usuários!")
	public void deveMostrarTodosUsuarios() {
		
		/**
		 * Envia a requisição http através do método exchange da classe TestRestTemplate
		 * 
		 * Como o método utiliza o verbo Get, ele não tem requisição
		 * 
		 * Parâmetros:
		 * 
		 * Endpoint: caminho do endpoint
		 * Verbo: Get
		 * Requisição: null
		 * Classe de retorno: String.class (objeto de retorno será uma String - lista de usuários)
		 * 
		 * Como a nossa API está com a segurança habilitada e o método de consulta não está 
		 * liberado de autenticação é necessário passar um usuário e uma senha.
		 * O objeto TestRestTemplate possui o método withBasicAuth() que se encarrega de fazer o login
		 * e passar o token gerado para o método,
		 * O Usuário admin será criado no método start() caso ele não exista no banco de dados
		 * 
		 */

		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("admin@email.com.br", "admin123").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		/**
		 * Verifica se a resposta da requisição (StatusCode) é igual a 200 (Ok). 
		 * Em caso positivo, o teste passou!
		 */

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	/**
	 *  Teste do método Put - 3° teste
	 */

	@Test
    @Order(3)
	@DisplayName("😳 Alterar Usuário!")
	public void deveRealizarPutUsuario() {

		/**
		 * Cria uma requisição http utilizando o objeto usuario
		 * Semelhante ao que o Postman faz
		 */

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioUpdate);

		/**
		 * Envia a requisição http através do método exchange da classe TestRestTemplate
		 * 
		 * Parâmetros:
		 * 
		 * Endpoint: caminho do endpoint
		 * Verbo: Put
		 * Requisição: request
		 * Claase de retorno: Usuario.class (objeto é do tipo Usuario)
		 */

		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("admin@email.com.br", "admin123").exchange("/usuarios/alterar", HttpMethod.PUT, request, Usuario.class);
		
		/**
		 * Verifica se a resposta da requisição (StatusCode) é igual a 200 (Ok). 
		 * Em caso positivo, o teste passou!
		 */

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
}
