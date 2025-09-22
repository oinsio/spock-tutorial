package com.example.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AsyncMultiService {

    public void fetchUserAsync(Consumer<String> callback) {

        CompletableFuture.runAsync(() -> {
           try {
               TimeUnit.MILLISECONDS.sleep(300);
               callback.accept("Alice");
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               callback.accept(null);
           }
        });
    }

    public void fetchOrderAsync(Consumer<String> callback) {

        CompletableFuture.runAsync(() -> {
           try {
               TimeUnit.MILLISECONDS.sleep(500);
               callback.accept("#123");
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               callback.accept(null);
           }
        });
    }
}
