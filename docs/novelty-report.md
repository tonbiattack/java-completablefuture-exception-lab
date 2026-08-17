# 新規性レポート

Repository Catalogは環境内に存在しないため、自動スクリーニングは未実施である。Qiita全体をCompletableFuture、CompletionException、join、ExecutionException、例外ラップで検索し、該当記事は確認できなかった。

既存ラボのSpring非同期通知とは異なり、本ラボはSpringやコミット境界を扱わず、Java標準CompletableFutureの `join()` と `get()` の例外ラップ差を、同期的なエラー契約として観測する。
