package com.genebridge.util;

import org.springframework.stereotype.Component;

@Component
public class SequenceAlignmentUtil {

    public double calculateSimilarity(String seq1, String seq2) {
        if (seq1 == null || seq2 == null || seq1.isEmpty() || seq2.isEmpty()) {
            return 0.0;
        }

        seq1 = seq1.toUpperCase().replaceAll("[^ACGTU]", "");
        seq2 = seq2.toUpperCase().replaceAll("[^ACGTU]", "");

        if (seq1.isEmpty() || seq2.isEmpty()) {
            return 0.0;
        }

        int matches = 0;
        int minLength = Math.min(seq1.length(), seq2.length());

        for (int i = 0; i < minLength; i++) {
            if (seq1.charAt(i) == seq2.charAt(i)) {
                matches++;
            }
        }

        double similarity = (double) matches / minLength;
        return similarity;
    }

    public boolean areSequencesCompatible(String seq1, String seq2) {
        boolean seq1IsDNA = isDNASequence(seq1);
        boolean seq2IsDNA = isDNASequence(seq2);
        return seq1IsDNA == seq2IsDNA;
    }

    private boolean isDNASequence(String seq) {
        return seq.toUpperCase().matches("[ACGTN]+");
    }
}