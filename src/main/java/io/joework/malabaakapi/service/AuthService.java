package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.dto.LoginResponse;
import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.util.Map;

public interface AuthService {

    LoginResponse postLogin(Authentication authentication);

    LoginResponse generateNewToken(String refreshToken);

    LoginResponse googleAuth(String token) throws ParseException;
}
