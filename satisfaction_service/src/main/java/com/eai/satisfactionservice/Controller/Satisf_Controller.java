package com.eai.satisfactionservice.Controller;

import com.eai.satisfactionservice.model.Satisfaction;
import com.eai.satisfactionservice.repository.Satisf_Rep;
import com.eai.satisfactionservice.service.SatisfactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api")
public class Satisf_Controller {

    @Autowired
    private Satisf_Rep repo;
    @Autowired
    private SatisfactionService satisfactionService;

    @PostMapping("/submit")
    public String submitSatisfaction(@RequestParam String email,
                                     @RequestParam String nom,
                                     @RequestParam String prenom,
                                     @RequestParam Integer id_client,
                                     @RequestParam String avis) {
        Satisfaction satisfaction = new Satisfaction();
        satisfaction.setEmail(email);
        satisfaction.setNom(nom);
        satisfaction.setPrenom(prenom);
        satisfaction.setId_client(id_client);
        satisfaction.setAvis_client(avis);
        repo.save(satisfaction);
        return "confirmation";
    }

    @GetMapping("/form")
    public String showSatisfactionForm(@RequestParam String email,
                                       @RequestParam String nom,
                                       @RequestParam String prenom,
                                       Model model) {
        model.addAttribute("email", email);
        model.addAttribute("nom", nom);
        model.addAttribute("prenom", prenom);
        return "index";
    }
    @GetMapping("/send-satisfaction-forms")
    @ResponseBody
    public String sendSatisfactionForms() {
        satisfactionService.sendSatisfactionForms();
        return "Formulaires de satisfaction envoyés avec succès";
    }
}
