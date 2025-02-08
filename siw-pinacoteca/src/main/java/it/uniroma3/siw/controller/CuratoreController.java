package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Area;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.service.AreaService;
import it.uniroma3.siw.service.CuratoreService;

@Controller
public class CuratoreController {

    @Autowired
    protected CuratoreService curatoreService;

    @Autowired
    protected AreaService areaService;

    @GetMapping("/admin/operazioniCuratore")
    public String operazioniCuratore() {
        return "admin/operazioniCuratore";
    }

    @GetMapping("/admin/inserimentoCuratore")
    public String inserimentoCuratore(Model model) {
        model.addAttribute("curatore", new Curatore());
        // Non caricare più le aree perché il curatore non avrà un'area al momento della creazione
        return "admin/formNewCuratore";
    }

    @PostMapping("/admin/salvaCuratore")
    public String salvaCuratore(@ModelAttribute Curatore curatore) {
        // Il curatore viene creato senza area
        curatore.setArea(null);  // Assicurati che non venga assegnata alcuna area

        curatoreService.save(curatore);  // Metodo che persiste il curatore
        return "admin/confermaSalvataggio";  // Conferma salvataggio
    }



    @GetMapping("/admin/listaCuratori")
    public String listaCuratori(Model model) {
        model.addAttribute("curatori", this.curatoreService.findAll());
        return "admin/listaCuratori";
    }

    @GetMapping("/admin/modificaCuratore/{id}")
    public String modificaCuratore(@PathVariable("id") Long id, Model model) {
        Curatore curatore = this.curatoreService.findById(id);
        if (curatore == null) {
            // Gestisci il caso in cui il curatore non esiste
            return "redirect:/admin/listaCuratori";
        }
        model.addAttribute("curatore", curatore);
        model.addAttribute("aree", this.areaService.findAll());
        return "admin/formModificaCuratore";
    }

    @PostMapping("/admin/salvaModificheCuratore/{id}")
    public String salvaModificheCuratore(@PathVariable("id") Long id, @ModelAttribute Curatore curatore, @RequestParam(value = "area", required = false) Long areaId, Model model) {
        
        Curatore curatoreEsistente = this.curatoreService.findById(id);
        
        curatoreEsistente.setNome(curatore.getNome());
        curatoreEsistente.setCognome(curatore.getCognome());
        curatoreEsistente.setDataNascita(curatore.getDataNascita());
        curatoreEsistente.setLuogoNascita(curatore.getLuogoNascita());
        
        if (areaId != null) {
            Area area = this.areaService.findById(areaId);
            curatoreEsistente.setArea(area);
        } else {
            curatoreEsistente.setArea(null);
        }

        this.curatoreService.save(curatoreEsistente);
        model.addAttribute("messaggio", "Curatore modificato con successo");
        
        return "admin/confermaSalvataggio";
    }

    
    @GetMapping("/admin/eliminaCuratore/{id}")
    public String eliminaCuratore(@PathVariable("id") Long id, Model model) {
        Curatore curatore = this.curatoreService.findById(id);
        if (curatore == null) {
            model.addAttribute("messaggioErrore", "Errore: curatore non trovato.");
            return "admin/listaCuratori";
        }

        // Dissocia l'area associata, se esiste
        if (curatore.getArea() != null) {
            curatore.getArea().setCuratore(null);
            this.areaService.save(curatore.getArea());
        }

        // Elimina il curatore
        this.curatoreService.deleteById(id);
        model.addAttribute("messaggioConferma", "Curatore eliminato con successo.");
        return "redirect:/admin/listaCuratori";
    }
    
    
    @GetMapping("/admin/listaAssegnaArea")
    public String listaAssegnaArea(Model model) {
    	
    	model.addAttribute("curatori", this.curatoreService.findAll());
        return "admin/listaAssegnaArea";
    }
    
    
    @GetMapping("/admin/assegnaAreaCuratore/{id}")
    public String assegnaAreaCuratore(@PathVariable("id") Long id, Model model) {
        Curatore curatore = this.curatoreService.findById(id);
        if (curatore == null) {
            model.addAttribute("messaggioErrore", "Curatore non trovato.");
            return "redirect:/admin/listaCuratori";
        }

        model.addAttribute("curatore", curatore);
        model.addAttribute("aree", this.areaService.findAll());
        return "admin/assegnaAreaCuratore";
    }

    @PostMapping("/admin/salvaAreaCuratore/{id}")
    public String salvaAssegnazioneArea(@PathVariable("id") Long id, @RequestParam("areaId") Long areaId, Model model) {
        Curatore curatore = this.curatoreService.findById(id);
        if (curatore == null) {
            model.addAttribute("messaggioErrore", "Curatore non trovato.");
            return "redirect:/admin/listaCuratori";
        }

        Area area = this.areaService.findById(areaId);
        if (area == null) {
            model.addAttribute("messaggioErrore", "Area non trovata.");
            return "redirect:/admin/listaCuratori";
        }

        // Assegna l'area al curatore
        curatore.setArea(area);
        this.curatoreService.save(curatore);

        model.addAttribute("messaggio", "Area assegnata con successo al curatore.");
        return "redirect:/admin/listaCuratori";
    }
    
}
