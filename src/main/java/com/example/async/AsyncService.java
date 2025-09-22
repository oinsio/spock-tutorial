package com.example.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncService {

    public CompletableFuture<Void> sendMessageAsync() {

        return CompletableFuture.runAsync(() -> {
           try {
               TimeUnit.MILLISECONDS.sleep(500);
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }
        });
    }

}
