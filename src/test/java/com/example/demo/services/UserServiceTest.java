package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;
    private RedisService redisService;

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        redisService = Mockito.mock(RedisService.class);
        userService = new UserService(userRepository, redisService);
    }

    @Test
    public void testSaveUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John Doe");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.saveUser(mockUser);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        Mockito.verify(redisService).saveUserToCache(savedUser);
    }

    @Test
    public void testGetUserById_Cached() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John Doe");

        Mockito.when(redisService.getUserFromCache(1L)).thenReturn(mockUser);

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        Mockito.verify(userRepository, Mockito.never()).findById(any());
    }

    @Test
    public void testGetUserById_Database() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John Doe");

        Mockito.when(redisService.getUserFromCache(1L)).thenReturn(null);
        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        Mockito.verify(redisService).saveUserToCache(mockUser);
    }

    @Test
    public void testGetUserById_NotFound() {
        Mockito.when(redisService.getUserFromCache(1L)).thenReturn(null);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User user = userService.getUserById(1L);

        assertNull(user);
    }
}
