# Implementando testes com o Spring Testing no Blog Pessoal

Nesta atividade iremos implementar os testes nas Camadas Model, Repository e Controller da Classe Usuário. 

Utilize o e-book sobre SpringTesting (<a href="https://github.com/conteudoGeneration/Spring-com-J-unit/blob/main/ebook/Spring_Testing_v2.pdf" target="_blank">Clique aqui</a>) e as instruções descritas abaixo como referências para implementar  os testes nas 3 Camadas da Classe Usuario.

## Boas Práticas

1. <a href="#dep">Configure as Dependências no arquivo pom.xml</a>
2. <a href="#dtb">Configure o Banco de Dados (db_blogpessoaltest)</a>
3. <a href="#pac">Prepare a estrutura de pacotes para os testes</a>
4. <a href="#mod">Crie a Classe de testes na Camada Model: UsuarioTest</a>
5. <a href="#rep">Crie a Classe de testes na Camada Repository: UsuarioRepositoryTest</a>
6. <a href="#ctr">Crie a Classe de testes na Camada Controller: UsuarioControllerTest</a>
7. <a href="#run">Execute os testes</a>
8. <a href="#ref">Referências</a>



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

1)  Crie a **Source Folder resources** em **src/test**

2) Na **Source Folder resources**, crie o arquivo **application.properties**, para configurarmos a conexão com o Banco de Dados de testes

<div align="center"><img src="https://i.imgur.com/phqRE9r.png" title="source: imgur.com" /></div>

3) Insira neste arquivo as seguintes linhas:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost/db_blogpessoaltest? createDatabaseIfNotExist=true&serverTimezone=UTC&useSSl=false
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.show-sql=true
```
Observe que na **linha 2**, o nome do Banco de dados possui a palavra **test** para indicar que será apenas para a execução dos testes.

Na **linha 4**, configure a senha do usuário root criada na instalação do MySQL na sua máquina local



<h2 id="pac">Estrutura de pacotes</h2>


Na Source Folder de Testes (**src/test/java**) , observe que existe uma estrutura de pacotes idêntica a Source Folder Main (**src/main/java**). Crie na Source Folder de Testes as packages Model, Repository e Controller. 

<div align="center"><img src="https://i.imgur.com/Z00I4BB.png" title="source: imgur.com" /></div>

O Processo de criação dos arquivos é o mesmo do código principal, exceto o nome dos arquivos que deverão ser iguais aos arquivos da Source Folder Main (**src/main/java**) acrescentando a palavra Test no final. 

<b>Exemplo: </b>
<b>UsuarioRepository -> UsuarioRepositoryTest</b>.

<div align="center"><img src="https://i.imgur.com/5KerGKB.png" title="source: imgur.com" /></div>



<h2 id="mod">Classe UsuarioTest</h2>

Crie a classe UsuarioTest na package **model**, na Source Folder de Testes (**src/test/java**) 

```java
package br.org.generation.blogpessoal.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
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
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    Validator validator = factory.getValidator();

    @BeforeEach
    public void start() throws ParseException {

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
    @DisplayName("❌ Valida Atributos Nulos")
    void testValidaAtributosNulos() {
        
        usuarioErro.setLogin("paulo@email.com.br");
        Set<ConstraintViolation<Usuario>> violacao = validator.validate(usuarioErro);
        System.out.println(violacao.toString());
        assertFalse(violacao.isEmpty());
   
    }

}
```

💥 Para inserir os emojis na annotation @DisplayName, utilize as teclas de atalho **Windows + Ponto**



<h2 id="rep">Classe UsuarioRepositoryTest</h2>

Crie a classe UsuarioRepositoryTest na package **repository**, na Source Folder de Testes (**src/test/java**) 

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
        Usuario usuario = new Usuario(0, "João Silva", "joao@email.com.br", "13465278", data);
        
        if(usuarioRepository.findByLogin(usuario.getLogin()) != null)
			usuarioRepository.save(usuario);
        
        usuario = new Usuario(0, "Manuel da Silva", "manuel@email.com.br", "13465278", data);

        if(usuarioRepository.findByLogin(usuario.getLogin()) != null)
            usuarioRepository.save(usuario);

        usuario = new Usuario(0, "Fred da Silva", "frederico@email.com.br", "13465278", data);

        if(usuarioRepository.findByLogin(usuario.getLogin()) != null)
            usuarioRepository.save(usuario);

       	usuario = new Usuario(0, "Paulo Antunes", "paulo@email.com.br", "13465278", data);

        if(usuarioRepository.findByLogin(usuario.getLogin()) != null)
            usuarioRepository.save(usuario);
  }

    @Test
    @DisplayName("💾 Retorna o nome")
    public void findFirstByNomeRetornaNome() throws Exception {
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

Crie a classe UsuarioControllerTest na package **controller**, na Source Folder de Testes (**src/test/java**) 

```java
package br.org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.org.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
  
    @Autowired
    private TestRestTemplate testRestTemplate;

    private Usuario usuario;
    private Usuario usuarioUpdate;

    @BeforeAll
    public void start() throws ParseException {

        LocalDate dataPost = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        /**
	    * Esta linha gera o usuário admin. Descomente esta linha, 
	    * comente a próxima linha e rode o teste Cadastrar Usuário 
	    * sozinho para criar o usuário admin@email.com.br no Banco 
	    * de Dados. 
	    *
	    * Em seguida faça o processo inverso.
        * 
        * Este usuário será utilizado para logar
        * na API nos demais testes nos endpoints protegidos na API
        * 
        */
        
       //usuario = new Usuario(0L, "Administrador", "admin@email.com.br", "admin123", dataPost);
       
       usuario = new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", dataPost);

        LocalDate dataPut = LocalDate.parse("2000-07-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        usuarioUpdate = new Usuario(1L, "João da Silva Souza", "joao@email.com.br", "joao123", dataPut);
  
    }

    @Disabled
    @Test
    @DisplayName("✔ Cadastrar Usuário!")
    public void deveRealizarPostUsuario() {

        HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuario);
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    
    }
  
    @Test
    @DisplayName("👍 Listar todos os Usuários!")
    public void deveMostrarTodosUsuarios() {
        ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("admin@email.com.br", "admin123").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

  }

    @Test
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
.exchange("/v1/fornecedor", HttpMethod.GET, null, String.class);
```



<h2  id="run">Executando os testes no Eclipse e no STS</h2>

1) No lado esquerdo superior, na Guia **Project**, na Package **src/test/java**, clique com o botão direito do mouse sobre um dos testes e clique na opção **Run As->JUnit Test**.

<a href="https://imgur.com/HJN6A6p"><img src="https://i.imgur.com/HJN6A6p.jpg" title="source: imgur.com" /></a>


2) Para acompanhar os testes, ao lado da Guia **Project**, clique na Guia **JUnit**.

<a href="https://imgur.com/vdLvg6o"><img src="https://i.imgur.com/vdLvg6o.jpg" title="source: imgur.com" /></a>

 3) Se todos os testes passarem, a Guia do JUnit ficará com uma faixa verde (janela 01). Caso algum teste não passe, a Guia do JUnit ficará com uma faixa vermelha (janela 02). Neste caso, observe o item <b>Failure Trace</b> para identificar o (s) erro (s).

<div align="center">
<table width=100%>
	<tr>
		<td width=50%><div align="center"><a href="https://imgur.com/fVb4UAc"><img  src="https://i.imgur.com/fVb4UAc.png" title="source: imgur.com" /></a>
		<td width=50%><div align="center"><a href="https://imgur.com/xB1obRN"><img src="https://i.imgur.com/xB1obRN.png" title="source: imgur.com" /></a>
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

