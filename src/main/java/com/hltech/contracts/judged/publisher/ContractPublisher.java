package com.hltech.contracts.judged.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hltech.contracts.judged.publisher.config.ConfigurationLoader;
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
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

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


            ConfigurationLoader configurationLoader = new ConfigurationLoader(new ObjectMapper(new YAMLFactory()));

            ContractPublisherConfig contractPublisherConfig = cliOptions.getConfigFile()!=null
                    ? configurationLoader.loadConfig(cliOptions.getConfigFile())
                    : configurationLoader.loadDefaultConfig();

            ServiceContractsForm serviceContracts = new ServiceContractsForm();

            Map<String, String> capabilities = new HashMap<>();
            for (Map.Entry<String, ReaderConfig> rc : contractPublisherConfig.getCapabilities().entrySet()) {
                capabilities.put(
                        rc.getKey(),
                        rc.getValue().read()
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



    @Getter
    public static class CliOptions {

        private static final Options options = createOptions();

        private final File configFile;
        private final String serviceName;
        private final String serviceVersion;
        private final String judgeDLocation;

        public CliOptions(File configFile, String serviceName, String serviceVersion, String judgeDLocation) {
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

        public static CliOptions parseCommandList(String[] args) throws ParseException, IllegalArgumentException {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("serviceName") || !cmd.hasOption("serviceVersion")|| !cmd.hasOption("judgeDLocation")) {
                throw new ParseException("");
            }
            File configFile = cmd.getOptionValue("c")!=null ? new File(cmd.getOptionValue("c")) : null;
            if (configFile == null || configFile.exists() && configFile.isFile()) {
                return new CliOptions(
                        configFile,
                        cmd.getOptionValue("sn"),
                        cmd.getOptionValue("sv"),
                        cmd.getOptionValue("jd"));
            } else {
                throw new IllegalArgumentException(format("Config file '%s' is doesnt exist or is not a file.", cmd.getOptionValue("c")));
            }
        }

        public static void showHelp() {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("judge-d-contract-publisher", options, true);
        }
    }
}
