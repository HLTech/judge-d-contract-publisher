package com.hltech.contracts.judged.publisher.vaunt;

import lombok.Data;

@Data
public class Service {

    private final String name;
    private final Capabilities capabilities;
    private final Expectations expectations;
}
