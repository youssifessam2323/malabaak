package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.Player;

import java.util.Optional;

public interface PlayerService {
    Player savePlayer(Player player);
    boolean enablePlayer(String uuid);

    Optional<Player> checkPlayerExists(String email);
}
