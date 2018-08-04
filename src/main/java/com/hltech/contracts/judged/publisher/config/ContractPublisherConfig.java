package com.hltech.contracts.judged.publisher.config;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class ContractPublisherConfig {

    private Map<String, ReaderConfig> capabilities;
    private Map<String, ReaderConfig> expectations;
}
