package com.hltech.contracts.judged.publisher.expectations.pact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class Pact {

    @JsonCreator
    public Pact(@JsonProperty("provider") Service provider,
                @JsonProperty("consumer") Service consumer,
                @JsonProperty("interactions") List<Interaction> interactions,
                @JsonProperty("metadata") JsonNode metadata) {
        if (provider == null) throw new IllegalArgumentException();
        if (consumer == null) throw new IllegalArgumentException();
        if (interactions == null) throw new IllegalArgumentException();
        if (metadata == null) throw new IllegalArgumentException();
        this.provider = provider;
        this.consumer = consumer;
        this.interactions = interactions;
        this.metadata = metadata;
    }

    private Service provider;
    private Service consumer;
    private List<Interaction> interactions;
    private JsonNode metadata;

}
