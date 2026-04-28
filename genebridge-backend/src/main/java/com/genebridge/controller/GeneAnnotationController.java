package com.genebridge.controller;

import com.genebridge.model.AnnotationResult;
import com.genebridge.model.GeneAnnotation;
import com.genebridge.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GeneAnnotationController {

    @Autowired
    private AnnotationService annotationService;

    @GetMapping("/health")
    public String health() {
        return "GeneBridge backend is running";
    }

    @PostMapping("/annotate")
    public AnnotationResult annotateGene(@RequestBody GeneAnnotation input) {
        return annotationService.annotateGene(input);
    }
}
