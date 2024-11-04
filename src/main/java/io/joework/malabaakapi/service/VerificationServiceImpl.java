package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.Player;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationLinkRepository verificationLinkRepository;

    @Override
    public String createVerificationLink(Player player, VerificationLinkConfig verificationLinkConfig) {
        VerificationLink verificationLink = new VerificationLink();
        verificationLink.setPlayer(player);
        verificationLink.setExpiredAt(verificationLinkConfig.getExpirationTime());
        UUID verificationTokenUUID = VerificationLinkUtil.generateVerificationToken();
        verificationLink.setVerificationToken(verificationTokenUUID);
        UUID verificationToken = verificationLinkRepository.save(verificationLink).getVerificationToken();
        return verificationToken.toString();
    }
}
