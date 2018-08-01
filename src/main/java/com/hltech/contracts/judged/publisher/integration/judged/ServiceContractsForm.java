package com.hltech.contracts.judged.publisher.integration.judged;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ServiceContractsForm {

    private Map<String, String> capabilities;
    private Map<String, Map<String, String>> expectations;

}
