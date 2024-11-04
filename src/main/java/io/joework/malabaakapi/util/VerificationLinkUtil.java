package io.joework.malabaakapi.util;

import io.joework.malabaakapi.model.VerificationLink;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.UUID;

public class VerificationLinkUtil {

    private VerificationLinkUtil(){}

    public static UUID generateVerificationToken() {
        return UUID.randomUUID();
    }

    public static String createVerificationUrl(String token, HttpServletRequest request) {
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return appUrl + "/verify?token=" + token;
    }

    public static boolean checkIfLinkExpired(VerificationLink verificationLink) {
        return Instant.now().compareTo(verificationLink.getExpiredAt() ) < 0;
    }
}
