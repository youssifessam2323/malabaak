package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
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

    /**
     *
     * @param user the user that this verification link will be created for
     * @param verificationLinkConfig verification link configuration
     * @return verification link created token
     */
    @Override
    public String createVerificationLink(User user, VerificationLinkConfig verificationLinkConfig) {
        VerificationLink verificationLink = new VerificationLink();
        verificationLink.setUser(user);
        verificationLink.setExpiredAt(verificationLinkConfig.getExpirationTime());
        UUID verificationTokenUUID = VerificationLinkUtil.generateVerificationToken();
        verificationLink.setVerificationToken(verificationTokenUUID);
        UUID verificationToken = verificationLinkRepository.save(verificationLink).getVerificationToken();
        return verificationToken.toString();
    }
}
