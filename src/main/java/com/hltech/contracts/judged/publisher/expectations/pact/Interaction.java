package com.hltech.contracts.judged.publisher.expectations.pact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class Interaction {

    @JsonProperty("provider_state")
    private String providerState;
    private String description;
    private JsonNode request;
    private JsonNode response;

    @JsonCreator
    public Interaction(@JsonProperty("provider_state") String providerState,
                       @JsonProperty("description") String description,
                       @JsonProperty("request") JsonNode request,
                       @JsonProperty("response") JsonNode response) {
        this.providerState = providerState;
        this.description = description;
        this.request = request;
        this.response = response;
    }
}
