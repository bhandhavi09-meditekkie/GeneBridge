package com.genebridge.model;

public class AnnotationResult {

    private String locId;
    private String geneName;
    private String geneFunction;
    private String sourceDatabase;
    private String sourceUrl;
    private String confidenceLevel;
    private Double confidenceScore;
    private String organism;
    private String annotationMethod;

    private String chromosome;
    private String startPosition;
    private String endPosition;
    private String strand;
    private String genomeBrowserUrl;

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getGeneFunction() {
        return geneFunction;
    }

    public void setGeneFunction(String geneFunction) {
        this.geneFunction = geneFunction;
    }

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getAnnotationMethod() {
        return annotationMethod;
    }

    public void setAnnotationMethod(String annotationMethod) {
        this.annotationMethod = annotationMethod;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String getGenomeBrowserUrl() {
        return genomeBrowserUrl;
    }

    public void setGenomeBrowserUrl(String genomeBrowserUrl) {
        this.genomeBrowserUrl = genomeBrowserUrl;
    }
}