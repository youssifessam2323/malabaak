package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.config.VerificationLinkConfig;
import jakarta.mail.MessagingException;

public interface VerificationMailService {
    void sendVerificationEmail(User user, VerificationLinkConfig verificationLinkConfig) throws MessagingException;
}
