package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;

public interface VerificationMailService {
    void sendVerificationEmail(User user, VerificationLinkConfig verificationLinkConfig);
}
