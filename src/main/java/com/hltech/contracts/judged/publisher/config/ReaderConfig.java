package com.hltech.contracts.judged.publisher.config;

import com.hltech.contracts.judged.publisher.capabilities.CapabilitiesReader;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ReaderConfig {

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

    public String getReaderClass() {
        return readerClass;
    }

    public void setReaderClass(String readerClass) {
        this.readerClass = readerClass;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
