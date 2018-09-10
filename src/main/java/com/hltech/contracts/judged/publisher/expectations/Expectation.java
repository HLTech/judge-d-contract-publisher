package com.hltech.contracts.judged.publisher.expectations;

public class Expectation {

    private String providerName;
    private String value;

    public Expectation(String providerName, String value) {
        this.providerName = providerName;
        this.value = value;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getValue() {
        return value;
    }
}
