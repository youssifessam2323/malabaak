package io.joework.malabaakapi.service;

import io.joework.malabaakapi.configurations.TokensConfigProperties;
import io.joework.malabaakapi.model.RefreshToken;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.repository.RefreshTokenRepository;
import io.joework.malabaakapi.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final String ACCESS_TOKEN_TYPE = "access_token";

    private final JwtEncoder encoder;
    private final TokensConfigProperties tokensConfigProperties;
    private final RefreshTokenRepository refreshTokenRepository;


    public String generateAccessToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var expiryTimeInSeconds = Long.parseLong(tokensConfigProperties.getAccessTokenConfig().getExpiryTimeInSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expiryTimeInSeconds, ChronoUnit.SECONDS))
                .subject(authentication.getName())
                .claim("tkn_type", ACCESS_TOKEN_TYPE)
                .claim("scope", scope)
                .claims(c -> c.putAll(Map.of("first_name", authentication.getName(), "last_name", authentication.getName())))
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String generateRefreshToken(Authentication authentication) {

        Instant now = Instant.now();
        var expiryTimeInSeconds = Long.parseLong(tokensConfigProperties.getRefreshTokenConfig().getExpiryTimeInSeconds());
        var user = (User) authentication.getPrincipal();

        return refreshTokenRepository.findByUser_Id(user.getId())
                .map(existingToken -> {
                    if (!DateUtil.isExpired(existingToken.getExpiresAt())) {
                        return existingToken.getRefreshToken().toString(); // Return valid token
                    } else {
                        refreshTokenRepository.delete(existingToken); // Delete expired token
                        return createAndSaveRefreshToken(user, now, expiryTimeInSeconds);
                    }
                })
                .orElseGet(() -> createAndSaveRefreshToken(user, now, expiryTimeInSeconds));
    }



    private String createAndSaveRefreshToken(User user, Instant now, long expiryTimeInSeconds) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID())
                .expiresAt(LocalDateTime.ofInstant(now.plus(expiryTimeInSeconds, ChronoUnit.SECONDS), ZoneId.systemDefault()))
                .user(user)
                .build();

        return refreshTokenRepository.save(newRefreshToken).getRefreshToken().toString();
    }

    public String generateAccessToken(Collection<String> authorities, String sub) {
        Instant now = Instant.now();
        String scope = String.join(" ", authorities);

        var expiryTimeInSeconds = Long.parseLong(tokensConfigProperties.getAccessTokenConfig().getExpiryTimeInSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expiryTimeInSeconds, ChronoUnit.SECONDS))
                .subject(sub)
                .claim("tkn_type", ACCESS_TOKEN_TYPE)
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * regenerate the access token from  the refresh token
     * @param refreshTokenVal refresh token value
     * @return the new access token value
     * @Throw new Runtime exception if the refresh token is not valid or expired
     */
    @Override
    public String reGenerateAccessToken(String refreshTokenVal) {
        Optional<RefreshToken> byRefreshToken = refreshTokenRepository.findByValidRefreshToken(UUID.fromString(refreshTokenVal));
        return  byRefreshToken.map(refreshToken -> {
            var user = refreshToken.getUser();
            var authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return this.generateAccessToken(authorities, user.getEmail());
        }).orElseThrow(() -> new RuntimeException("failed to refresh the token..."));
    }
}
