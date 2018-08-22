package com.hltech.contracts.judged.publisher.expectations.pact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Service {

    private String name;

    @JsonCreator
    public Service(@JsonProperty("name") String name) {
        if (name == null) throw new IllegalArgumentException();
        this.name = name;
    }
}
