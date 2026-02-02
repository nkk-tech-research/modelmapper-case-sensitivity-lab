# 調査ガイドライン: ModelMapper Collision & Case Sensitivity

あなたは Java 17 / Spring Boot 開発のエキスパート調査員です。
以前の調査方針は破棄し、以下の事象解決に全力を注いでください。

## 1. ターゲット環境（厳守）
- **Spring Framework**: 6.0.3
- **ModelMapper**: 3.1.1
- **Java**: 17

## 2. 解決すべき事象 (Corrected Problem Statement)

### シナリオ: 変数名の衝突 (`xType` vs `xTYPE`)
ソース・宛先のクラスに以下の2つのプロパティが存在する。
- `xType`
- `xTYPE`

### 発生している現象
1. **Default (STANDARD戦略)**:
   - `matches multiple source property` エラーが発生する（曖昧性エラー）。
2. **STRICT戦略**:
   - エラーは消えるが、**値のマッピングが不正**になる。
   - 例: `dest.xType` に `source.xTYPE` の値が入る、あるいは両方に同じ値が入るなど。

## 3. 調査ミッション
この「STRICTでも正しく分離できない」原因を特定し、両者を厳密に区別して正しくマッピングさせるための設定を特定すること。

### 仮説・着眼点
- **JavaBeans Naming Convention**: `getxType` vs `getxTYPE` の Introspector 解釈の違い。
- **Tokenizer**: ModelMapper がこれをどうトークン分割しているか。
- **Configuration**: Tokenizer, NamingConvention, MatchingStrategy の組み合わせで解決可能か。

## 4. 成果物 (investigation_report.md)
以下の構成でレポートを再作成すること。
- **# 事象再現**: 確認された挙動の正確な記述。
- **# 原因分析**: なぜStrictでも混ざるのか？（最重要）
- **# 解決策**: コードベースで検証済みの設定。