package com.hltech.contracts.judged.publisher.expectations;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface ExpectationsReader {

    Map<String, JsonNode> read();

}
