package com.genebridge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metrics used to calculate annotation confidence level
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfidenceMetrics {
    
    /** Number of matches from general databases */
    private int generalDatabaseMatches;
    
    /** Number of matches from organism-specific databases */
    private int organismSpecificMatches;
    
    /** Homology/sequence similarity score */
    private double homologyScore;
    
    /** Whether gene found in multiple databases */
    private boolean foundInMultipleSources;
    
    /** Primary database that returned the result */
    private String primarySource;
    
    /** Total number of databases queried */
    private int totalSourcesQueried;
    
    /** Number of successful database queries */
    private int successfulSources;
    
    /** Average match score across all matches */
    private double averageMatchScore;
}