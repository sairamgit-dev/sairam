package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    // Constructor-based dependency injection
    @Autowired
    public UserService(UserRepository userRepository, RedisService redisService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        redisService.saveUserToCache(savedUser);
        return savedUser;
    }

    public User getUserById(Long userId) {
        User cachedUser = redisService.getUserFromCache(userId);
        if (cachedUser != null) {
            return cachedUser;
        }

        return userRepository.findById(userId).map(user -> {
            redisService.saveUserToCache(user);
            return user;
        }).orElse(null);
    }

    public User updateUser(Long userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            User savedUser = userRepository.save(user);
            redisService.saveUserToCache(savedUser);
            return savedUser;
        }).orElse(null);
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            redisService.deleteUserFromCache(userId);
            return true;
        }
        return false;
    }
}
