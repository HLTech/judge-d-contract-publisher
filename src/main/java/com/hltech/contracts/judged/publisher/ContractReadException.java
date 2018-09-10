package com.hltech.contracts.judged.publisher;

public class ContractReadException extends RuntimeException {

    public ContractReadException(String message) {
        super(message);
    }

    public ContractReadException(String message, Throwable e) {
        super(message, e);
    }

}
