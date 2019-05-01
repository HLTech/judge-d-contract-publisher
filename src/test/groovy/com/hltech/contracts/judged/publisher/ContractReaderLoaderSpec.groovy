package com.hltech.contracts.judged.publisher

import com.hltech.contracts.judged.publisher.capabilities.SwaggerCapabilitiesReader
import com.hltech.contracts.judged.publisher.capabilities.VauntCapabilitiesReader
import com.hltech.contracts.judged.publisher.expectations.pact.PactExpectationsReader
import spock.lang.Specification
import spock.lang.Subject

class ContractReaderLoaderSpec extends Specification {

    @Subject
    ContractReaderLoader loader = new ContractReaderLoader()

    def "should load capabilities readers"() {
        when:
            def readers = loader.getCapabilitiesReaders(['rest', 'jms'] as Set<String>)
        then:
            readers.size() == 2
        and:
            readers['rest'].class == SwaggerCapabilitiesReader
            readers['jms'].class == VauntCapabilitiesReader
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
