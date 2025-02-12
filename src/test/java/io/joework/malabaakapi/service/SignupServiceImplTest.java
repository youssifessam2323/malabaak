package io.joework.malabaakapi.service;

import io.joework.malabaakapi.exception.UserExistsException;
import io.joework.malabaakapi.mapper.UserMapper;
import io.joework.malabaakapi.model.Role;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static io.joework.malabaakapi.fixtures.UserFixture.getUserEntityNewRecord;
import static io.joework.malabaakapi.fixtures.UserFixture.getValidSignupRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private VerificationMailService verificationMailService;

    @InjectMocks
    private SignupServiceImpl classUnderTest;

    private static User user;

    private static String EMAIL_BODY_EXAMPLE = """
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">
            <head>
                <title>Verification Success</title>
            </head>
            <body>
            <h1>Account Verified</h1>
            <p>Your account has been successfully verified. You can now <a href='http://localhost:8080/verify?token=20354d7a-e4fe-47af-8ff6-187bca92f3f9'">log in</a>.</p>
            </body>
            </html>
            """;

    @BeforeAll
    static void setUp() {
        user = getUserEntityNewRecord();
    }

    @Test
    void testSignUp_UsingValidSignupRequest_Success() {
        SignupRequest signupRequest = getValidSignupRequest();

        when(userMapper.fromSignupRequest(signupRequest)).thenReturn(user);
        when(userService.save(user)).thenReturn(user);

        SignupResponse signupResponse = classUnderTest.signUp(signupRequest);

        verify(userService, times(1)).save(any(User.class));
        verify(verificationMailService, times(1)).sendVerificationEmail(any(User.class), any(VerificationLinkConfig.class));

        assertEquals(signupResponse.getEmail(), user.getEmail());
        assertTrue(signupResponse.getIsVerificationRequired());
        assertEquals(signupResponse.getRole(), Role.PLAYER.name());
    }

    @Test
    void testSignUp_SaveUserThrowsPersistenceException_SignupFails() {
        SignupRequest signupRequest = getValidSignupRequest();

        when(userMapper.fromSignupRequest(signupRequest)).thenReturn(user);
        when(userService.save(user)).thenThrow(UserExistsException.class);

        assertThrows(UserExistsException.class, () -> classUnderTest.signUp(signupRequest));
        verify(verificationMailService, never()).sendVerificationEmail(any(User.class), any(VerificationLinkConfig.class));
    }
}