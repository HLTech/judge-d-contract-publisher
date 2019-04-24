package com.hltech.contracts.judged.publisher

import com.hltech.contracts.judged.publisher.capabilities.SwaggerCapabilitiesReader
import com.hltech.contracts.judged.publisher.expectations.pact.PactExpectationsReader
import spock.lang.Specification
import spock.lang.Subject

class ContractReaderLoaderTest extends Specification {

    @Subject
    ContractReaderLoader loader = new ContractReaderLoader()

    def "should load capabilities readers"() {
        when:
            def readers = loader.getCapabilitiesReaders(['rest'] as Set<String>)
        then:
            readers.size() == 1
        and:
            readers['rest'].class == SwaggerCapabilitiesReader
    }

    def "should load expectations readers"() {
        when:
            def readers = loader.getExpectationsReaders(['rest'] as Set<String>)
        then:
            readers.size() == 1
        and:
            readers['rest'].class == PactExpectationsReader
    }
}
