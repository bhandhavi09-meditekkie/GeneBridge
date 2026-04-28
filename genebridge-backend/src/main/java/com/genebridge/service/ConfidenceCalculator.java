package com.genebridge.service;

import com.genebridge.model.AnnotationResult;
import org.springframework.stereotype.Service;

@Service
public class ConfidenceCalculator {

    public AnnotationResult calculateConfidence(AnnotationResult result) {

        double score = result.getConfidenceScore() == null ? 0.50 : result.getConfidenceScore();

        if (score >= 0.85) {
            result.setConfidenceLevel("HIGH");
        } else if (score >= 0.65) {
            result.setConfidenceLevel("MEDIUM");
        } else {
            result.setConfidenceLevel("LOW");
        }

        return result;
    }
}