package com.genebridge.service;

import com.genebridge.exception.AnnotationException;
import com.genebridge.model.AnnotationResult;
import com.genebridge.model.GeneAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ConfidenceCalculator confidenceCalculator;

    public AnnotationResult annotateGene(GeneAnnotation input) {

        validateInput(input);

        AnnotationResult result = databaseService.searchDatabases(input);

        if (result == null) {
            throw new AnnotationException("No annotation found for LOC ID: " + input.getLocId());
        }

        return confidenceCalculator.calculateConfidence(result);
    }

    private void validateInput(GeneAnnotation input) {

        if (input == null) {
            throw new AnnotationException("Input cannot be empty");
        }

        if (input.getLocId() == null || input.getLocId().trim().isEmpty()) {
            throw new AnnotationException("LOC ID is required");
        }

        if (input.getOrganismName() == null || input.getOrganismName().trim().isEmpty()) {
            throw new AnnotationException("Organism name is required");
        }
    }
}