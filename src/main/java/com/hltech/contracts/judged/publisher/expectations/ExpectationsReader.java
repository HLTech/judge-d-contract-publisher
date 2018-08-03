package com.hltech.contracts.judged.publisher.expectations;

import java.util.List;

public interface ExpectationsReader {

    List<Expectation> read() throws Exception;

}
