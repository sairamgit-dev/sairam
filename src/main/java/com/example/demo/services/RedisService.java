package com.example.demo.services;

import com.example.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String USER_KEY_PREFIX = "user:";

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public void saveUserToCache(User user) {
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user);
    }

    public User getUserFromCache(Long userId) {
        return redisTemplate.opsForValue().get(USER_KEY_PREFIX + userId);
    }

    public void removeUserFromCache(Long userId) {
        redisTemplate.delete(USER_KEY_PREFIX + userId);
    }

    public void deleteUserFromCache(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }
    
}

