package io.joework.malabaakapi.service;

import io.joework.malabaakapi.mapper.UserMapper;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.ExpirationTime;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
 public class SignupServiceImpl implements SignupService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final VerificationMailService verificationMailService;

    @Override
    public SignupResponse signUp(SignupRequest signupRequest) throws MessagingException {

        User user = userMapper.fromSignupRequest(signupRequest);
        User savedUser = userService.saveUser(user);


        VerificationLinkConfig verificationLinkConfig = VerificationLinkConfig.builder()
                .expirationTime(new ExpirationTime(24, ChronoUnit.HOURS))
                .build();

        verificationMailService.sendVerificationEmail(user, verificationLinkConfig);

        return SignupResponse.builder()
                .fullName(savedUser.getFirstName() + ' ' + savedUser.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isVerificationRequired(true)
                .build();
    }

}
