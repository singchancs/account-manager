package com.acmebank.accountmanager.exceptions;

import com.acmebank.accountmanager.constant.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TransferFailedException extends RuntimeException {

    private final String errorCode;

    public TransferFailedException(String errorCode, String errorMessage) {
        super(TransferFailedException.generateMessage(errorCode, errorMessage));
        this.errorCode = errorCode;
    }

    public TransferFailedException(String errorCode, String errorMessage, Throwable throwable) {
        super(TransferFailedException.generateMessage(errorCode, errorMessage), throwable);
        this.errorCode = errorCode;
    }

    private static String generateMessage(String errorCode, String errorMessage) {
        return "[" + errorCode + "] " + errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
