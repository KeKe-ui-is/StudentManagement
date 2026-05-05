# 受講生管理システム　[![Java CD with Gradle](https://github.com/KeKe-ui-is/StudentManagement/actions/workflows/gradle.yml/badge.svg)](https://github.com/KeKe-ui-is/StudentManagement/actions/workflows/gradle.yml)

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

## CI/CDの設定

CDを実行するために、GitHub ActionsのRepository Secretsに以下を設定します。

| Secret名 | 用途 |
|---|---|
| EC2_HOST | デプロイ先EC2のホスト名またはIPアドレス |
| EC2_USER | EC2にSSH接続するユーザー名 |
| EC2_SSH_KEY | EC2へ接続するための秘密鍵 |
| EC2_PORT | SSH接続ポート |

## OpenAPI(ローカル環境)

url : http://localhost:8080/swagger-ui.html

## ローカル環境での実行方法

実行 : ./gradlew bootRun

テスト実行 : ./gradlew test
