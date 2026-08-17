package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class AsyncFailureNormalizer {

    private AsyncFailureNormalizer() {
    }

    public static String load() {
        try {
            return CompletableFuture.<String>supplyAsync(() -> {
                throw new IllegalStateException("upstream unavailable");
            }).join();
        } catch (CompletionException exception) {
            Throwable cause = exception.getCause() == null ? exception : exception.getCause();
            return cause.getClass().getSimpleName() + ":" + cause.getMessage();
        }
    }
}
