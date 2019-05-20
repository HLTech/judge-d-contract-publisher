package com.hltech.contracts.judged.publisher.capabilities;

import com.hltech.contracts.judged.publisher.CommunicationInterface;
import com.hltech.contracts.judged.publisher.vaunt.VauntFileReader;
import com.hltech.vaunt.core.domain.model.Contract;
import com.hltech.vaunt.core.domain.model.Service;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@CommunicationInterface("jms")
public class VauntCapabilitiesReader implements CapabilitiesReader {

    private final VauntFileReader vauntFileReader = new VauntFileReader();

    @Override
    public String read(Properties properties) {
        List<Contract> contracts = vauntFileReader.readVauntFiles(properties)
            .stream()
            .map(Service::getCapabilities)
            .flatMap(capabilities -> capabilities.getContracts().stream())
            .collect(Collectors.toList());

        return vauntFileReader.serialize(contracts);
    }

    @Override
    public String getSupportedFormat() {
        return MediaType.APPLICATION_JSON;
    }
}
