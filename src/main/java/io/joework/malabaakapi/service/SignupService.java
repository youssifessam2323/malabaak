package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import jakarta.mail.MessagingException;

public interface SignupService {
    SignupResponse signUp(SignupRequest signupRequest) throws MessagingException;
}
