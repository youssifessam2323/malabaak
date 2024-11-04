package io.joework.malabaakapi.repository;

import io.joework.malabaakapi.model.VerificationLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationLinkRepository extends JpaRepository<VerificationLink, Long> {
    Optional<VerificationLink> findByVerificationToken(UUID token);
}
