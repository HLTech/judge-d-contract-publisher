package com.hltech.contracts.judged.publisher.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationLoader {

    private final ObjectMapper objectMapper;

    public ConfigurationLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ContractPublisherConfig loadConfig(File configFile) throws IOException {
            return loadConfig(new FileInputStream(configFile));
    }

    public ContractPublisherConfig loadDefaultConfig() throws IOException {
        return loadConfig(ConfigurationLoader.class.getResourceAsStream("./default-config.yaml"));
    }

    private ContractPublisherConfig loadConfig(InputStream src) throws IOException {
        return objectMapper.readValue(src, ContractPublisherConfig.class);
    }

}
