package com.hltech.contracts.judged.publisher.capabilities;

import java.util.Properties;

public interface CapabilitiesReader {

    String read(Properties properties);

    String getSupportedFormat();
}
