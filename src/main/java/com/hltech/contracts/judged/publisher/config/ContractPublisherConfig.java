package com.hltech.contracts.judged.publisher.config;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ContractPublisherConfig {

    private List<ReaderConfig> capabilities;
    private List<ReaderConfig> expectations;
}
