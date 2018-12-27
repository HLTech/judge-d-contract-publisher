package com.hltech.contracts.judged.publisher;

import com.hltech.contracts.judged.publisher.capabilities.CapabilitiesReader;
import com.hltech.contracts.judged.publisher.expectations.Expectation;
import com.hltech.contracts.judged.publisher.expectations.ExpectationsReader;
import com.hltech.contracts.judged.publisher.integration.judged.JudgeDClient;
import com.hltech.contracts.judged.publisher.integration.judged.ServiceContractsForm;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.*;

public class ContractPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractPublisher.class);

    public static void main(String[] args) throws Exception {

        try {
            final CliOptions cliOptions = CliOptions.parseCommandList(args);

            LOGGER.info("--------------------------");
            LOGGER.info("serviceName = " + cliOptions.getServiceName());
            LOGGER.info("serviceVersion = " + cliOptions.getServiceVersion());
            LOGGER.info("Judge-D location = " + cliOptions.getJudgeDLocation());
            LOGGER.info("Capabilities = " + cliOptions.getCapabilities());
            LOGGER.info("Expectations = " + cliOptions.getExpectations());
            LOGGER.info("--------------------------");

            ContractReaderLoader contractReaderLoader = new ContractReaderLoader();
            Map<String, CapabilitiesReader> allAvailableCapabilitiesReaders = contractReaderLoader.getCapabilitiesReaders(cliOptions.getCapabilities());
            Map<String, ExpectationsReader> allAvailableExpectationsReaders = contractReaderLoader.getExpectationsReaders(cliOptions.getExpectations());

            ServiceContractsForm serviceContracts = readContracts(
                allAvailableCapabilitiesReaders,
                allAvailableExpectationsReaders,
                System.getProperties()
            );

            LOGGER.info("publishing service contracts: " + serviceContracts);
            JudgeDClient contractPublisher = Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .contract(new JAXRSContract())
                .target(JudgeDClient.class, cliOptions.getJudgeDLocation());
            contractPublisher.publish(cliOptions.getServiceName(), cliOptions.getServiceVersion(), serviceContracts);
        } catch (ParseException e) {
            CliOptions.showHelp();
        }

    }

    private static ServiceContractsForm readContracts(
        Map<String, CapabilitiesReader> capabilitiesReaders,
        Map<String, ExpectationsReader> expectationsReaders,
        Properties configuration
    ) throws Exception {
        Map<String, ServiceContractsForm.ContractForm> capabilities = new HashMap<>();
        for (Map.Entry<String, CapabilitiesReader> rc : capabilitiesReaders.entrySet()) {
            capabilities.put(
                rc.getKey(),
                new ServiceContractsForm.ContractForm(rc.getValue().read(configuration), MediaType.APPLICATION_JSON)
            );
        }
        Map<String, Map<String, ServiceContractsForm.ContractForm>> expectations = new HashMap<>();
        for (Map.Entry<String, ExpectationsReader> rc : expectationsReaders.entrySet()) {
            List<Expectation> read = rc.getValue().read(configuration);
            for (Expectation e : read) {
                if (!expectations.containsKey(e.getProviderName())) {
                    expectations.put(e.getProviderName(), new HashMap<>());
                }
                expectations.get(
                    e.getProviderName()).put(rc.getKey(),
                    new ServiceContractsForm.ContractForm(e.getValue(), MediaType.APPLICATION_JSON));
            }
        }

        return new ServiceContractsForm(capabilities, expectations);
    }


    public static class CliOptions {

        private static final Options options = createOptions();

        private final String serviceName;
        private final String serviceVersion;
        private final String judgeDLocation;
        private final String expectations;
        private final String capabilities;

        public CliOptions(String serviceName, String serviceVersion, String judgeDLocation, String expectations, String capabilities) {
            this.serviceName = serviceName;
            this.serviceVersion = serviceVersion;
            this.judgeDLocation = judgeDLocation;
            this.expectations = expectations != null ? expectations : "";
            this.capabilities = capabilities != null ? capabilities : "";
        }

        private static Options createOptions() {
            Options options = new Options();
            options.addOption("ex", "expectations", true, "Coma separated list of expectations communication interfaces (if not set will use all available Readers)");
            options.addOption("cb", "capabilities", true, "Coma separated list of capabilities communication interfaces (if not set will use all available Readers)");
            options.addOption("sn", "serviceName", true, "Name of a service");
            options.addOption("sv", "serviceVersion", true, "Version of a service");
            options.addOption("jd", "judgeDLocation", true, "Url of Judge-D");
            return options;
        }

        public static CliOptions parseCommandList(String[] args) throws ParseException, IllegalArgumentException {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("serviceName") || !cmd.hasOption("serviceVersion") || !cmd.hasOption("judgeDLocation")) {
                throw new ParseException("");
            }
            return new CliOptions(
                cmd.getOptionValue("sn"),
                cmd.getOptionValue("sv"),
                cmd.getOptionValue("jd"),
                cmd.getOptionValue("ex"),
                cmd.getOptionValue("cb")
            );
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getServiceVersion() {
            return serviceVersion;
        }

        public String getJudgeDLocation() {
            return judgeDLocation;
        }

        public Set<String> getExpectations() {
            HashSet<String> result = new HashSet<>();
            for (String e : this.expectations.split(",")) {
                if (!e.trim().isEmpty())
                    result.add(e.trim());
            }
            return result;
        }

        public Set<String> getCapabilities() {
            HashSet<String> result = new HashSet<>();
            for (String e : this.capabilities.split(",")) {
                if (!e.trim().isEmpty())
                    result.add(e.trim());
            }
            return result;
        }

        public static void showHelp() {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("judge-d-contract-publisher", options, true);
        }


    }
}
