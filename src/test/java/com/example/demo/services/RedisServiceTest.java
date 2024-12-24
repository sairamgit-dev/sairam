package com.example.demo.services;

import com.example.demo.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testSaveAndRetrieveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        redisService.saveUserToCache(user);
        User cachedUser = redisService.getUserFromCache(1L);

        assert cachedUser != null && cachedUser.getName().equals("John Doe");
    }
}
