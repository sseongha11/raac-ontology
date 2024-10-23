package com.lboro.ontology.controller;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/ontology")
public class RAACOntologyController {
    @PostMapping("/addClass")
    public String create(@RequestParam String className, @RequestParam(required = false) String subclassName, Model model) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory dataFactory = manager.getOWLDataFactory();

            IRI ontologyIRI = IRI.create("http://www.example.org/raac");
            File file = new File("/Users/seongha/Documents/Codes/ontology/ontology/raacOntology.owl");
            OWLOntology raacOntology = manager.loadOntologyFromOntologyDocument(file);

            OWLClass newClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + className));
            OWLAxiom axiom = dataFactory.getOWLDeclarationAxiom(newClass);
            manager.addAxiom(raacOntology, axiom);

            if (subclassName != null && !subclassName.isEmpty()) {
                OWLClass subclass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + subclassName));
                OWLAxiom subclassAxiom = dataFactory.getOWLSubClassOfAxiom(subclass, newClass);
                manager.addAxiom(raacOntology, subclassAxiom);
            }

            manager.saveOntology(raacOntology, IRI.create(file.toURI()));

            Set<OWLClass> classes = raacOntology.getClassesInSignature();
            model.addAttribute("classes", classes);

            model.addAttribute("message", className + " added to RAAC Ontology successfully.");
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            model.addAttribute("message", "Error creating RAAC Ontology: " + e.getMessage());
        }

        return "redirect:/class";
    }

    @PostMapping("class/update")
    public String update(@RequestParam String oldClassName, @RequestParam String newClassName, Model model) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory dataFactory = manager.getOWLDataFactory();

            IRI ontologyIRI = IRI.create("http://www.example.org/raac");
            File file = new File("/Users/seongha/Documents/Codes/ontology/ontology/raacOntology.owl");
            OWLOntology raacOntology = manager.loadOntologyFromOntologyDocument(file);

            OWLClass oldClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + oldClassName));
            OWLClass newClass = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + newClassName));

            OWLEntityRenamer renamer = new OWLEntityRenamer(manager, Collections.singleton(raacOntology));
            List<OWLOntologyChange> changes = renamer.changeIRI(oldClass.getIRI(), newClass.getIRI());
            manager.applyChanges(changes);

            manager.saveOntology(raacOntology, IRI.create(file.toURI()));

            Set<OWLClass> classes = raacOntology.getClassesInSignature();
            model.addAttribute("classes", classes);

            model.addAttribute("message", oldClassName + " updated to " + newClassName + " in RAAC Ontology successfully.");
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            model.addAttribute("message", "Error updating RAAC Ontology: " + e.getMessage());
        }

        return "redirect:/class";
    }

    @PostMapping("class/delete")
    public String delete(@RequestParam String className, Model model) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory dataFactory = manager.getOWLDataFactory();

            IRI ontologyIRI = IRI.create("http://www.example.org/raac");
            File file = new File("/Users/seongha/Documents/Codes/ontology/ontology/raacOntology.owl");
            OWLOntology raacOntology = manager.loadOntologyFromOntologyDocument(file);

            OWLClass classToDelete = dataFactory.getOWLClass(IRI.create(ontologyIRI + "#" + className));

            OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(raacOntology));
            classToDelete.accept(remover);
            manager.applyChanges(remover.getChanges());

            manager.saveOntology(raacOntology, IRI.create(file.toURI()));

            Set<OWLClass> classes = raacOntology.getClassesInSignature();
            model.addAttribute("classes", classes);

            model.addAttribute("message", className + " deleted from RAAC Ontology successfully.");
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            model.addAttribute("message", "Error deleting from RAAC Ontology: " + e.getMessage());
        }

        return "redirect:/class";
    }
}