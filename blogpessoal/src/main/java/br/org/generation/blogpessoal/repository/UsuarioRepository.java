package br.org.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.generation.blogpessoal.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByUsuario(String usuario);

	/** 
	 * Método criado para a Sessão de testes
	 */ 
	public List<Usuario> findAllByNomeContainingIgnoreCase(String nome);

	/** 
	 * Método criado para a Sessão de testes
	 */ 
	public Usuario findByNome(String nome);

}
