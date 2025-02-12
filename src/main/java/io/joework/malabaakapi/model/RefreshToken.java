package io.joework.malabaakapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "token")
    private UUID refreshToken;
    @ManyToOne(targetEntity = User.class)
    private User user;
}
