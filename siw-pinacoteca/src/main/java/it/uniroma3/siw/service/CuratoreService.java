package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Area;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.repository.CuratoreRepository;

@Service
public class CuratoreService {

	@Autowired
	protected CuratoreRepository curatoreRepository;
	
	public List<Curatore> findAll() {
		
		return this.curatoreRepository.findAll();
	}
	
	public Curatore findById(Long id){
		Optional<Curatore> curatore = this.curatoreRepository.findById(id);
		return curatore.orElse(null);
	}
	
//	public Curatore findByArea(Area area) {
//		Optional<Curatore> curatore = this.curatoreRepository.findByArea(area);
//		return curatore.orElse(null);
//	}
	
	public Curatore save(Curatore curatore) {
		return this.curatoreRepository.save(curatore);
	}
	
	public void deleteById(Long id) {
		if(curatoreRepository.existsById(id)) {
			curatoreRepository.deleteById(id);
		}else {
			throw new IllegalArgumentException("Non esiste nessun curatore con questo codice fiscale");
		}
	}
}
