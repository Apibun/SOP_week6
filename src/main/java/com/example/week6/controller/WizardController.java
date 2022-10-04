package com.example.week6.controller;

import com.example.week6.pojo.Wizard;
import com.example.week6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @RequestMapping(value ="/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizards(){
        List<Wizard> wizards = wizardService.retriveWizards();
        return ResponseEntity.ok(wizards);
    }

    @RequestMapping(value ="/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> addWizards(@RequestBody Wizard wizard){
        return ResponseEntity.ok(wizardService.createWizard(wizard));
    }

    @RequestMapping(value ="/updateWizard", method = RequestMethod.POST)
    public ResponseEntity<?> updateWizards(@RequestBody Wizard wizard){
        return ResponseEntity.ok(wizardService.updateWizard(wizard));
    }

    @RequestMapping(value ="/deleteWizard", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWizards(@RequestBody Wizard wizard){
        return ResponseEntity.ok(wizardService.deleteWizard(wizard));
    }

}
