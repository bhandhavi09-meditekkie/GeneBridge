package com.genebridge.exception;

public class AnnotationException extends RuntimeException {

    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable cause) {
        super(message, cause);
    }
}