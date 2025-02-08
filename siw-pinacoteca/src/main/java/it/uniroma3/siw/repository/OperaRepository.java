package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.model.Opera;

public interface OperaRepository extends CrudRepository<Opera, Long>{
	
	public boolean existsByTitolo(String titolo);
	
	public List<Opera> findAll();
	
	public Optional<Opera> findByTitolo(String titolo);
	
	public Optional<Opera> findById(Long id);
	
//	public Optional<Opera> findByArtista(Artista autore);
	
	public void deleteById(Long id);

}
