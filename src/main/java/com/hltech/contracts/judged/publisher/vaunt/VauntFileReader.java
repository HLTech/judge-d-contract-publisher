package com.hltech.contracts.judged.publisher.vaunt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.hltech.contracts.judged.publisher.ContractReadException;
import com.hltech.vaunt.core.domain.model.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class VauntFileReader {

    private static final String VAUNT_FILE_LOCATION_CONFIG_KEY = "vauntLocation";
    private static final String DEFAULT_VAUNT_FILE_LOCATION = "./target/classes/static/vaunt";
    private static final String JSON_FILE_SUFFIX = ".json";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());

    public List<Service> readVauntFiles(Properties properties) {
        String vauntLocation = DEFAULT_VAUNT_FILE_LOCATION;
        if (properties.containsKey(VAUNT_FILE_LOCATION_CONFIG_KEY)) {
            vauntLocation = properties.getProperty(VAUNT_FILE_LOCATION_CONFIG_KEY);
            log.info("Using: '{}' as path to vaunt file location.", vauntLocation);
        } else {
            log.info("Using default vaunt file location: '{}'", vauntLocation);
        }

        return readFromLocation(vauntLocation);
    }

    public String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new ContractReadException("Error when trying to serialize service capabilities.", ex);
        }
    }

    private List<Service> readFromLocation(String vauntLocation) {
        File[] files = Paths.get(vauntLocation).toFile().listFiles();
        if (files != null && files.length > 0) {
            log.info("Found {} files with vaunt specification", files.length);
            return processFiles(files);
        } else {
            throw new ContractReadException("Vaunt file not found in location: " + vauntLocation);
        }
    }

    private List<Service> processFiles(File[] files) {
        return Stream.of(files)
            .filter(this::jsonFile)
            .map(this::readFrom)
            .collect(Collectors.toList());
    }

    private boolean jsonFile(File file) {
        return file.getName().endsWith(JSON_FILE_SUFFIX);
    }

    private Service readFrom(File file) {
        try {
            return objectMapper.readValue(file, Service.class);
        } catch (IOException ex) {
            throw new ContractReadException("Cannot read vaunt file: " + file.getAbsolutePath(), ex);
        }
    }
}
