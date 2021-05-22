package com.kairachka.bankapi.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.UserMapper;
import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    @Mock
    UserRepositoryImpl userRepository = Mockito.mock(UserRepositoryImpl.class);
    private final UserMapper userMapper = new UserMapper();
    ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addUser() {
    }

    @Test
    void getUserIdByLogin() throws UserNotFoundException {
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.of(user));
        assertEquals(1, userService.getUserIdByLogin("login"));
    }

    @Test
    void getUserIdByLoginNull() {
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserIdByLogin("login"));
    }

    @Test
    void authentication() {
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        assertFalse(userService.authentication("login", "123"));
    }

    @Test
    void getRoleByLogin() throws UserNotFoundException {
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user).get().getRole(), userService.getRoleByLogin("login"));
    }

    @Test
    void getRoleByLoginNull() {
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getRoleByLogin("login"));
    }

    @Test
    void getUserByLogin() throws UserNotFoundException {
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user).get(), userService.getUserByLogin("login"));
    }

    @Test
    void getUserByLoginNull() {
        Mockito.when(userRepository.getUserByLogin("login")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin("login"));
    }
}