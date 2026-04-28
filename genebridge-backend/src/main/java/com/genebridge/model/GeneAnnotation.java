package com.genebridge.model;

public class GeneAnnotation {

    private String locId;
    private String organismName;
    private String geneSymbol;
    private String fastaSequence;

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getOrganismName() {
        return organismName;
    }

    public void setOrganismName(String organismName) {
        this.organismName = organismName;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getFastaSequence() {
        return fastaSequence;
    }

    public void setFastaSequence(String fastaSequence) {
        this.fastaSequence = fastaSequence;
    }
}