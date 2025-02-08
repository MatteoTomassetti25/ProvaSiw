package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.repository.OperaRepository;

@Service
public class OperaService {

	@Autowired
	protected OperaRepository operaRepository;
	
	public List<Opera> findAll(){
		return this.operaRepository.findAll();
	}
	
	public Opera findByNome(String titolo) {
		Optional<Opera> opera = this.operaRepository.findByTitolo(titolo);
		return opera.orElse(null);
	}
	
	public Opera findById(Long id) {
		Optional<Opera> opera = this.operaRepository.findById(id);
		return opera.orElse(null);
	}
	
//	public Opera findByArtista(Artista artista) {
//		Optional<Opera> opera = this.operaRepository.findByArtista(artista);
//		return opera.orElse(null);
//	}
	
	public Opera save(Opera opera) {
		return this.operaRepository.save(opera);
	}
	
	public void deleteById(Long id) {
		if(this.operaRepository.existsById(id)) {
			this.operaRepository.deleteById(id);
		}else {
			throw new IllegalArgumentException("Non esistono opere con questo id");
		}
	}
	
	public List<Integer> findAllAnni(){
		
		List<Integer> resuList = new ArrayList<Integer>();
		
		for(Opera elem : this.operaRepository.findAll()) {
			resuList.add(elem.getAnno());
		}
		
		
		return resuList;
	}
	
	
public List<String> findAllTecniche(){
		
		List<String> resuList = new ArrayList<>();
		
		for(Opera elem : this.operaRepository.findAll()) {
			resuList.add(elem.getTecnica());
		}
		
		
		return resuList;
	}
	
public List<Opera> findbyFiltri(Artista artista, Integer anno, String tecnica) {
    List<Opera> opere = operaRepository.findAll();
    List<Opera> result = new ArrayList<>();

    for (Opera elem : opere) {
        // Controlla se l'opera soddisfa tutti i filtri applicati
        boolean filtroArtista = (artista == null || elem.getArtista() != null && elem.getArtista().equals(artista));
        boolean filtroAnno = (anno == null || elem.getAnno() != null && elem.getAnno().equals(anno));
        boolean filtroTecnica = (tecnica == null || elem.getTecnica() != null && elem.getTecnica().equals(tecnica));

        // Se l'opera soddisfa tutti i filtri, aggiungila ai risultati
        if (filtroArtista && filtroAnno && filtroTecnica) {
            result.add(elem);
        }
    }

    return result;
}
}
