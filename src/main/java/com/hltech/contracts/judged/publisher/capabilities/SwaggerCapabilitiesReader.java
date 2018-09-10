package com.hltech.contracts.judged.publisher.capabilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.contracts.judged.publisher.CommunicationInterface;
import com.hltech.contracts.judged.publisher.ContractReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@CommunicationInterface("rest")
public class SwaggerCapabilitiesReader implements CapabilitiesReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerCapabilitiesReader.class);

    public static final String SWAGGER_LOCATION_CONFIG_KEY = "swaggerLocation";
    public static final String DEFAUlT_SWAGGER_LOCATION = "./target/classes/static/swagger";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String read(Properties properties) {
        String swaggerLocation = DEFAUlT_SWAGGER_LOCATION;
        if (properties.containsKey(SWAGGER_LOCATION_CONFIG_KEY)) {
            swaggerLocation = properties.getProperty(SWAGGER_LOCATION_CONFIG_KEY);
            LOGGER.info("using: '" + swaggerLocation + "' as path to swagger.json ");
        } else {
            LOGGER.info(SWAGGER_LOCATION_CONFIG_KEY + " parameters has not been provided. Using default swagger.json location: " + DEFAUlT_SWAGGER_LOCATION);
        }
        try {
            return objectMapper.writeValueAsString(
                objectMapper.readTree(new File(new File(swaggerLocation), "swagger.json"))
            );
        } catch (IOException e) {
            throw new ContractReadException("Unexpected exception when reading swagger.json", e);
        }
    }
}
