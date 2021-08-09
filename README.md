# Implementando testes com o Spring Testing no Blog Pessoal

Nesta atividade iremos implementar os testes nas Camadas Model, Repository e Controller da Classe Usuário. 

Utilize o e-book sobre SpringTesting (<a href="https://github.com/rafaelq80/tdd_blogpessoal/blob/main/ebook/junit_teste_blog_pessoal.pdf" target="_blank">Clique aqui</a>) e as instruções descritas abaixo como referências para implementar  os testes nas 3 Camadas da Classe Usuario.

## Boas Práticas

1. <a href="#dep">Configure as Dependências no arquivo pom.xml</a>
2. <a href="#dtb">Configure o Banco de Dados (db_blogpessoaltest)</a>
3. <a href="#pac">Prepare a estrutura de pacotes para os testes</a>
4. <a href="#mcon">Crie os métodos construtores na Classe Usuario</a>
5. <a href="#mod">Crie a Classe de testes na Camada Model: UsuarioTest</a>
6. <a href="#rep">Crie a Classe de testes na Camada Repository: UsuarioRepositoryTest</a>
7. <a href="#ctr">Crie a Classe de testes na Camada Controller: UsuarioControllerTest</a>
8. <a href="#run">Execute os testes</a>
9. <a href="#ref">Referências</a>



<h2 id="dep"> Dependências</h2>

No arquivo, **pom.xml**, vamos alterar a linha:

```xml
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-test</artifactId>
    	<scope>test</scope>
    </dependency>
```
Para:
```xml
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-test</artifactId>
    	<scope>test</scope>
    	<exclusions>
    		<exclusion>
    			<groupId>org.junit.vintage</groupId>
    			<artifactId>junit-vintage-engine</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>
```
*Essa alteração irá ignorar as versões anteriores ao **JUnit 5** (vintage).



<h2 id="dtb">Banco de Dados</h2>



Agora vamos configurar um Banco de dados de testes para não usar o Banco de dados principal.

1) No lado esquerdo superior, na Guia **Package Explorer**, clique sobre a pasta do projeto com o botão direito do mouse e clique na opção **New->Source folder**

<div align="center"><img src="https://i.imgur.com/GYKQsnW.png" title="source: imgur.com" /></div>

2) Em **Source Folder**, no item **Folder name**, informe o caminho como mostra a figura abaixo (**src/test/resources**), e clique em **Finish**:

<div align="center"><img src="https://i.imgur.com/lZ6FEDX.png" title="source: imgur.com" /></div>

3. Na nova Source Folder (**src/test/resources**) , crie o arquivo **application.properties**, para configurarmos a conexão com o Banco de Dados de testes

4. No lado esquerdo superior, na Guia **Package explorer**, na Package **src/test/resources**, clique com o botão direito do mouse e clique na opção **New->File**.

   <div align="center"><img src="https://i.imgur.com/j7ckkJy.png" title="source: imgur.com" /></div>

5. Em File name, digite o nome do arquivo (**application.properties**) e clique em **Finish**. 

<div align="center"><img src="https://i.imgur.com/TGusKTm.png" title="source: imgur.com" /></div>

6) Veja o arquivo criado na  **Package Explorer** 

<div align="center"><img src="https://i.imgur.com/phqRE9r.png" title="source: imgur.com" /></div>

7) Insira no arquivo application.properties as seguinte linhas:

```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database=mysql
spring.datasource.url=jdbc:mysql://localhost/db_testeblogpessoal?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo&useSSl=false
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=Brazil/East


```

Observe que o nome do Banco de dados possui a palavra **teste** para indicar que será apenas para a execução dos testes.

Não esqueça de configurar a senha do usuário root.



<h2 id="pac">Estrutura de pacotes</h2>



Na Source Folder de Testes (**src/test/java**) , observe que existe uma estrutura de pacotes idêntica a Source Folder Main (**src/main/java**). Crie na Source Folder de Testes as packages Model, Repository e Controller como mostra a figura abaixo. 

<div align="center"><img src="https://i.imgur.com/Z00I4BB.png" title="source: imgur.com" /></div>

O Processo de criação dos arquivos é o mesmo do código principal, exceto o nome dos arquivos que deverão ser iguais aos arquivos da Source Folder Main (**src/main/java**) acrescentando a palavra Test no final como mostra a figura abaixo. 

<b>Exemplo: </b>
<b>UsuarioRepository -> UsuarioRepositoryTest</b>.

<div align="center"><img src="https://i.imgur.com/5KerGKB.png" title="source: imgur.com" /></div>



<h2 id="mcon">Métodos Construtores na Classe Usuario</h2>



Na Classe Usuario, na canada Model, vamos criar 2 métodos construtores: o primeiro com todos os atributos, exceto o postagens e um segundo método vazio, ou seja, sem atributos.

```java
package br.org.generation.blogpessoal.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull(message = "O atributo nome é obrigatório")
	@Size(min = 5, max = 100, message = "O atributo nome deve conter no mínimo 05 e no máximo 100 caracteres")
	private String nome;
	
	@NotNull(message = "O atributo usuário é obrigatório")
	@NotBlank(message = "O atributo usuário não pode ser vazio")
	@Email(message = "O atributo usuário deve ser um email")
	private String usuario;
	
	@NotNull(message = "O atributo senha é obrigatório")
	@Size(min = 8, message = "O atributo senha deve ter no mínimo 8 caracteres")
	private String senha;
	
	@Column(name = "dt_nascimento")
	@JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dataNascimento;
	
	@OneToMany (mappedBy = "usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List <Postagem> postagem;

	// Primeiro método Construtor

	public Usuario(long id, String nome, String usuario, String senha, LocalDate dataNascimento) {
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
	}

	// Segundo método Construtor

	public Usuario() {	}


	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public LocalDate getDataNascimento() {
		return this.dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<Postagem> getPostagem() {
		return this.postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

}

```



<h2 id="mod">Classe UsuarioTest</h2>



A Classe UsuarioTest será utilizada parta testar a Classe Model do Usuario. Crie a classe UsuarioTest na package **model**, dentro da Source Folder de Testes (**src/test/java**) 

**Importante:** O Teste da Classe Usuario da camada Model, não utiliza o Banco de Dados.

```java
package br.org.generation.blogpessoal.model;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioTest {
    
    public Usuario usuario;
    public Usuario usuarioErro = new Usuario();

	@Autowired
	private  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	
	Validator validator = factory.getValidator();

	@BeforeEach
	public void start() {

		LocalDate data = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		usuario = new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", data);

	}

	@Test
	@DisplayName("✔ Valida Atributos Não Nulos")
	void testValidaAtributos() {

		Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuario);
		
		System.out.println(violacao.toString());

		assertTrue(violacao.isEmpty());
	}
    
    @Test
	@DisplayName("✖ Não Valida Atributos Nulos")
	void  testNaoValidaAtributos() {

		Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuarioNulo);
		System.out.println(violacao.toString());

		assertTrue(violacao.isEmpty());
	}

}
```

💥 Para inserir os emojis na annotation **@DisplayName**, utilize as teclas de atalho **Windows + Ponto**



<h2 id="rep">Classe UsuarioRepositoryTest</h2>



A Classe UsuarioRepositoryTest será utilizada parta testar a Classe Repository do Usuario. Crie a classe UsuarioRepositoryTest na package **repository**, na Source Folder de Testes (**src/test/java**)

**Importante:** O Teste da Classe UsuarioRepository da camada Repository, utiliza o Banco de Dados, entretanto ele não criptografa a senha ao gravar um novo usuario no Banco de dados. O teste não utiliza a Classe de Serviço UsuarioService para gravar o usuário. O Teste utiliza o método save(), da Classe JpaRepository de forma direta. 

```java
package br.org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.org.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
    
    @Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() throws ParseException {
	   
		LocalDate data = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		Usuario usuario = new Usuario(0, "João da Silva", "joao@email.com.br", "13465278", data);
		if(!usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			usuarioRepository.save(usuario);
		
		usuario = new Usuario(0, "Manuel da Silva", "manuel@email.com.br", "13465278", data);
		if(!usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			usuarioRepository.save(usuario);
		
		usuario = new Usuario(0, "Frederico da Silva", "frederico@email.com.br", "13465278", data);
		if(!usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			usuarioRepository.save(usuario);

        usuario = new Usuario(0, "Paulo Antunes", "paulo@email.com.br", "13465278", data);
        if(!usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            usuarioRepository.save(usuario);
	}
	
	@Test
	@DisplayName("💾 Retorna o nome")
	public void findByNomeRetornaNome() throws Exception {

		Usuario usuario = usuarioRepository.findByNome("João da Silva");
		assertTrue(usuario.getNome().equals("João da Silva"));
	}
	
	@Test
	@DisplayName("💾 Retorna 3 usuarios")
	public void findAllByNomeContainingIgnoreCaseRetornaTresUsuarios() {

		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
	}

	@AfterAll
	public void end() {
		
		usuarioRepository.deleteAll();
		
	}
}
```



<h2 id="ctr">Classe UsuarioControllerTest</h2>



A Classe UsuarioControllerTest será utilizada parta testar a Classe Controller do Usuario. Crie a classe UsuarioControllerTest na package **controller**, na Source Folder de Testes (**src/test/java**) 

**Importante:** Para executar todos os testes da Classe UsuarioControllerTest de uma única vez, faça o Drop do Banco de Dados de testes antes. Caso contrário será necessário verificar o id do usuário que será alterado pelo método PUT no banco de dados via MySQL Workbench antes de executar teste.

```java
package br.org.generation.blogpessoal.controller;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
    
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private Usuario usuario;
	private Usuario usuarioUpdate;
	private Usuario usuarioAdmin;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	public void start() throws ParseException {

		LocalDate dataAdmin = LocalDate.parse("1990-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuarioAdmin = new Usuario(0L, "Administrador", "admin@email.com.br", "admin123", dataAdmin);

		if(!usuarioRepository.findByUsuario(usuarioAdmin.getUsuario()).isPresent()) {

            HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioAdmin);
			testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
			
		}
		
		LocalDate dataPost = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuario = new Usuario(0L, "Paulo Antunes", "paulo@email.com.br", "13465278", dataPost);
        
		LocalDate dataPut = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuarioUpdate = new Usuario(2L, "Paulo Antunes de Souza", "paulo_souza@email.com.br", "souza123", dataPut);
	}

	@Test
	@Order(1)
    @DisplayName("✔ Cadastrar Usuário!")
	public void deveRealizarPostUsuario() {

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuario);

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

	}

	@Test
	@Order(2)
    @DisplayName("👍 Listar todos os Usuários!")
	public void deveMostrarTodosUsuarios() {
		
		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("admin@email.com.br", "admin123").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
    @Order(3)
	@DisplayName("😳 Alterar Usuário!")
	public void deveRealizarPutUsuario() {

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("admin@email.com.br", "admin123").exchange("/usuarios/alterar", HttpMethod.PUT, request, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
}
```

Observe que como habilitamos em nosso Blog Pessoal o **Spring Security** com autenticação do tipo **BasicAuth** na API, o Objeto **testRestTemplate** deverá passar um usuário e a sua respectiva senha para realizar os testes. 

**Exemplo:**

```java
ResponseEntity<String> resposta = testRestTemplate
.withBasicAuth("admin@email.com.br", "admin123")
.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
```



<h2  id="run">Executando os testes no Eclipse / STS</h2>



1) No lado esquerdo superior, na Guia **Project**, na Package **src/test/java**, clique com o botão direito do mouse sobre o teste que você deseja executar e clique na opção **Run As->JUnit Test**.

<div align="center"><img src="https://i.imgur.com/Ol2N93J.png" title="source: imgur.com" /></div>


2) Para acompanhar os testes, ao lado da Guia **Project**, clique na Guia **JUnit**.

<div align="center"><img src="https://i.imgur.com/JvC0kS3.png" title="source: imgur.com" /></div>

 3) Se todos os testes passarem, a Guia do JUnit ficará com uma faixa verde (janela 01). Caso algum teste não passe, a Guia do JUnit ficará com uma faixa vermelha (janela 02). Neste caso, observe o item <b>Failure Trace</b> para identificar o (s) erro (s).

<div align="center">
<table width=100%>
	<tr>
		<td width=50%><div align="center"><img src="https://i.imgur.com/TeiTjQW.png" title="source: imgur.com" /></div>
		<td width=50%><div align="center"><img src="https://i.imgur.com/7b13sd6.png" title="source: imgur.com" /></div>
	</tr>
	<tr>
		<td><div align="center">Janela 01: <i> Testes aprovados.
		<td><div align="center">Janela 02: <i> Testes reprovados.
	</tr>
</table>
</div>
Ao escrever testes, sempre verifique se a importação dos pacotes do JUnit na Classe de testes estão corretos. O JUnit 5 tem como pacote base <b><i>org.junit.jupiter.api</i></b>.




<h2 id="ref">Referências</h2>

<a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testing-introduction" target="_blank">Documentação Oficial do Spring Testing</a>

<a href="https://junit.org/junit5/" target="_blank">Página Oficial do JUnit5</a>

<a href="https://junit.org/junit5/docs/current/user-guide/" target="_blank">Documentação Oficial do JUnit 5</a>

