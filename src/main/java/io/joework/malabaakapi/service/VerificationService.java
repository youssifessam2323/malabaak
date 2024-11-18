package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;

public interface VerificationService {
    String createVerificationLink(User user, VerificationLinkConfig verificationLinkConfig);
}
