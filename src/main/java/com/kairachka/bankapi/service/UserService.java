package com.kairachka.bankapi.service;

import com.kairachka.bankapi.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public String create(User user) {
//        return userRepository.create(user);
//    }
}
