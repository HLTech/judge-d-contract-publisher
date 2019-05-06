package com.hltech.contracts.judged.publisher.expectations

import spock.lang.Specification
import spock.lang.Subject

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasSize
import static spock.util.matcher.HamcrestSupport.expect

class VauntExpectationsReaderSpec extends Specification {

    @Subject
    VauntExpectationsReader reader = new VauntExpectationsReader()

    def "should read vaunt expectations from given location" () {
        given:
            def location = 'src/test/resources/vaunt'
        and:
            def properties = new Properties()
            properties.setProperty('vauntLocation', location)
        when:
            def expectations = reader.read(properties)
        then:
            expectations.size() == 2
        and:
            def remoteClientExpectations = expectations.find { it.providerName == 'remote-client' }
            expect remoteClientExpectations.value, isJson()
            expect remoteClientExpectations.value, hasJsonPath('$', hasSize(2))
            expect remoteClientExpectations.value, hasJsonPath('$[0].destinationType', equalTo('QUEUE'))
            expect remoteClientExpectations.value, hasJsonPath('$[0].destinationName', equalTo('reject_information_queue'))
            expect remoteClientExpectations.value, hasJsonPath('$[0].body.type', equalTo('object'))
            expect remoteClientExpectations.value, hasJsonPath('$[0].body.properties.reason.type', equalTo('string'))
            expect remoteClientExpectations.value, hasJsonPath('$[0].body.properties.code.type', equalTo('integer'))
            expect remoteClientExpectations.value, hasJsonPath('$[1].destinationType', equalTo('QUEUE'))
            expect remoteClientExpectations.value, hasJsonPath('$[1].destinationName', equalTo('accept_information_queue'))
            expect remoteClientExpectations.value, hasJsonPath('$[1].body.properties.id.type', equalTo('integer'))
        and:
            def auditServiceExpectations = expectations.find { it.providerName == 'audit-service' }
            expect auditServiceExpectations.value, isJson()
            expect auditServiceExpectations.value, hasJsonPath('$', hasSize(1))
            expect auditServiceExpectations.value, hasJsonPath('$[0].destinationType', equalTo('QUEUE'))
            expect auditServiceExpectations.value, hasJsonPath('$[0].destinationName', equalTo('audit_queue'))
            expect auditServiceExpectations.value, hasJsonPath('$[0].body.type', equalTo('object'))
            expect auditServiceExpectations.value, hasJsonPath('$[0].body.properties.payload.type', equalTo('string'))
    }
}
