package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * this class responsible for taking the user and building the body for the email or the message
 * that we need to send to the user
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationEmailServiceImpl implements VerificationMailService {

    private static final String VERIFICATION_EMAIL_HTML_TEMPLATE = "verification-email";


    private final EmailTemplateService emailTemplateService;
    private final MailService mailService;
    private final VerificationMailConfigProperties verificationMailConfigProperties;
    private final VerificationLinkRepository verificationLinkRepository;

    @Override
    public void sendVerificationEmail(User user, VerificationLinkConfig verificationLinkConfig) throws MessagingException {
        log.debug("creating verification token...");
        String verificationLinkToken =
                createVerificationLink(user, verificationLinkConfig);
        String userFullName = user.getFirstName() + " " + user.getLastName();
        String verificationUrl = VerificationLinkUtil.createVerificationUrl(verificationMailConfigProperties.baseUrl(), verificationLinkToken);

        log.debug("preparing the email template...");
        String mailBody = emailTemplateService.prepareEmailTemplate(VERIFICATION_EMAIL_HTML_TEMPLATE,
                Map.of(
                        "name", userFullName,
                        "verificationUrl", verificationUrl));

        log.debug("sending the email...");
        mailService.send(user.getEmail(),"Account Verification", mailBody, true);
    }

    /**
     *
     * @param user the user that this verification link will be created for
     * @param verificationLinkConfig verification link configuration
     * @return verification link created token
     */
    private String createVerificationLink(User user, VerificationLinkConfig verificationLinkConfig) {
        VerificationLink verificationLink = new VerificationLink();
        verificationLink.setUser(user);
        verificationLink.setExpiredAt(Instant.now()
                .plus(verificationLinkConfig.getExpirationTime().expirationTime(),
                        verificationLinkConfig.getExpirationTime().timeUnit()));
        UUID verificationTokenUUID = VerificationLinkUtil.generateVerificationToken();
        verificationLink.setVerificationToken(verificationTokenUUID);
        UUID verificationToken = verificationLinkRepository.save(verificationLink).getVerificationToken();
        return verificationToken.toString();
    }


}
