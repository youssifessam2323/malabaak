package io.joework.malabaakapi.service;

import io.joework.malabaakapi.mapper.UserMapper;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import io.joework.malabaakapi.service.mail.MailService;
import io.joework.malabaakapi.service.mail.EmailTemplateService;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
 public class SignupServiceImpl implements SignupService {
    private static final String VERIFICATION_EMAIL_HTML_TEMPLATE = "verification-email";
    private final UserService userService;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final EmailTemplateService emailTemplateService;
    private final VerificationService verificationLinkService;
    private final HttpServletRequest request;
    @Override
    public SignupResponse signUp(SignupRequest signupRequest) throws MessagingException {

        User user = userMapper.fromSignupRequest(signupRequest);
        User savedUser = userService.saveUser(user);

        String mailBody = prepareVerificationEmailBody(request, savedUser);
        mailService.send(savedUser.getEmail(),"Account Verification", mailBody, true);

        return SignupResponse.builder()
                .fullName(savedUser.getFirstName() + ' ' + savedUser.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isVerificationRequired(true)
                .build();
    }

    private String prepareVerificationEmailBody(HttpServletRequest request, User user) {
        String verificationLinkToken =
                verificationLinkService.createVerificationLink(user, VerificationLinkConfig.builder()
                        .expirationTime(Instant.now().plus(24, ChronoUnit.HOURS))
                        .build());
        String userFullName = user.getFirstName() + " " + user.getLastName();
        String verificationUrl = VerificationLinkUtil.createVerificationUrl(verificationLinkToken, request);
        return emailTemplateService.prepareEmailTemplate(VERIFICATION_EMAIL_HTML_TEMPLATE,
                        Map.of(
                                "name", userFullName,
                                "verificationUrl", verificationUrl));
    }

}
