package io.joework.malabaakapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "verification_links")
@Setter
@Getter
public class VerificationLink {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_link_generator")
    @SequenceGenerator(name = "verification_links_id_seq",sequenceName = "verification_links_id_seq", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "verification_token")
    private UUID verificationToken = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "expired_at")
    private Instant expiredAt;
}
