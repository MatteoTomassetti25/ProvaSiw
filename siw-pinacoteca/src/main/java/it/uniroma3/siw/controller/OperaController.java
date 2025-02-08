package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Artista;
import it.uniroma3.siw.model.Opera;
import it.uniroma3.siw.repository.CredenzialiRepository;
import it.uniroma3.siw.repository.OperaRepository;
import it.uniroma3.siw.service.AreaService;
import it.uniroma3.siw.service.ArtistaService;
import it.uniroma3.siw.service.OperaService;



@Controller
public class OperaController {

    @Autowired
    protected OperaRepository operaRepository;

    @Autowired
    protected OperaService operaService;

    @Autowired
    protected CredenzialiRepository credenzialiRepository;

    @Autowired
    protected ArtistaService artistaService;

    @Autowired
    protected AreaService areaService;

    
    @GetMapping("/admin/operazioniOpere")
    public String operazioniOpere() {
        return "admin/operazioniOpere";
    }

 
    @GetMapping("/admin/inserimentoOpera")
    public String inserimentoOpera(Model model) {
        model.addAttribute("opera", new Opera());
        model.addAttribute("artisti", this.artistaService.findAll());
        model.addAttribute("aree", this.areaService.findAll());
        return "admin/formNewOpera";
    }


    @PostMapping("/admin/salvaOpera")
    public String salvaOpera(Opera opera, Model model) {
    	
        this.operaService.save(opera);
        model.addAttribute("messaggio", "Opera salvata con successo");

        return "admin/confermaSalvataggio";
    }
    
    
    @GetMapping("/admin/listaOpere")
    public String listaOpere(Model model) {
       
    	model.addAttribute("opere", this.operaService.findAll());
    	return "admin/listaOpere";
    }
    
    @GetMapping("/admin/modificaOpera/{id}")
    public String modificaOpera(@PathVariable("id") Long id, Model model) {
    	
    	Opera opera = this.operaService.findById(id);
    	model.addAttribute("opera", opera);
    	model.addAttribute("artisti", this.artistaService.findAll());
    	model.addAttribute("collocazioni", this.areaService.findAll());
    	
        return "admin/formModificaOpera";
    }
    
    @PostMapping("/admin/salvaModificheOpera/{id}")
    public String salvaModificheOpera(@PathVariable("id") Long id, Opera opera, Model model ) {
        
    	Opera operaEsistente = this.operaService.findById(id);
        
    	operaEsistente.setTitolo(opera.getTitolo());
    	operaEsistente.setTecnica(opera.getTecnica());
    	operaEsistente.setAnno(opera.getAnno());
    	operaEsistente.setArtista(opera.getArtista());
    	operaEsistente.setCollocazione(opera.getCollocazione());
    	
    	this.operaService.save(operaEsistente);
    	model.addAttribute("messaggio", "opera modificata con successo");
    	
        return"admin/confermaSalvataggio";
    }
    
    
    @GetMapping("/admin/eliminaOpera/{id}")
    public String eliminaOpera(@PathVariable("id") Long id, Model model) {
    	
    	this.operaService.deleteById(id);
    	model.addAttribute("messaggio", "eliminazione successo");
    	
        return "admin/confermaSalvataggio";
    }
    
    @GetMapping("/listaOpere")
    public String listaOpereUtente(Model model) {
       
    	model.addAttribute("opere", this.operaService.findAll());
    	return "listaOpereUtente";
    }
    
    @GetMapping("/visualizzaDettaglio/{id}")
    public String visualizza(@PathVariable("id") Long id, Model model) {
    	
    	Opera opera = this.operaService.findById(id);
    	
    	model.addAttribute("opera", opera);
        return "visualizzaOpera";
    }
    
    
    @GetMapping("/filtraOpere")
    public String filtraOpere(Model model) {
        // Aggiunge gli artisti, gli anni e le tecniche disponibili per i filtri
        model.addAttribute("artisti", this.artistaService.findAll());
        model.addAttribute("anni", operaService.findAllAnni());
        model.addAttribute("tecniche", operaService.findAllTecniche());

        return "listaOpereFiltrate"; // Restituisce la vista con i filtri
    }
    
    
    @GetMapping("/risultatiFiltri")
    public String risultatiFiltri(@RequestParam(required = false) Long artistaId,
                                  @RequestParam(required = false) Integer anno,
                                  @RequestParam(required = false) String tecnica,
                                  Model model) {
        Artista artista = null;
        if (artistaId != null) {
            artista = artistaService.findById(artistaId);
        }

        List<Opera> opereFiltrate = operaService.findbyFiltri(artista, anno, tecnica);

        model.addAttribute("opere", opereFiltrate);
        model.addAttribute("selectedArtista", artista);
        model.addAttribute("selectedAnno", anno);
        model.addAttribute("selectedTecnica", tecnica);

        return "risultatiFiltri";
    }

    
}
