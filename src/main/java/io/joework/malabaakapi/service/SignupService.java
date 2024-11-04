package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface SignupService {
    SignupResponse signUp(SignupRequest signupRequest, HttpServletRequest request) throws MessagingException;
}
