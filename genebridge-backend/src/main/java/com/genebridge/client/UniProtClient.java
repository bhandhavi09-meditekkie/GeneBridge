package com.genebridge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genebridge.model.AnnotationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
public class UniProtClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public AnnotationResult searchByLocId(String locId, String organismName) {
        try {
            String query = locId + " AND organism_name:\"" + organismName + "\"";
            String encodedQuery = UriUtils.encodeQueryParam(query, StandardCharsets.UTF_8);

            String url =
                    "https://rest.uniprot.org/uniprotkb/search?query="
                            + encodedQuery
                            + "&format=json&size=1";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode json = mapper.readTree(response);

            JsonNode results = json.path("results");

            if (!results.isArray() || results.size() == 0) {
                return null;
            }

            JsonNode first = results.get(0);

            String proteinName = first.path("proteinDescription")
                    .path("recommendedName")
                    .path("fullName")
                    .path("value")
                    .asText("Protein annotation available");

            AnnotationResult result = new AnnotationResult();
            result.setLocId(locId);
            result.setGeneName(locId);
            result.setGeneFunction(proteinName);
            result.setSourceDatabase("UniProt");
            result.setOrganism(organismName);
            result.setAnnotationMethod("UniProt LOC Search");
            result.setConfidenceScore(0.80);

            return result;

        } catch (Exception e) {
            return null;
        }
    }
}