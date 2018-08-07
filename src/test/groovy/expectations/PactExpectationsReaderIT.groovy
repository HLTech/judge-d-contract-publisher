package expectations

import com.hltech.contracts.judged.publisher.expectations.Expectation
import com.hltech.contracts.judged.publisher.expectations.PactExpectationsReadException
import com.hltech.contracts.judged.publisher.expectations.PactExpectationsReader
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
            expectations[0].value == "{\"provider\":{\"name\":\"Animal_Service\"},\"consumer\":{\"name\":\"Zoo_App\"},\"interactions\":[{\"provider_state\":\"there is not an alligator named Mary\",\"description\":\"a request for alligator Mary\",\"request\":{\"method\":\"GET\",\"path\":\"/alligators/Mary\"},\"response\":{\"status\":404}},{\"provider_state\":\"there is an alligator named Mary\",\"description\":\"a request for alligator Mary\",\"request\":{\"method\":\"GET\",\"path\":\"/alligators/Mary\"},\"response\":{\"status\":200,\"body\":{\"name\":\"Mary\"}}},{\"provider_state\":\"an error has occurred\",\"description\":\"a request for alligators\",\"request\":{\"method\":\"GET\",\"path\":\"/alligators\"},\"response\":{\"status\":500,\"body\":{\"error\":\"Argh!!!\"}}},{\"provider_state\":\"there are alligators\",\"description\":\"a request for alligators\",\"request\":{\"method\":\"GET\",\"path\":\"/alligators\"},\"response\":{\"status\":200,\"body\":[{\"name\":\"Bob\"}]}},{\"provider_state\":\"there are alligators\",\"description\":\"a request for animals\",\"request\":{\"method\":\"GET\",\"path\":\"/animals\"},\"response\":{\"status\":200,\"body\":[{\"name\":\"Bob\"}]}}],\"metadata\":{\"pact_gem\":{\"version\":\"1.0.9\"}}}"
    }

}
