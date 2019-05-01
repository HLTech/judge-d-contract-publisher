package com.hltech.contracts.judged.publisher.capabilities

import spock.lang.Specification
import spock.lang.Subject

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.collection.IsCollectionWithSize.hasSize
import static spock.util.matcher.HamcrestSupport.expect

class VauntCapabilitiesReaderSpec extends Specification {

    @Subject
    VauntCapabilitiesReader reader = new VauntCapabilitiesReader()

    def "should read service capabilities from location given in properties"() {
        given:
            def location = 'src/test/resources/vaunt'
        and:
            def properties = new Properties()
            properties.setProperty('vauntLocation', location)
        when:
            def capabilities = reader.read(properties)
        then:
            expect capabilities, isJson()
            expect capabilities, hasJsonPath('$.contracts', hasSize(2))
        and:
            expect capabilities, hasJsonPath('$.contracts[0].destinationType', equalTo('QUEUE'))
            expect capabilities, hasJsonPath('$.contracts[0].destinationName', equalTo('request_for_information_queue'))
            expect capabilities, hasJsonPath('$.contracts[0].body.type', equalTo('object'))
            expect capabilities, hasJsonPath('$.contracts[0].body.properties.name.type', equalTo('string'))
        and:
            expect capabilities, hasJsonPath('$.contracts[1].destinationType', equalTo('TOPIC'))
            expect capabilities, hasJsonPath('$.contracts[1].destinationName', equalTo('something_changed_topic'))
            expect capabilities, hasJsonPath('$.contracts[1].body.type', equalTo('object'))
            expect capabilities, hasJsonPath('$.contracts[1].body.properties.timestamp.type', equalTo('integer'))
    }
}
