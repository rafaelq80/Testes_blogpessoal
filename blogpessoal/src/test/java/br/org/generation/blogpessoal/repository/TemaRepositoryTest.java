package br.org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.org.generation.blogpessoal.model.Tema;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemaRepositoryTest {
    
 
	@Autowired
	private TemaRepository temaRepository;
	
	@BeforeAll
	void start() {

		temaRepository.save(new Tema(0L, "Linguagem Java"));

		temaRepository.save(new Tema(0L, "Linguagem C#"));

		temaRepository.save(new Tema(0L, "Linguagem Phyton"));

		temaRepository.save(new Tema(0L, "Linguagem GO"));

		temaRepository.save(new Tema(0L, "Javascript"));
	
    }
	
	@Test
	@DisplayName("Retorna a Descrição")
	public void deveRetornarUmTema() throws Exception {

		Optional<Tema> tema = temaRepository.findByDescricao("Linguagem Java");
		assertTrue(tema.get().getDescricao().equals("Linguagem Java"));
	}
	
    
	@Test
	@DisplayName("Retorna 4 Temas semelhantes")
	public void deveRetornarQuatroTemas() {

		/** Caso a tabela esteja com muitos dados cadastrados, este teste poderá falhar */

		List<Tema> listaDeTemas = temaRepository.findAllByDescricaoContainingIgnoreCase("linguagem");
		assertEquals(4, listaDeTemas.size());
	}
	

}
