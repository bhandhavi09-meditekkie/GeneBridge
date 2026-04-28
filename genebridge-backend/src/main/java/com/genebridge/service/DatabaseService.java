package com.genebridge.service;

import com.genebridge.client.EnsemblClient;
import com.genebridge.client.NCBIClient;
import com.genebridge.client.NonModelOrganismClient;
import com.genebridge.client.UniProtClient;
import com.genebridge.model.AnnotationResult;
import com.genebridge.model.GeneAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private NCBIClient ncbiClient;

    @Autowired
    private UniProtClient uniProtClient;

    @Autowired
    private EnsemblClient ensemblClient;

    @Autowired
    private NonModelOrganismClient nonModelOrganismClient;

    public AnnotationResult searchDatabases(GeneAnnotation input) {

        AnnotationResult ncbiResult =
                ncbiClient.searchByLocId(input.getLocId(), input.getOrganismName());

        if (ncbiResult != null) {

            if (missingLocation(ncbiResult)) {
                AnnotationResult locationResult =
                        ensemblClient.searchByLocId(input.getLocId(), input.getOrganismName());

                if (locationResult == null) {
                    locationResult =
                            nonModelOrganismClient.searchByOrganism(input.getLocId(), input.getOrganismName());
                }

                ncbiResult = mergeLocation(ncbiResult, locationResult);
            }

            return ncbiResult;
        }

        AnnotationResult uniProtResult =
                uniProtClient.searchByLocId(input.getLocId(), input.getOrganismName());

        if (uniProtResult != null) {
            return uniProtResult;
        }

        AnnotationResult ensemblResult =
                ensemblClient.searchByLocId(input.getLocId(), input.getOrganismName());

        if (ensemblResult != null) {
            return ensemblResult;
        }

        return nonModelOrganismClient.searchByOrganism(
                input.getLocId(),
                input.getOrganismName()
        );
    }

    private boolean missingLocation(AnnotationResult result) {
        return result.getStartPosition() == null
                || result.getEndPosition() == null
                || result.getStartPosition().equalsIgnoreCase("Not available")
                || result.getEndPosition().equalsIgnoreCase("Not available");
    }

    private AnnotationResult mergeLocation(
            AnnotationResult mainResult,
            AnnotationResult locationResult
    ) {
        if (locationResult == null) {
            return mainResult;
        }

        if (locationResult.getChromosome() != null) {
            mainResult.setChromosome(locationResult.getChromosome());
        }

        if (locationResult.getStartPosition() != null) {
            mainResult.setStartPosition(locationResult.getStartPosition());
        }

        if (locationResult.getEndPosition() != null) {
            mainResult.setEndPosition(locationResult.getEndPosition());
        }

        if (locationResult.getStrand() != null) {
            mainResult.setStrand(locationResult.getStrand());
        }

        if (locationResult.getGenomeBrowserUrl() != null) {
            mainResult.setGenomeBrowserUrl(locationResult.getGenomeBrowserUrl());
        }

        return mainResult;
    }
}