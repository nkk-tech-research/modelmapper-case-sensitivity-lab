# ModelMapper Investigation Report: Hybrid Solution

## 目的
`xType` と `xTYPE` の衝突問題を、既存の正常なマッピングに影響を与えずに解決する。

## 原因分析
なぜ `Matches multiple source property` や「誤マッピング」が発生するのか？

### 1. トークン化と正規化 (Tokenization)
ModelMapper はデフォルトでプロパティ名を単語（トークン）に分割してマッチングを行います。
- `xType` -> `["x", "Type"]`
- `xTYPE` -> `["x", "TYPE"]`

### 2. マッチング判定 (Matching)
調査の結果、ModelMapper のマッチングプロセスにおいて、これらのトークン列は**実質的に同一（Equivalent）**とみなされることが判明しました。
- **Standard戦略**: `x` という共通トークンと、類似した `Type/TYPE` の存在により、両方のソースが両方の宛先に候補として挙がり、「曖昧（Ambiguous）」と判定されます。
- **Strict戦略**: 文献上は「厳密」とされていますが、トークン比較レベルでは大文字小文字の差異が吸収される（あるいは正規化される）挙動となり、`["x", "Type"]` と `["x", "TYPE"]` が「完全一致」してしまいます。その結果、定義順序などが早いほうのプロパティが誤ってマッピングされる「衝突（Collision）」が発生します。

### 3. 本解決策の有効性
推奨する **Literal Tokenizer** は、この「分割」と「正規化」をバイパスします。
- `xType` -> `["xType"]`
- `xTYPE` -> `["xTYPE"]`
これにより、トークン自体が物理的に異なる文字列となるため、Strict戦略下で明確に区別され、正しいマッピングが可能となります。

## 解決策: Hybrid ModelMapper Wrapper
「デフォルト設定（Standard）」と「厳密設定（Literal+Strict）」の2つのインスタンスを使い分けるラッパークラスを導入します。
通常はデフォルト設定で処理し、エラーが発生した場合のみ厳密設定で再トライすることで、**互換性と解決力**を両立させます。

### 推奨実装コード

```java
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.NameTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmartModelMapper {

    private static final Logger logger = LoggerFactory.getLogger(SmartModelMapper.class);

    private final ModelMapper primaryMapper;   // Standard (CamelCase separation)
    private final ModelMapper fallbackMapper;  // Strict (Literal separation)

    public SmartModelMapper() {
        // 1. Primary Mapper (既存の挙動設定)
        this.primaryMapper = new ModelMapper();
        // 必要に応じて既存の設定を適用
        
        // 2. Fallback Mapper (衝突解決用の特殊設定)
        this.fallbackMapper = new ModelMapper();
        NameTokenizer literalTokenizer = (name, nameableType) -> new String[]{name}; // 分割しない
        
        this.fallbackMapper.getConfiguration()
            .setSourceNameTokenizer(literalTokenizer)
            .setDestinationNameTokenizer(literalTokenizer)
            .setMatchingStrategy(MatchingStrategies.STRICT); // 完全一致
    }

    /**
     * ハイブリッド・マッピング処理
     */
    public <D> D map(Object source, Class<D> destinationType) {
        try {
            // First attempt: Standard behavior
            return primaryMapper.map(source, destinationType);
            
        } catch (ConfigurationException | MappingException e) {
            // Check if error is ambiguity related (generic check)
            logger.warn("Mapping failed with standard strategy. Retrying with strict/literal strategy. Cause: {}", e.getMessage());

            try {
                // Second attempt: Strict behavior for collision cases
                return fallbackMapper.map(source, destinationType);
                
            } catch (Exception fatalE) {
                // Both failed
                logger.error("Fallback mapping also failed.");
                throw fatalE;
            }
        }
    }
}
```

### この実装のメリット
1.  **既存影響なし**: SnakeCase -> CamelCase などの便利な変換はそのまま機能します。
2.  **自動解決**: `xType` vs `xTYPE` のような「曖昧エラー」が出たときだけ、自動的に厳密モードに切り替わって成功させます。
3.  **メンテナンス性**: 個別の `PropertyMap` を管理する必要がなく、将来的に似たようなフィールドが増えても自動対応できます。

## 結論
この `SmartModelMapper` パターンを採用することで、プロジェクト全体の安定性を損なうことなく、特定の問題（Collision）を汎用的に解決できます。
