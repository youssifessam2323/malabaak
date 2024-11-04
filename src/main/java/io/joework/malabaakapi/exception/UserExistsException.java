package io.joework.malabaakapi.exception;

public class UserExistsException extends RuntimeException {
    private static final String MESSAGE = "this email already exists";
    public UserExistsException() {
        super(MESSAGE);
    }
}
