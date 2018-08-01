package com.hltech.contracts.judged.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hltech.contracts.judged.publisher.config.ContractPublisherConfig;
import com.hltech.contracts.judged.publisher.config.ReaderConfig;
import com.hltech.contracts.judged.publisher.integration.judged.ServiceContractsForm;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import com.hltech.contracts.judged.publisher.integration.judged.JudgeDClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ContractPublisher {

    public static void main(String[] args) throws Exception {

        try {
            final CliOptions cliOptions = CliOptions.parseCommandList(args);

            log.info("--------------------------");
            log.info("configFile = " + (cliOptions.getConfigFile()==null?"<default>":cliOptions.getConfigFile()));
            log.info("serviceName = " + cliOptions.getServiceName());
            log.info("serviceVersion = " + cliOptions.getServiceVersion());
            log.info("Judge-D location = " + cliOptions.getJudgeDLocation());
            log.info("--------------------------");

            ContractPublisherConfig contractPublisherConfig = loadConfig(cliOptions.getConfigFile());

            ServiceContractsForm serviceContracts = new ServiceContractsForm();

            Map<String, String> capabilities = new HashMap<>();
            for (ReaderConfig rc : contractPublisherConfig.getCapabilities()) {
                capabilities.put(
                        rc.get_interface(),
                        rc.read()
                );
            }
            serviceContracts.setCapabilities(capabilities);
            serviceContracts.setExpectations(new HashMap<>());


            log.info("publishing service contracts: "+serviceContracts);

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

    private static ContractPublisherConfig loadConfig(String configFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        if (configFile == null) {

            return objectMapper.readValue(ContractPublisher.class.getResourceAsStream("/default-config.yaml"), ContractPublisherConfig.class);
        } else {
            return objectMapper.readValue(new File(configFile), ContractPublisherConfig.class);
        }
    }

    @Getter
    public static class CliOptions {

        private static final Options options = createOptions();
        private final String configFile;
        private final String serviceName;
        private final String serviceVersion;
        private final String judgeDLocation;

        public CliOptions(String configFile, String serviceName, String serviceVersion, String judgeDLocation) {
            this.configFile = configFile;
            this.serviceName = serviceName;
            this.serviceVersion = serviceVersion;
            this.judgeDLocation = judgeDLocation;
        }

        private static Options createOptions() {
            Options options = new Options();
            options.addOption("c", "configFile", true, "Config file location");
            options.addOption("sn", "serviceName", true, "Name of a service");
            options.addOption("sv", "serviceVersion", true, "Version of a service");
            options.addOption("jd", "judgeDLocation", true, "Url of Judge-D");
            return options;
        }

        public static CliOptions parseCommandList(String[] args) throws ParseException {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("serviceName") || !cmd.hasOption("serviceVersion")|| !cmd.hasOption("judgeDLocation")) {
                throw new ParseException("");
            }

            return new CliOptions(
                    cmd.getOptionValue("c"),
                    cmd.getOptionValue("sn"),
                    cmd.getOptionValue("sv"),
                    cmd.getOptionValue("jd"));
        }

        public static void showHelp() {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("judge-d-contract-publisher", options, true);
        }
    }
}
