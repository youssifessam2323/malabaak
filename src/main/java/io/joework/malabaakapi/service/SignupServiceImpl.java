package io.joework.malabaakapi.service;

import io.joework.malabaakapi.exception.UserExistsException;
import io.joework.malabaakapi.mapper.PlayerMapper;
import io.joework.malabaakapi.model.Player;
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
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final MailService mailService;
    private final EmailTemplateService emailTemplateService;
    private final VerificationService verificationLinkService;

    @Override
    public SignupResponse signUp(SignupRequest signupRequest, HttpServletRequest request) throws MessagingException {

        Player player = playerMapper.fromSignupRequest(signupRequest, new Player());
        if(playerExists(player)){
            throw new UserExistsException();
        }
        log.info("creating new Player account: {}", player);

        Player savedPlayer = playerService.savePlayer(player);

        String mailBody = prepareVerificationEmailBody(request, player, savedPlayer);
        mailService.send(savedPlayer.getEmail(),"Account Verification", mailBody, true);

        return SignupResponse.builder()
                .fullName(savedPlayer.getFirstName() + ' ' + savedPlayer.getLastName())
                .email(player.getEmail())
                .role(player.getRole().name())
                .isVerificationRequired(true)
                .build();
    }

    private String prepareVerificationEmailBody(HttpServletRequest request, Player player, Player savedPlayer) {
        String verificationLinkToken =
                verificationLinkService.createVerificationLink(player, VerificationLinkConfig.builder()
                        .expirationTime(Instant.now().plus(24, ChronoUnit.HOURS))
                        .build());
        String userFullName = savedPlayer.getFirstName() + " " + savedPlayer.getLastName();
        String verificationUrl = VerificationLinkUtil.createVerificationUrl(verificationLinkToken, request);
        return emailTemplateService.prepareEmailTemplate(VERIFICATION_EMAIL_HTML_TEMPLATE,
                        Map.of(
                                "name", userFullName,
                                "verificationUrl", verificationUrl));
    }

    private boolean playerExists(Player player) {
        return playerService.checkPlayerExists(player.getEmail()).isPresent();
    }
}
