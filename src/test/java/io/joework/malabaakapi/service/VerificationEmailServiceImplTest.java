package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.model.config.ExpirationTime;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.joework.malabaakapi.fixtures.UserFixture.getUserEntityNewRecord;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationEmailServiceImplTest {

    private static User user;

    @Mock
    private EmailTemplateService emailTemplateService;

    @Mock
    private MailService mailService;

    @Mock
    private VerificationMailConfigProperties verificationMailConfigProperties = new VerificationMailConfigProperties("http://example.com");

    @Mock
    private VerificationLinkRepository verificationLinkRepository;

    @InjectMocks
    private VerificationEmailServiceImpl classUnderTest;

    @BeforeAll
    static void setup(){
        user = getUserEntityNewRecord();
    }
    @Test
    void sendVerificationEmail_ShouldSuccessWhen_HavingValidUserAndVerificationConfig() {
        //Arrange
        VerificationLink verificationLink = new VerificationLink(
                1,
                UUID.randomUUID(),
                user,
                Instant.now()
        );
        when(verificationLinkRepository.save(any(VerificationLink.class))).thenReturn(verificationLink);
        when(emailTemplateService.prepareEmailTemplate(any(String.class),any(Map.class))).thenReturn("mailBody");
        //Act
        //Asserts
         assertDoesNotThrow(
                 () -> classUnderTest.sendVerificationEmail(user, VerificationLinkConfig.builder()
                         .expirationTime(new ExpirationTime(24, ChronoUnit.HOURS)).build())
         );
    }
}