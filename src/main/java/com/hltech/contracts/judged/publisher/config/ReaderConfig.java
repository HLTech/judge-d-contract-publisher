package com.hltech.contracts.judged.publisher.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import com.hltech.contracts.judged.publisher.capabilities.CapabilitiesReader;

import java.lang.reflect.Constructor;
import java.util.Map;

@Data
@ToString
@Slf4j
public class ReaderConfig {

    @JsonProperty("interface")
    private String _interface;

    private String readerClass;

    private Map<String, String> config;

    public String read() throws ContractReadException {
        try {
            Constructor<?> constructor = Class.forName(readerClass).getConstructor(Map.class);

            CapabilitiesReader capabilitiesReader = (CapabilitiesReader) constructor.newInstance(config);
            return capabilitiesReader.read();
        } catch (Exception e ){
            throw new ContractReadException("unable to read contracts with: "+readerClass, e);
        }
    }
}
