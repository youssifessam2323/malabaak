package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.Player;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;

public interface VerificationService {
    String createVerificationLink(Player player, VerificationLinkConfig verificationLinkConfig);
}
