package com.example.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AsyncServiceWithData {

    public void fetchDataAsync(Consumer<String> callback) {
        CompletableFuture.runAsync( () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                callback.accept("async data");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.accept(null);
            }
        });


    }

}
