package com.hltech.contracts.judged.publisher.expectations.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.contracts.judged.publisher.CommunicationInterface;
import com.hltech.contracts.judged.publisher.ContractReadException;
import com.hltech.contracts.judged.publisher.expectations.Expectation;
import com.hltech.contracts.judged.publisher.expectations.ExpectationsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CommunicationInterface("rest")
public class PactExpectationsReader implements ExpectationsReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PactExpectationsReader.class);

    public static final String PACTS_LOCATION_PARAMETER = "pactsLocation";
    public static final String DEFAULT_PACTS_LOCATION = "./target/pacts";

    public final ObjectMapper objectMapper = new ObjectMapper();

    public List<Expectation> read(Properties configuration) {
        String pactsLocation = DEFAULT_PACTS_LOCATION;
        if (configuration.containsKey(PACTS_LOCATION_PARAMETER)) {
            pactsLocation = configuration.getProperty(PACTS_LOCATION_PARAMETER);
            LOGGER.info("using: '" + pactsLocation + "' as path to swagger.json ");
        } else {
            LOGGER.info(PACTS_LOCATION_PARAMETER + " parameters has not been provided. Using default pacts location: " + DEFAULT_PACTS_LOCATION);
        }

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
            throw new ContractReadException("Pacts location dir not found: " + pactsLocationDir);
        }
        return results;
    }

    private Optional<Expectation> processFile(File file) {
        if (file.isFile() && file.getName().endsWith(".json")) {
            try {
                return Optional.of(readExpectation(file));
            } catch (IOException ex) {
                LOGGER.error("Expectations for file {} cannot be read. {}", file.getName(), ex.getMessage());
            }
        }

        return Optional.empty();
    }

    private Expectation readExpectation(File file) throws IOException {
        Pact pact = objectMapper.readValue(file, Pact.class);
        return new Expectation(pact.getProvider().getName(), objectMapper.writeValueAsString(pact));
    }
}
