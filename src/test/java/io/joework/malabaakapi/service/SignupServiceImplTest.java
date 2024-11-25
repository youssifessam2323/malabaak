package io.joework.malabaakapi.service;

import io.joework.malabaakapi.mapper.UserMapper;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.service.mail.EmailTemplateService;
import io.joework.malabaakapi.service.mail.MailService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static io.joework.malabaakapi.fixtures.UserFixture.getUserEntityNewRecord;
import static io.joework.malabaakapi.fixtures.UserFixture.getValidSignupRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SignupServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailService mailService;

    @Mock
    private EmailTemplateService emailTemplateService;

    @Mock
    private VerificationService verificationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SignupServiceImpl classUnderTest;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = getUserEntityNewRecord();
    }

    @Test
    void signUp() {
        SignupRequest signupRequest = getValidSignupRequest();

        when(userMapper.fromSignupRequest(signupRequest)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
//        doThrow(new MessagingException("message was not sent")).when(mailService).send(any(String.class), any(String.class), any(String.class),any(Boolean.class));
        assertNotNull(signupRequest);
    }
}