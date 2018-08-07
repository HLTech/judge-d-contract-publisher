package com.hltech.contracts.judged.publisher.expectations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class PactExpectationsReader implements ExpectationsReader {

    public static final String PACTS_LOCATION = "pactsLocation";

    private final String pactsLocation;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PactExpectationsReader(Map<String, String> configuration) throws PactExpectationsReadException {
        if (configuration.containsKey(PACTS_LOCATION)) {
            pactsLocation = configuration.get(PACTS_LOCATION);
        } else {
            throw new PactExpectationsReadException("Invalid configuration of PactExpectationsReader. Missing configuration property: " + PACTS_LOCATION);
        }
    }

    public List<Expectation> read() {
        List<Expectation> results = new ArrayList<>();
        File pactsLocationDir = new File(pactsLocation);
        if (pactsLocationDir.exists()) {
            File[] files = pactsLocationDir.listFiles();
            results.addAll(Arrays.stream(files)
                    .filter(Objects::nonNull)
                    .map(this::processFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        } else {
            throw new PactExpectationsReadException("Pacts location dir not found: " + pactsLocationDir);
        }
        return results;
    }

    private Optional<Expectation> processFile(File file) {
        if (file.isFile() && file.getName().endsWith(".json")) {
            try {
                return Optional.of(readExpectation(file));
            } catch (IOException ex) {
                log.error("Expectations for file {} cannot be read. {}", file.getName(), ex.getMessage());
            }
        }

        return Optional.empty();
    }

    private Expectation readExpectation(File file) throws IOException {
        return new Expectation(getProviderName(file), readContent(file));
    }

    private String getProviderName(File file) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(file);

        return jsonNode.get("provider").get("name").textValue();
    }

    private String readContent(File file) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(file);

        return objectMapper.writeValueAsString(jsonNode);
    }

}
