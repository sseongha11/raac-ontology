package com.lboro.ontology.controller;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

@Controller
public class RAACOntologyController {

    @PostMapping("/create")
    public String create(@RequestParam String className, Model model) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory dataFactory = manager.getOWLDataFactory();

            IRI ontologyIRI = IRI.create("http://www.example.org/raac");
            OWLOntology raacOntology = manager.createOntology(ontologyIRI);

            OWLClass newClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + className));
            OWLClass buildingMaterial = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#Building_Material"));

            OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(newClass, buildingMaterial);
            manager.addAxiom(raacOntology, axiom);

            File file = new File("/Users/seongha/Documents/ontology/ontology/raacOntology.owl");
            manager.saveOntology(raacOntology, IRI.create(file.toURI()));

            model.addAttribute("message", className + " added to RAAC Ontology successfully.");
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            model.addAttribute("message", "Error creating RAAC Ontology: " + e.getMessage());
        }

        return "result";
    }
}
