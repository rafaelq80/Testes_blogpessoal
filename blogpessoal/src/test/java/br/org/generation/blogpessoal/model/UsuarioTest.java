package br.org.generation.blogpessoal.model;

/**
 * Antes de rodar os testes, verifique se as importações estão corretas
 *  
 */

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * 
 *  Esta anotação indica que esta classe é uma classe de testes do
 *  tipo Spring Boot Test - Clasee de testes do Spring
 *  
 *  A opção webEnvironment = WebEnvironment.RANDOM_PORT indica que caso
 *  a sua API esteja rodando, a classe de testes irá procurar outra
 *  porta livre.
 * 
 *  Por uma questão de sobrecarga da máquina, recomendo rodar o teste sem
 *  a aplicação rodando.
 * 
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioTest {
    
    public Usuario usuario;
    public Usuario usuarioErro = new Usuario();
	
	/**
	 * 
	 *  Nas próximas 3 linhas após o comentário, foi injetado um objeto do tipo
	 *  ValidatorFactory que receberá todas as validações possíveis e na 
	 *  sequência o objeto do tipo Validator receberá essa lista para 
	 *  futuramente verificar as valiodações criadas na classe Usuario
	 * 
	 */

	@Autowired
	private  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	
	Validator validator = factory.getValidator();
	
	/**
	 * 
	 * A linha throws ParseException da definição do método start foi
	 * removida por se tornar desnecessária.
	 * 
	 */

	@BeforeEach
	public void start() {

		/**
		 * 
		 * Instancia um objeto do LocalDate definindo a data e o formato
		 * em nosso caso está no formato do MySQL
		 * Caso você não tenha criado o atributo dataAniversario, não será
		 * necessário instanciar este objeto
		 * 
		 */
		LocalDate data = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
        usuario = new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", data);

	}

	/**
	 * 
	 * Este teste irá verificar se todos os atributos do nosso objeto usuario
	 * estão preenchidos corretamente.
	 * 
	 * Se todas as regras da nossa classe Usuario forem satisfeitas, o teste 
	 * passará
	 * 
	 */

	@Test
	@DisplayName("✔ Valida Atributos Não Nulos")
	void testValidaAtributos() {
		
		/**
		 * Por definição, a classe ConstraintViolation trabalha com a Collection
		 * Set (semelhante a List, porém não aceita dados duplicados). 
		 * 
		 * Por isso criamos um objeto do tipo Set que recebedrá a lista de 
		 * ConstraintViolation da classe Usuario através do método 
		 * validator.validate(usuario)
		 *  
		 */

		Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuario);
		
		System.out.println(violacao.toString());
		
		/**
		 * 
		 * Para confirmar que todas as validações foram satisfeitas, ou seja, que o 
		 * teste passou, verificamos o tamanho (size) da nossa Collection.
		 * 
		 * Se for verdade (assertTrue) que a nossa collection Set está vazia, ou seja,
		 * nenhum erro foi encontrado, o teste passou!
		 */

		assertTrue(violacao.isEmpty());
	}

	/**
	 * 
	 * Este teste irá verificar se algum atributo do nosso objeto usuarioErro
	 * são nulos, ou seja, este teste só passará se algum atributo for nulo
	 * 
	 */

	@Test
	@DisplayName("❌ Valida Atributos Nulos")
	void testValidaAtributosNulos() {
		
		/**
		 * 
		 * Por definição, o objeto usuarioErro já possui todos os atribuitos nulos 
		 * por ter sido instanciado pelo nosso método construtor vazio
		 * 
		 * Para demonstrar isso, vamos alterar apenas o atributo usuario, mantendo os 
		 * demais atributos nulos
		 * 
		 */

		usuarioErro.setUsuario("paulo@email.com.br");

		Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuarioErro);
		
		System.out.println(violacao.toString());
		
		/**
		 * 
		 * Para confirmar que algumas validações não foram satisfeitas, ou seja, que o 
		 * teste passou, verificamos o tamanho (size) da nossa Collection.
		 * 
		 * Se for falso (assertFalse) que a nossa collection Set está vazia (Não estará vazia),
		 * ou seja, alguns erros foram encontrados, o teste passou!
		 * 
		 * Se neste teste você passar o objeto usuario ao invés de usuarioErro 
		 * na linha: Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuarioErro);
		 * o teste irá falhar
		 * 
		 */

		assertFalse(violacao.isEmpty());
		
	}
}