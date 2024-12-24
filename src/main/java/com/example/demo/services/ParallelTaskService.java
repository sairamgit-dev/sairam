package com.example.demo.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ParallelTaskService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * Executes a task asynchronously.
     *
     * @param task A Runnable task to execute.
     * @return A CompletableFuture representing the task's execution.
     */
    public CompletableFuture<Void> executeAsync(Runnable task) {
        return CompletableFuture.runAsync(task, executorService);
    }

    /**
     * Shutdown the executor service gracefully.
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
