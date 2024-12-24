package com.example.demo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelTaskServiceTest {

    private final ParallelTaskService parallelTaskService = new ParallelTaskService();

    @AfterEach
    public void tearDown() {
        parallelTaskService.shutdown();
    }

    @Test
    public void testExecuteAsync() throws Exception {
        AtomicBoolean taskExecuted = new AtomicBoolean(false);

        parallelTaskService.executeAsync(() -> taskExecuted.set(true)).get();

        assertTrue(taskExecuted.get(), "The task should have been executed.");
    }
}

