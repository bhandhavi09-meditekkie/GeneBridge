package com.genebridge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genebridge.model.AnnotationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NonModelOrganismClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public AnnotationResult searchByOrganism(String locId, String organismName) {

        AnnotationResult result;

        result = searchEnsemblGenomes(locId, organismName);
        if (result != null) return result;

        result = searchWormBase(locId, organismName);
        if (result != null) return result;

        result = searchFlyBase(locId, organismName);
        if (result != null) return result;

        return fallbackByOrganism(locId, organismName);
    }

    private AnnotationResult searchEnsemblGenomes(String locId, String organismName) {
        try {
            String species = organismName.toLowerCase().replace(" ", "_");

            String url =
                    "https://rest.ensembl.org/lookup/symbol/"
                            + species
                            + "/"
                            + locId
                            + "?content-type=application/json";

            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.trim().isEmpty()) {
                return null;
            }

            JsonNode json = mapper.readTree(response);

            if (json.path("id").asText("").isEmpty()) {
                return null;
            }

            AnnotationResult result = new AnnotationResult();

            result.setLocId(locId);
            result.setGeneName(json.path("display_name").asText(locId));
            result.setGeneFunction(json.path("description").asText("Function not available from Ensembl"));
            result.setSourceDatabase("Ensembl / Ensembl Genomes REST");
            result.setSourceUrl("https://rest.ensembl.org/lookup/symbol/" + species + "/" + locId);
            result.setOrganism(organismName);
            result.setAnnotationMethod("Ensembl Genomes REST API lookup");
            result.setConfidenceScore(0.82);

            result.setChromosome(json.path("seq_region_name").asText("Unknown"));

            if (json.has("start") && json.has("end")) {
                result.setStartPosition(json.path("start").asText());
                result.setEndPosition(json.path("end").asText());

                int strandValue = json.path("strand").asInt(0);
                if (strandValue == 1) {
                    result.setStrand("+");
                } else if (strandValue == -1) {
                    result.setStrand("-");
                } else {
                    result.setStrand("Unknown");
                }
            } else {
                result.setStartPosition("Not available");
                result.setEndPosition("Not available");
                result.setStrand("Unknown");
            }

            result.setGenomeBrowserUrl(getEnsemblBrowserUrl(organismName, locId));

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    private AnnotationResult searchWormBase(String locId, String organismName) {
        try {
            String org = organismName.toLowerCase();

            if (!org.contains("caenorhabditis") && !org.contains("nematode")) {
                return null;
            }

            String url =
                    "https://rest.wormbase.org/rest/widget/gene/"
                            + locId
                            + "/overview";

            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.trim().isEmpty()) {
                return null;
            }

            JsonNode json = mapper.readTree(response);

            if (json.path("error").asBoolean(false)) {
                return null;
            }

            AnnotationResult result = new AnnotationResult();

            result.setLocId(locId);
            result.setGeneName(locId);
            result.setGeneFunction("WormBase gene overview available");
            result.setSourceDatabase("WormBase REST API");
            result.setSourceUrl("https://wormbase.org/species/all/gene/" + locId);
            result.setOrganism(organismName);
            result.setAnnotationMethod("WormBase REST lookup");
            result.setConfidenceScore(0.78);

            result.setChromosome("See WormBase record");
            result.setStartPosition("Not available");
            result.setEndPosition("Not available");
            result.setStrand("Unknown");
            result.setGenomeBrowserUrl("https://wormbase.org/species/all/gene/" + locId);

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    private AnnotationResult searchFlyBase(String locId, String organismName) {
        try {
            String org = organismName.toLowerCase();

            if (!org.contains("drosophila")) {
                return null;
            }

            String url =
                    "https://api.flybase.org/api/v1.0/gene/summaries/auto/"
                            + locId;

            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.trim().isEmpty()) {
                return null;
            }

            AnnotationResult result = new AnnotationResult();

            result.setLocId(locId);
            result.setGeneName(locId);
            result.setGeneFunction("FlyBase gene summary available");
            result.setSourceDatabase("FlyBase API");
            result.setSourceUrl("https://flybase.org/reports/" + locId);
            result.setOrganism(organismName);
            result.setAnnotationMethod("FlyBase gene summary API lookup");
            result.setConfidenceScore(0.78);

            result.setChromosome("See FlyBase record");
            result.setStartPosition("Not available");
            result.setEndPosition("Not available");
            result.setStrand("Unknown");
            result.setGenomeBrowserUrl("https://flybase.org/reports/" + locId);

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    private AnnotationResult fallbackByOrganism(String locId, String organismName) {

        String org = organismName.toLowerCase();

        if (org.contains("aphis") || org.contains("acyrthosiphon") || org.contains("aphid")) {
            return aphidFallback(locId, organismName);
        }

        if (org.contains("bombyx") || org.contains("aedes") || org.contains("tribolium") || org.contains("insect")) {
            return insectFallback(locId, organismName);
        }

        if (org.contains("arabidopsis") || org.contains("oryza") || org.contains("triticum") || org.contains("plant")) {
            return plantFallback(locId, organismName);
        }

        if (org.contains("caenorhabditis") || org.contains("nematode")) {
            return nematodeFallback(locId, organismName);
        }

        if (org.contains("fish") || org.contains("shrimp") || org.contains("crab") || org.contains("marine") || org.contains("aquaculture")) {
            return aquacultureFallback(locId, organismName);
        }

        return genericNonModelFallback(locId, organismName);
    }

    private AnnotationResult aphidFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Putative aphid gene candidate");
        result.setGeneFunction("Aphid-specific annotation candidate; verify using AphidBase/BIPAA, NCBI, or sequence homology");
        result.setSourceDatabase("AphidBase / BIPAA fallback");
        result.setSourceUrl("https://bipaa.genouest.org/is/aphidbase/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("Aphid organism-specific rescue");
        result.setConfidenceScore(0.60);

        result.setChromosome("Aphid scaffold not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://bipaa.genouest.org/is/aphidbase/");

        return result;
    }

    private AnnotationResult insectFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Putative insect gene candidate");
        result.setGeneFunction("Predicted insect gene annotation; verify with Ensembl Metazoa, FlyBase, or species-specific insect resources");
        result.setSourceDatabase("Insect non-model fallback");
        result.setSourceUrl("https://metazoa.ensembl.org/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("Insect organism-specific rescue");
        result.setConfidenceScore(0.60);

        result.setChromosome("Insect scaffold not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://metazoa.ensembl.org/");

        return result;
    }

    private AnnotationResult plantFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Putative plant gene candidate");
        result.setGeneFunction("Predicted plant annotation; verify with Ensembl Plants or species-specific plant databases");
        result.setSourceDatabase("Ensembl Plants fallback");
        result.setSourceUrl("https://plants.ensembl.org/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("Plant organism-specific rescue");
        result.setConfidenceScore(0.60);

        result.setChromosome("Plant scaffold/chromosome not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://plants.ensembl.org/");

        return result;
    }

    private AnnotationResult nematodeFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Putative nematode gene candidate");
        result.setGeneFunction("Predicted nematode annotation; verify with WormBase or WormBase ParaSite");
        result.setSourceDatabase("WormBase / WormBase ParaSite fallback");
        result.setSourceUrl("https://parasite.wormbase.org/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("Nematode organism-specific rescue");
        result.setConfidenceScore(0.60);

        result.setChromosome("Nematode scaffold not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://parasite.wormbase.org/");

        return result;
    }

    private AnnotationResult aquacultureFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Putative aquaculture/marine gene candidate");
        result.setGeneFunction("Predicted marine organism annotation; verify with NCBI, Ensembl, or species-specific aquaculture resources");
        result.setSourceDatabase("Aquaculture / Marine organism fallback");
        result.setSourceUrl("https://www.ncbi.nlm.nih.gov/gene/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("Marine organism-specific rescue");
        result.setConfidenceScore(0.55);

        result.setChromosome("Marine scaffold not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://www.ncbi.nlm.nih.gov/gene/");

        return result;
    }

    private AnnotationResult genericNonModelFallback(String locId, String organismName) {
        AnnotationResult result = new AnnotationResult();

        result.setLocId(locId);
        result.setGeneName("Unresolved non-model organism LOC candidate");
        result.setGeneFunction("No direct annotation found; sequence-based homology analysis is recommended");
        result.setSourceDatabase("GeneBridge non-model fallback");
        result.setSourceUrl("https://www.ncbi.nlm.nih.gov/gene/");
        result.setOrganism(organismName);
        result.setAnnotationMethod("General non-model rescue");
        result.setConfidenceScore(0.45);

        result.setChromosome("Not resolved");
        result.setStartPosition("Not available");
        result.setEndPosition("Not available");
        result.setStrand("Unknown");
        result.setGenomeBrowserUrl("https://www.ncbi.nlm.nih.gov/gene/");

        return result;
    }

    private String getEnsemblBrowserUrl(String organismName, String locId) {

        String org = organismName.toLowerCase();

        if (org.contains("arabidopsis") || org.contains("oryza") || org.contains("triticum")) {
            return "https://plants.ensembl.org/Multi/Search/Results?q=" + locId;
        }

        if (org.contains("fungi") || org.contains("saccharomyces") || org.contains("aspergillus")) {
            return "https://fungi.ensembl.org/Multi/Search/Results?q=" + locId;
        }

        if (org.contains("protist") || org.contains("plasmodium")) {
            return "https://protists.ensembl.org/Multi/Search/Results?q=" + locId;
        }

        return "https://metazoa.ensembl.org/Multi/Search/Results?q=" + locId;
    }
}