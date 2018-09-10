package com.hltech.contracts.judged.publisher.expectations;

import java.util.List;
import java.util.Properties;

public interface ExpectationsReader {

    List<Expectation> read(Properties properties) throws Exception;

}
