package expectations

import com.hltech.contracts.judged.publisher.expectations.Expectation
import com.hltech.contracts.judged.publisher.expectations.PactExpectationsReader
import org.junit.Test
import spock.lang.Specification

class PactExpectationsReaderIT extends Specification {

    Map<String, String> configuration
    PactExpectationsReader pactExpectationsReader

    def setup() {
        configuration = new HashMap<String, String>()
    }

    def 'should throw IllegalStateException when configuration does not have pactsLocation'() {
        when:
            new PactExpectationsReader(configuration)

        then:
            thrown(IllegalStateException)
    }

    def 'should throw IllegalStateException when pactsLocation does not exist'() {
        given:
            configuration.put("pactsLocation", "")
            pactExpectationsReader = new PactExpectationsReader(configuration)

        when:
            pactExpectationsReader.read()

        then:
            thrown(IllegalStateException)
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
    }

}
