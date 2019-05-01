package com.hltech.contracts.judged.publisher.vaunt;

import com.google.common.collect.Multimap;
import lombok.Data;

@Data
public class Expectations {
    private final Multimap<String, Contract> providerNameToContracts;
}
