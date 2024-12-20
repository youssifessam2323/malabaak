package io.joework.malabaakapi.util;

import io.joework.malabaakapi.model.VerificationLink;

import java.time.Instant;
import java.util.UUID;

public class VerificationLinkUtil {

    private VerificationLinkUtil(){}

    public static UUID generateVerificationToken() {
        return UUID.randomUUID();
    }

    public static String createVerificationUrl(String baseUrl, String token) {
        return baseUrl + "/verify?token=" + token;
    }

    public static boolean isLinkExpired(VerificationLink verificationLink) {
        return Instant.now().compareTo(verificationLink.getExpiredAt() ) < 0;
    }
}
