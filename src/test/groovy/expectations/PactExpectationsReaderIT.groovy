package expectations

import com.fasterxml.jackson.databind.ObjectMapper
import com.hltech.contracts.judged.publisher.expectations.Expectation
import com.hltech.contracts.judged.publisher.expectations.pact.PactExpectationsReadException
import com.hltech.contracts.judged.publisher.expectations.pact.PactExpectationsReader
import spock.lang.Specification

class PactExpectationsReaderIT extends Specification {

    Map<String, String> configuration
    PactExpectationsReader pactExpectationsReader

    def setup() {
        configuration = new HashMap<String, String>()
    }

    def 'should throw PactExpectationsReadException when configuration does not have pactsLocation'() {
        when:
            new PactExpectationsReader(configuration)

        then:
            thrown(PactExpectationsReadException)
    }

    def 'should throw PactExpectationsReadException when pactsLocation does not exist'() {
        given:
            configuration.put("pactsLocation", "")
            pactExpectationsReader = new PactExpectationsReader(configuration)

        when:
            pactExpectationsReader.read()

        then:
            thrown(PactExpectationsReadException)
    }

    def 'should read expectations from files under pactsLocation folder'() {
        given:
            String pathLocation = new File("src/test/resources/expectations").getAbsolutePath()
            configuration.put("pactsLocation", pathLocation)
            pactExpectationsReader = new PactExpectationsReader(configuration)

        when:
            List<Expectation> expectations = pactExpectationsReader.read()

        then:
            expectations.size() == 1
            expectations[0].providerName == "Animal_Service"
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(expectations[0].value) == objectMapper.readTree(new File(new File(pathLocation), "example-pact-file.json"))
    }

}
