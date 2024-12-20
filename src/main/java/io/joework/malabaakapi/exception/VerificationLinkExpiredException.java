package io.joework.malabaakapi.exception;

public class VerificationLinkExpiredException extends RuntimeException {
    public VerificationLinkExpiredException(String s) {
        super(s);
    }
}
