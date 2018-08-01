package com.hltech.contracts.judged.publisher.capabilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class SwaggerCapabilitiesReader implements CapabilitiesReader {

    public static final String SWAGGER_LOCATION = "swaggerLocation";

    private final String swaggerLocation;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SwaggerCapabilitiesReader(Map<String, String> properties) {
        if (properties.containsKey(SWAGGER_LOCATION)){
            swaggerLocation = properties.get(SWAGGER_LOCATION);
        } else {
            throw new IllegalStateException("invalid configuration of SwaggerCapabilitiesReader. Missing configuration property: "+SWAGGER_LOCATION);
        }
    }

    public String read() throws Exception {
        return objectMapper.writeValueAsString(
                objectMapper.readTree(
                        new File(new File(swaggerLocation), "swagger.json")
                )
        );
    }
}
