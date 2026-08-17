# デバッグ記録

## 再現

バグ状態で `mvn --batch-mode test` を実行すると、`expected: <IllegalStateException:upstream unavailable> but was: <CompletionException:java.lang.IllegalStateException: upstream unavailable>` となる。

## 観測

非同期処理本体は `IllegalStateException("upstream unavailable")` を投げている。`join()` の呼び出し側で観測した文字列は `CompletionException` で始まり、原因のメッセージが内側にある。

## 仮説比較

| 仮説 | 実験 | 結果 |
| --- | --- | --- |
| 非同期処理は別の原因を発生させた | supplier内の例外型を確認する | IllegalStateExceptionのため棄却 |
| joinが失敗をCompletionExceptionでラップする | `getCause()` を確認する | causeが元のIllegalStateExceptionで採用 |
| テストが例外型を過度に固定している | 呼び出し側の公開契約を文字列で確認する | 原因型を返す契約として妥当 |

## 原因

CompletableFutureは例外完了時、`join()` ではCompletionExceptionを直接送出する。一方、`get()` はExecutionExceptionを送出する契約である。[1] バグ状態は `join()` のラッパーをそのまま公開契約へ変換していた。

## 最小修正

`CompletionException#getCause()` を確認し、causeがあれば原因例外の型とメッセージを返す。修正コミットは `f661f58` である。

## 再発防止テスト

元のテストは `IllegalStateException:upstream unavailable` を確認する。修正後は `failure=IllegalStateException:upstream unavailable`、`Tests run: 1, Failures: 0, Errors: 0` となる。

## References

[1] [Java SE 21 API — CompletableFuture](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
