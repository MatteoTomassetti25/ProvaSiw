package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Area;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.repository.AreaRepository;
import jakarta.transaction.Transactional;

@Service
public class AreaService {

	@Autowired
	protected AreaRepository areaRepository;
	
	public List<Area> findAll(){
		return this.areaRepository.findAll();
	}
	
	public Area findById(Long id) {
		Optional<Area> area = this.areaRepository.findById(id);
		return area.orElse(null);
	}
	
	public Area findByNome(String nome) {
		Optional<Area> area = this.areaRepository.findByNome(nome);
		return area.orElse(null);
	}
	
//	public Area findByCuratore(Curatore curatore) {
//		Optional<Area> area = this.areaRepository.findByCuratore(curatore);
//		return area.orElse(null);
//	}
	
	public Area save(Area area) {
		return this.areaRepository.save(area);
	}
	
	@Transactional
	public void deleteById(Long id) {
		if(this.areaRepository.existsById(id)) {
			this.areaRepository.deleteById(id);
		}else {
			throw new IllegalArgumentException("Non esistono aree con questo id");
		}
	}
}
