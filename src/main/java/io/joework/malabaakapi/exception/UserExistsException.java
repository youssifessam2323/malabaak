package io.joework.malabaakapi.exception;

public class UserExistsException extends RuntimeException {
    private static final String MESSAGE = "this account already exists";
    public UserExistsException(String message) {
        super(MESSAGE);
    }
}
