package br.org.generation.blogpessoal.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.generation.blogpessoal.model.Tema;
import br.org.generation.blogpessoal.repository.PostagemRepository;
import br.org.generation.blogpessoal.repository.TemaRepository;

@Service
public class TemaService {

	@Autowired
	private TemaRepository temaRepository;
	
	@Autowired
	private PostagemRepository postagemRepository;

	public List<Tema> trendTopics(){
		
		List<Tema> temas = temaRepository.findAll();
		
		for (Tema tema : temas) {
	
			tema.setQtdTema(postagemRepository.countPosts2(tema.getId()));
		}
		
		Collections.sort(temas, Collections.reverseOrder(Comparator.comparing(Tema::getQtdTema)));
		return temas;
	}
}
