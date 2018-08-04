package com.hltech.contracts.judged.publisher.config;

import java.util.Map;

public class ContractPublisherConfig {

    private Map<String, ReaderConfig> capabilities;
    private Map<String, ReaderConfig> expectations;

    public Map<String, ReaderConfig> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, ReaderConfig> capabilities) {
        this.capabilities = capabilities;
    }

    public Map<String, ReaderConfig> getExpectations() {
        return expectations;
    }

    public void setExpectations(Map<String, ReaderConfig> expectations) {
        this.expectations = expectations;
    }

    @Override
    public String toString() {
        return "ContractPublisherConfig{" +
                "capabilities=" + capabilities +
                ", expectations=" + expectations +
                '}';
    }
}
