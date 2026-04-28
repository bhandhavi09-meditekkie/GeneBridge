package com.genebridge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single match from a database query
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseMatch {
    
    /** Database name (e.g., NCBI Gene, UniProt, FlyBase) */
    private String database;
    
    /** Matched ID in that database */
    private String matchedId;
    
    /** Matched gene/protein name */
    private String matchedName;
    
    /** Match score (0.0 - 1.0) */
    private Double matchScore;
    
    /** URL to the database entry */
    private String url;
    
    /**
     * Get a summary of this match
     */
    @Override
    public String toString() {
        return String.format("%s: %s (%.2f)", database, matchedName, matchScore);
    }
}