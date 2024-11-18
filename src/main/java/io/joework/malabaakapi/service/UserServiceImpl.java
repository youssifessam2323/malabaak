package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.repository.UserRepository;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerificationLinkRepository verificationLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean enableUser(String token){

        Optional<VerificationLink> verificationLinkOptional = verificationLinkRepository.findByVerificationToken(UUID.fromString(token));
        if(verificationLinkOptional.isEmpty()){
            return false;
        }
        VerificationLink verificationLink = verificationLinkOptional.get();
        if(!VerificationLinkUtil.checkIfLinkExpired(verificationLink)){
            verificationLinkRepository.delete(verificationLink);
            return false;
        }
        verificationLink.getUser().setIsEnabled(true);
        verificationLinkRepository.delete(verificationLink);
        return true;
    }

    @Override
    public Optional<User> checkUserExists(String email) {
        return userRepository.findByEmail(email);
    }


}
