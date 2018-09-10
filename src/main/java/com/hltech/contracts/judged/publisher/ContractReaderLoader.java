package com.hltech.contracts.judged.publisher;

import com.hltech.contracts.judged.publisher.capabilities.CapabilitiesReader;
import com.hltech.contracts.judged.publisher.expectations.ExpectationsReader;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.Spliterator;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

public class ContractReaderLoader {

    public Map<String, CapabilitiesReader> getCapabilitiesReaders(Set<String> requestedCommunicationInterfaces) {
        return loadContractReaders(CapabilitiesReader.class, requestedCommunicationInterfaces);
    }

    public Map<String, ExpectationsReader> getExpectationsReaders(Set<String> requestedCommunicationInterfaces) {
        return loadContractReaders(ExpectationsReader.class, requestedCommunicationInterfaces);
    }

    private <T> Map<String, T> loadContractReaders(Class<T> service, Set<String> requestedCommunicationInterfaces) {
        ServiceLoader<T> load = ServiceLoader.load(service);

        return stream(spliteratorUnknownSize(load.iterator(), Spliterator.ORDERED), false)
            .filter(i -> i.getClass().getAnnotation(CommunicationInterface.class) != null)
            .filter(i -> requestedCommunicationInterfaces.contains(i.getClass().getAnnotation(CommunicationInterface.class).value()))
            .collect(toMap(
                i -> i.getClass().getAnnotation(CommunicationInterface.class).value(),
                identity()
            ));
    }


}
