package com.genebridge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genebridge.model.AnnotationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NCBIClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public AnnotationResult searchByLocId(String locId, String organismName) {

        try {
            String searchUrl =
                    "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi"
                            + "?db=gene&term="
                            + locId.replace(" ", "+")
                            + "+AND+"
                            + organismName.replace(" ", "+")
                            + "[Organism]&retmode=json";

            String searchResponse = restTemplate.getForObject(searchUrl, String.class);

            JsonNode searchJson = mapper.readTree(searchResponse);
            JsonNode ids = searchJson.path("esearchresult").path("idlist");

            if (!ids.isArray() || ids.size() == 0) {
                return null;
            }

            String geneId = ids.get(0).asText();

            String summaryUrl =
                    "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi"
                            + "?db=gene&id="
                            + geneId
                            + "&retmode=json";

            String summaryResponse = restTemplate.getForObject(summaryUrl, String.class);

            JsonNode root = mapper.readTree(summaryResponse);
            JsonNode gene = root.path("result").path(geneId);

            AnnotationResult result = new AnnotationResult();

            result.setLocId(locId);
            result.setGeneName(gene.path("name").asText(locId));
            result.setGeneFunction(gene.path("description").asText("Function not available"));
            result.setSourceDatabase("NCBI Gene");
            result.setSourceUrl("https://www.ncbi.nlm.nih.gov/gene/" + geneId);
            result.setOrganism(organismName);
            result.setAnnotationMethod("NCBI LOC ID Search");
            result.setConfidenceScore(0.95);

            result.setChromosome(gene.path("chromosome").asText("Unknown"));

            JsonNode genomicInfo = gene.path("genomicinfo");

            if (genomicInfo.isArray() && genomicInfo.size() > 0) {
                JsonNode location = genomicInfo.get(0);

                long chrStart = location.path("chrstart").asLong(-1);
                long chrStop = location.path("chrstop").asLong(-1);

                if (chrStart >= 0 && chrStop >= 0) {
                    long start = Math.min(chrStart, chrStop) + 1;
                    long end = Math.max(chrStart, chrStop) + 1;

                    result.setStartPosition(String.valueOf(start));
                    result.setEndPosition(String.valueOf(end));

                    if (chrStart <= chrStop) {
                        result.setStrand("+");
                    } else {
                        result.setStrand("-");
                    }

                    if (organismName.equalsIgnoreCase("Homo sapiens")
                            && !result.getChromosome().equalsIgnoreCase("Unknown")) {

                        result.setGenomeBrowserUrl(
                                "https://genome.ucsc.edu/cgi-bin/hgTracks?db=hg38&position=chr"
                                        + result.getChromosome()
                                        + "%3A"
                                        + start
                                        + "-"
                                        + end
                        );

                    } else {
                        result.setGenomeBrowserUrl(result.getSourceUrl());
                    }
                }
            }

            return result;

        } catch (Exception e) {
            return null;
        }
    }
}