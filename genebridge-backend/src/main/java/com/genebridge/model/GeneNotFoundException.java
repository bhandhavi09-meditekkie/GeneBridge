package com.genebridge.exception;

/**
 * Exception thrown when a gene is not found in any database
 */
public class GeneNotFoundException extends AnnotationException {
    
    public GeneNotFoundException(String locId, String organism) {
        super(String.format("Gene not found: LOC ID '%s' in organism '%s'", locId, organism));
    }
    
    public GeneNotFoundException(String message) {
        super(message);
    }
}