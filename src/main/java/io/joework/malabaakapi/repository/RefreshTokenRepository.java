package io.joework.malabaakapi.repository;

import io.joework.malabaakapi.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUser_Id(Integer id);

    @Query(value = "select rt from RefreshToken rt where rt.refreshToken = :refreshToken and rt.expiresAt > current_date")
    Optional<RefreshToken> findByValidRefreshToken(UUID refreshToken);
}
