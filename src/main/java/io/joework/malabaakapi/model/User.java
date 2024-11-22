package io.joework.malabaakapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "users_generator",sequenceName ="users_id_seq" , initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "first name cannot be empty.")
    private String firstName;

    @Column(name =  "last_name", nullable = false)
    @NotEmpty(message = "last name cannot be empty.")
    private String lastName;

    @Column(name = "email", nullable = false)
    @Email(message = "must be valid email format.")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.PLAYER;

    @Column(name = "account_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountProvider accountProvider = AccountProvider.LOCAL;

    @Column(name = "is_blocked")
    private Boolean isBlocked = Boolean.FALSE;

    @Column(name = "is_enabled")
    private Boolean isEnabled = Boolean.FALSE;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(Role.PLAYER.name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    // this as the email is the account identifier that we will use
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountProvider=" + accountProvider +
                ", isBlocked=" + isBlocked +
                ", isEnabled=" + isEnabled +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                '}';
    }
}
