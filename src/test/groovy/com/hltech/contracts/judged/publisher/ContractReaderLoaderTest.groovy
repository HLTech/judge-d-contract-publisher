package com.hltech.contracts.judged.publisher

import com.hltech.contracts.judged.publisher.capabilities.SwaggerCapabilitiesReader
import com.hltech.contracts.judged.publisher.expectations.pact.PactExpectationsReader
import spock.lang.Specification

class ContractReaderLoaderTest extends Specification {

    def "should load capabilities readers"() {
        when:
            def readers = new ContractReaderLoader().capabilitiesReaders
        then:
            readers.get("rest") instanceof SwaggerCapabilitiesReader
    }

    def "should load expectations readers"() {
        when:
            def readers = new ContractReaderLoader().expectationsReaders
        then:
            readers.get("rest") instanceof PactExpectationsReader
    }
}
