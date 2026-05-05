# 受講生管理システム

## 概要

受講生情報と受講コース情報を管理するためのWebアプリケーションです。  
受講生の登録・検索・更新・削除、受講コース情報の管理を行うことができます。

RaiseTech Javaフルコースの学習課題として作成し、  
Java / Spring Boot / MyBatis / MySQL / REST API / テスト / CI/CD / AWSデプロイ を一通り学習することを目的としています。

---

## 使用技術

| 分類 | 技術 |
|---|---|
| 言語 | Java 21 |
| フレームワーク | Spring Boot |
| DB | MySQL |
| APIドキュメント | springdoc-openapi / Swagger UI |
| テスト | JUnit 5, Mockito, MockMvc, H2 Database |
| ビルドツール | Gradle |
| CI | GitHub Actions |
| CD / デプロイ | GitHub Actions |
| 実行環境 | AWS EC2 Amazon Linux 2023 |

---

## 主な機能

- 受講生一覧の取得
- 受講生詳細の取得
- 受講生の新規登録
- 受講生情報の更新
- 受講生の論理削除
- 複数条件による受講生検索
- 受講コース情報の登録
- バリデーションによる入力チェック
- 例外発生時のエラーレスポンス返却
- Swagger UIによるAPI仕様確認
- GitHub Actionsによるテスト・ビルド・デプロイ自動化

---

## OpenAPI(ローカル環境)

url : http://localhost:8080/swagger-ui.html

## ローカル環境での実行方法

実行 : ./gradlew bootRun

テスト実行 : ./gradlew test
