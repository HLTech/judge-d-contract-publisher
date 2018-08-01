package com.hltech.contracts.judged.publisher.expectations;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class PactExpectationsReader implements ExpectationsReader {

    public PactExpectationsReader(Map<String, String> configuration) {
    }

    public Map<String, JsonNode> read() {
        return new HashMap<String, JsonNode>();
    }
}
