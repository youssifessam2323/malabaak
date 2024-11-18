package io.joework.malabaakapi.service;

import io.joework.malabaakapi.model.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    boolean enableUser(String uuid);

    Optional<User> checkUserExists(String email);
}
