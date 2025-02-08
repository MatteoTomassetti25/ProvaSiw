package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.repository.ArtistaRepository;
import it.uniroma3.siw.service.ArtistaService;
import it.uniroma3.siw.service.CuratoreService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ArtistaController {

	
	@Autowired
	protected ArtistaService artistaService;
	
	@Autowired
	protected ArtistaRepository artistaRepository;
	
	
	
	@GetMapping("/admin/operazioniArtista")
	public String operazioniArtista() {
		return "admin/operazioniArtista";
	}
	
	@GetMapping("/admin/inserimentoArtista")
	public String inserimentoArtista(Model model) {
		
		model.addAttribute("artista",new Artista());
		
		return "admin/formNewArtista";
	}
	
	@PostMapping("/admin/salvaArtista")
	public String salvaArtista(Artista artista, Model model) {
		
		this.artistaService.save(artista);
		model.addAttribute("messaggio", "artista con successo");
		return "admin/confermaSalvataggio";
	}
	
	
	@GetMapping("/admin/listaArtisti")
	public String listaArtisti(Model model) {
		model.addAttribute("artisti", this.artistaService.findAll());
		return "admin/listaArtisti";
	}
	
	
	@GetMapping("/admin/modificaArtista/{id}")
	public String getMethodName(@PathVariable("id") Long id, Model model) {
		
		Artista artista = this.artistaService.findById(id);
		model.addAttribute("artista", artista);
		
		return "admin/formModificaArtista";
	}
	
	@PostMapping("/admin/salvaModificaArtista/{id}")
	public String salvaModificaArtista(@PathVariable("id") Long id, Model model, Artista artista) {
		
		Artista artistaEsistente = this.artistaService.findById(id);
		
		artistaEsistente.setNome(artista.getNome());
		artistaEsistente.setCognome(artista.getCognome());
		artistaEsistente.setDataNascita(artista.getDataNascita());
		artistaEsistente.setDataMorte(artista.getDataMorte());
		artistaEsistente.setLuogoNascita(artista.getLuogoNascita());
		
		this.artistaService.save(artistaEsistente);
		model.addAttribute("messaggio", "modifica Artista successo");
		
		return "admin/confermaSalvataggio" ;
	}
	
	
	@GetMapping("/admin/eliminaArtista/{id}")
	public String eliminaArtisra(@PathVariable("id") Long id, Model model) {
		
		Artista artista = this.artistaService.findById(id);
		
		artista.getOpereArtista().clear();
		
		this.artistaService.deleteById(id);
		model.addAttribute("artisti", this.artistaService.findAll());
		model.addAttribute("messaggio", "eliminazione successo");
		
		return "admin/listaArtisti";
	}
	
	
	
	
}
