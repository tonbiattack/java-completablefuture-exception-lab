package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AsyncFailureNormalizerTest {

    @Test
    void 非同期処理の原因例外を安定した契約として返す() {
        String failure = AsyncFailureNormalizer.load();

        System.out.println("[evidence] failure=" + failure);

        assertEquals("IllegalStateException:upstream unavailable", failure);
    }
}
