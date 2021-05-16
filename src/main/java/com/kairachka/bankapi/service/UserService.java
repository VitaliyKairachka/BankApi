package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.repository.UserRepository;

public class UserService {
    private  UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.addUser(user);
    }
}
