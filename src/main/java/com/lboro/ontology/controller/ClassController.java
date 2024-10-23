package com.lboro.ontology.controller;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.Set;

@Controller
public class ClassController {
    @GetMapping("/class")
    public String classPage(Model model) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            IRI ontologyIRI = IRI.create("http://www.example.org/raac");
            OWLOntology raacOntology = manager.loadOntologyFromOntologyDocument(new File("/Users/seongha/Documents/Codes/ontology/ontology/raacOntology.owl"));

            Set<OWLClass> classes = raacOntology.getClassesInSignature();
            model.addAttribute("classes", classes);
        } catch (OWLOntologyCreationException e) {
            model.addAttribute("message", "Error loading RAAC Ontology: " + e.getMessage());
        }

        return "class";
    }
}
