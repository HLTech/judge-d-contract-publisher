package com.hltech.contracts.judged.publisher.vaunt;

import lombok.Data;

import java.util.List;

@Data
public class Capabilities {
    private final List<Contract> contracts;
}
