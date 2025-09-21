package com.example.async;

import java.util.concurrent.CompletableFuture;

public class AsyncService {

    public CompletableFuture<Void> sendMessageAsync() {

        return CompletableFuture.runAsync(() -> {
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }
        });
    }

}
