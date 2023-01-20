package com.acmebank.accountmanager.constant;

public class ErrorCode {
    public static final String ERR_ACME_001 = "ERR_ACME_001"; // Unknown Error
    public static final String ERR_ACME_002 = "ERR_ACME_002"; // Account Not Found
    public static final String ERR_ACME_003 = "ERR_ACME_003"; // Insufficient Balance
    public static final String ERR_ACME_004 = "ERR_ACME_004"; // Source account is the same as target account
    public static final String ERR_ACME_005 = "ERR_ACME_005"; // Transfer amount must be greater than 0.0001
    public static final String ERR_ACME_006 = "ERR_ACME_006"; // Incorrect parameters
}
