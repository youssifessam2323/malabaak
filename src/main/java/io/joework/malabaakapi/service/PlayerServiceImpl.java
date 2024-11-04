package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.Player;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.repository.PlayerRepository;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import io.joework.malabaakapi.util.VerificationLinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final VerificationLinkRepository verificationLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;

    @Override
    public Player savePlayer(Player player) {
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player);
    }

    @Override
    public boolean enablePlayer(String token){

        Optional<VerificationLink> verificationLinkOptional = verificationLinkRepository.findByVerificationToken(UUID.fromString(token));
        if(verificationLinkOptional.isEmpty()){
            return false;
        }
        VerificationLink verificationLink = verificationLinkOptional.get();
        if(!VerificationLinkUtil.checkIfLinkExpired(verificationLink)){
            verificationLinkRepository.delete(verificationLink);
            return false;
        }
        verificationLink.getPlayer().setIsEnabled(true);
        verificationLinkRepository.delete(verificationLink);
        return true;
    }

    @Override
    public Optional<Player> checkPlayerExists(String email) {
        return playerRepository.findByEmail(email);
    }


}
