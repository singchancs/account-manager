package com.acmebank.accountmanager.exceptions;

import com.acmebank.accountmanager.constant.ErrorCode;

public class TransferFailedException extends Exception {

    private final String errorCode = ErrorCode.ERR_ACME_001;

    public TransferFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
