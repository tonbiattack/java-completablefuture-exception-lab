# CompletableFutureのjoinで原因例外が隠れる

本ラボは、非同期処理の失敗を `join()` で受けたとき、`CompletionException` のまま呼び出し側へ返し、原因例外を契約として扱えなくなる問題を再現します。

## 実行

```bash
mvn --batch-mode test
```

バグ状態は `f661f58` の親コミットに相当する状態で、期待する `IllegalStateException:upstream unavailable` に対して `CompletionException:java.lang.IllegalStateException: upstream unavailable` となります。修正状態は `f661f58` で、`getCause()` を辿って原因を正規化します。

## 学習の流れ

| 段階 | 観測 |
| --- | --- |
| 再現 | join結果がCompletionExceptionで始まる |
| 仮説 | 非同期処理が別の例外へ変換した |
| 切り分け | `getCause()` と `join()` の仕様を確認する |
| 修正 | CompletionExceptionのcauseをドメイン境界へ返す |

詳細は `docs/debugging-record.md` を参照してください。

## References

[1] [Java SE 21 API — CompletableFuture](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
