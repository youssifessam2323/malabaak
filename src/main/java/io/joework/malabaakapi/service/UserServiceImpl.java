package io.joework.malabaakapi.service;

import io.joework.malabaakapi.exception.UserExistsException;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.repository.UserRepository;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerificationLinkRepository verificationLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     *
     * @param user the new user that need to be created
     * @return the new created user
     */
    @Override
    public User save(User user) {
        if(Objects.isNull(user)){
            throw new IllegalArgumentException("user is null");
        }

        if(checkUserExists(user.getEmail()).isPresent()) {
            log.info("user already exists");
            throw new UserExistsException("user already exists");
        }

        log.info("creating new User account: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     *
     * @param token: the token that was sent to the user to be verified
     * @return true if the user is enabled
     */
    @Override
    public boolean enableUser(String token){
        if(Objects.isNull(token)){
            throw new IllegalArgumentException("token is null");
        }
        Optional<VerificationLink> verificationLinkOptional = verificationLinkRepository.findByVerificationToken(UUID.fromString(token));
        if(verificationLinkOptional.isEmpty()){
            return false;
        }
        VerificationLink verificationLink = verificationLinkOptional.get();
        if(!VerificationLinkUtil.isLinkExpired(verificationLink)){
            verificationLinkRepository.delete(verificationLink);
            return false;
        }
        verificationLink.getUser().setIsEnabled(true);
        verificationLinkRepository.delete(verificationLink);
        return true;
    }

    /**
     *
     * @param email user's email
     * @return an optional of the result
     */
    @Override
    public Optional<User> checkUserExists(String email) {
        return userRepository.findByEmail(email);
    }


}
