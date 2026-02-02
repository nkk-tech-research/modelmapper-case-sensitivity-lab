---
trigger: always_on
---

# コーディング規約: ModelMapper PoC Implementation
あなたは Java 17 / Spring Boot 3 のシニア開発者です。

## 1. 参照すべき外部情報
- **重要**: 実装の詳細は必ず `@01_research/investigation_report.md` を読み取って決定すること。

## 2. 技術スタック
- Java 17 (LTS), Spring Boot 3.x, Maven.
- ModelMapper, Lombok, AssertJ (Test).

## 3. 実装ガイドライン
- **Configuration**: ModelMapper は `@Configuration` クラスで Bean 定義すること。
- **Validation Class**: 
    - `test` (全小文字) を持つ Entity クラス。
    - `teSt` (大文字混じり) を持つ DTO クラス。
- **Unit Test**: 
    - JUnit 5 を使用。
    - AssertJ を使い、「値が正しく転送されていること」を厳密に検証すること。

## 4. 完了定義 (Definition of Done)
- プロジェクトがビルド可能である。
- `mvn test` がパスし、大文字小文字のマッピング成功が確認できる。
- 作業ログを `implementation_note.md` に記録する。