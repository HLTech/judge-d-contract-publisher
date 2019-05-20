package com.hltech.contracts.judged.publisher.expectations;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hltech.contracts.judged.publisher.CommunicationInterface;
import com.hltech.contracts.judged.publisher.vaunt.VauntFileReader;
import com.hltech.vaunt.core.domain.model.Contract;
import com.hltech.vaunt.core.domain.model.Expectations;
import com.hltech.vaunt.core.domain.model.Service;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@CommunicationInterface("jms")
public class VauntExpectationsReader implements ExpectationsReader {

    private final VauntFileReader vauntFileReader = new VauntFileReader();

    @Override
    public List<Expectation> read(Properties properties) {
        List<Expectations> vauntExpectations = vauntFileReader.readVauntFiles(properties)
            .stream()
            .map(Service::getExpectations)
            .collect(Collectors.toList());

        Multimap<String, Contract> providerNameToContracts = vauntExpectations
            .stream()
            .map(Expectations::getProviderNameToContracts)
            .reduce(ArrayListMultimap.create(), (m1, m2) -> {
                m1.putAll(m2);
                return m1;
            });

        return providerNameToContracts.keySet()
            .stream()
            .map(key -> new Expectation(key, vauntFileReader.serialize(providerNameToContracts.get(key))))
            .collect(Collectors.toList());
    }

    @Override
    public String getSupportedFormat() {
        return MediaType.APPLICATION_JSON;
    }
}
