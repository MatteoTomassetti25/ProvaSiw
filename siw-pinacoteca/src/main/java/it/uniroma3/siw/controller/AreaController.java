package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Area;
import it.uniroma3.siw.model.Curatore;
import it.uniroma3.siw.service.AreaService;
import it.uniroma3.siw.service.CuratoreService;

@Controller
public class AreaController {

    @Autowired
    protected AreaService areaService;

    @Autowired
    protected CuratoreService curatoreService;

    @GetMapping("/admin/operazioniAree")
    public String operazioniAree() {
        return "admin/operazioniAree";
    }

    @GetMapping("/admin/inserimentoArea")
    public String inserimentoArea(Model model) {
        model.addAttribute("area", new Area());
        model.addAttribute("curatori", this.curatoreService.findAll());
        return "admin/formNewArea";
    }

    @PostMapping("/admin/salvaArea")
    public String salvaArea(@RequestParam("nome") String nome, 
                            @RequestParam(value = "curatore", required = false) Long curatoreId, 
                            Model model) {
        
        Area nuovaArea = new Area();
        nuovaArea.setNome(nome);

        if (curatoreId != null) {
            Curatore curatore = curatoreService.findById(curatoreId);
            nuovaArea.setCuratore(curatore);
        } else {
            nuovaArea.setCuratore(null);
        }

        areaService.save(nuovaArea);
        model.addAttribute("messaggioConferma", "Area creata con successo!");

        return "redirect:/admin/operazioniAree";
    }


    @GetMapping("/admin/listaAree")
    public String listaAree(Model model) {
        model.addAttribute("aree", this.areaService.findAll());
        return "admin/listaAree";
    }

    @GetMapping("/admin/modificaArea/{id}")
    public String modificaArea(Model model, @PathVariable("id") Long id) {
        Area area = this.areaService.findById(id);
        model.addAttribute("area", area);
        model.addAttribute("curatori", this.curatoreService.findAll());
        return "admin/formModificaArea";
    }

    @PostMapping("/admin/salvaModifiche/{id}")
    public String salvaModificheArea(@PathVariable("id") Long id, 
                                     @RequestParam("nome") String nome, 
                                     @RequestParam(value = "curatore", required = false) Long curatoreId, 
                                     Model model) {
        
        Area areaEsistente = this.areaService.findById(id);
        areaEsistente.setNome(nome);

        if (curatoreId != null) {
            Curatore curatore = this.curatoreService.findById(curatoreId);
            areaEsistente.setCuratore(curatore);
        } else {
            areaEsistente.setCuratore(null);
        }

        this.areaService.save(areaEsistente);
        model.addAttribute("messaggioConferma", "Modifiche salvate con successo!");

        return "redirect:/admin/listaAree";
    }


    @GetMapping("/admin/eliminaArea/{id}")
    public String eliminaArea(@PathVariable("id") Long id, Model model) {
        try {
            Area area = this.areaService.findById(id);

            if (area == null) {
                model.addAttribute("messaggioErrore", "Errore: area non trovata.");
                return "admin/listaAree";
            }

            // Dissocia il curatore associato, se presente
            Curatore curatore = area.getCuratore();
            if (curatore != null) {
                curatore.setArea(null); // Dissocia il curatore dall'area
                this.curatoreService.save(curatore); // Salva le modifiche al curatore
            }

            // Elimina l'area
            this.areaService.deleteById(id);
            model.addAttribute("messaggioConferma", "Area eliminata con successo.");
        } catch (Exception e) {
            // Gestisci eventuali errori durante l'eliminazione
            model.addAttribute("messaggioErrore", "Errore durante l'eliminazione dell'area: " + e.getMessage());
        }

        // Ritorna alla lista delle aree aggiornata
        model.addAttribute("aree", this.areaService.findAll());
        return "admin/listaAree";
    }
}
