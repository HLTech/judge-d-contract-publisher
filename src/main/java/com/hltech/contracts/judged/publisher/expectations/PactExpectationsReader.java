package com.hltech.contracts.judged.publisher.expectations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PactExpectationsReader implements ExpectationsReader {

    public static final String PACTS_LOCATION = "pactsLocation";

    private final String pactsLocation;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PactExpectationsReader(Map<String, String> configuration) {
        if (configuration.containsKey(PACTS_LOCATION)) {
            pactsLocation = configuration.get(PACTS_LOCATION);
        } else {
            throw new IllegalStateException("Invalid configuration of PactExpectationsReader. Missing configuration property: " + PACTS_LOCATION);
        }
    }

    public List<Expectation> read() {
        List<Expectation> results = new ArrayList<>();
        try {
            File pactsLocationDir = new File(pactsLocation);
            if (pactsLocationDir.exists()) {
                File[] files = pactsLocationDir.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".json")) {
                        results.add(new Expectation(getProviderName(file), readContent(file)));
                    }
                }
            } else {
                throw new IllegalStateException("Pacts location dir not found: " + pactsLocationDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private String readContent(File file) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(file);

        return objectMapper.writeValueAsString(jsonNode);
    }

    private String getProviderName(File file) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(file);

        return jsonNode.get("provider").get("name").toString();
    }

}
