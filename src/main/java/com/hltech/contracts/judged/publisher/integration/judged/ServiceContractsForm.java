package com.hltech.contracts.judged.publisher.integration.judged;


import java.util.Map;

public class ServiceContractsForm {

    private Map<String, String> capabilities;
    private Map<String, Map<String, String>> expectations;

    public Map<String, String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, String> capabilities) {
        this.capabilities = capabilities;
    }

    public Map<String, Map<String, String>> getExpectations() {
        return expectations;
    }

    public void setExpectations(Map<String, Map<String, String>> expectations) {
        this.expectations = expectations;

    }

    @Override
    public String toString() {
        return "ServiceContractsForm{" +
                "capabilities=" + capabilities +
                ", expectations=" + expectations +
                '}';
    }
}
