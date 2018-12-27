package com.hltech.contracts.judged.publisher.integration.judged;


import java.io.Serializable;
import java.util.Map;

public class ServiceContractsForm {

    private final Map<String, ContractForm> capabilities;
    private final Map<String, Map<String, ContractForm>> expectations;

    public ServiceContractsForm(Map<String, ContractForm> capabilities, Map<String, Map<String, ContractForm>> expectations) {
        this.capabilities = capabilities;
        this.expectations = expectations;
    }

    public Map<String, ContractForm> getCapabilities() {
        return capabilities;
    }

    public Map<String, Map<String, ContractForm>> getExpectations() {
        return expectations;
    }

    @Override
    public String toString() {
        return "ServiceContractsForm{" +
            "capabilities=" + capabilities +
            ", expectations=" + expectations +
            '}';
    }

    public static class ContractForm implements Serializable {

        private final String value;
        private final String mimeType;

        public ContractForm(String value, String mimeType) {
            this.value = value;
            this.mimeType = mimeType;
        }

        public String getValue() {
            return value;
        }

        public String getMimeType() {
            return mimeType;
        }

        @Override
        public String toString() {
            return "ContractForm{" +
                "value='" + value + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
        }
    }
}
