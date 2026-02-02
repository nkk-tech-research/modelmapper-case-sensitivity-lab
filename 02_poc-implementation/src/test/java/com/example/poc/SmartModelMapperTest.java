package com.example.poc;

import com.example.poc.config.SmartModelMapper;
import com.example.poc.dto.TestDto;
import com.example.poc.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmartModelMapperTest {

    @Autowired
    private SmartModelMapper smartModelMapper;

    @Test
    void testCollisionResolution() {
        // Arrange
        TestEntity source = new TestEntity();
        source.setTest("NormalTestValue");
        source.setXType("TypeLower");
        source.setXTYPE("TYPE_UPPER");

        // Act
        TestDto result = smartModelMapper.map(source, TestDto.class);

        // Assert
        // 1. Verify Collision Fields are mapped correctly (Case Sensitivity check)
        assertThat(result.getXType()).isEqualTo("TypeLower");
        assertThat(result.getXTYPE()).isEqualTo("TYPE_UPPER");

        // 2. Verify 'test' -> 'teSt' mapping.
        // Note: Standard ModelMapper might map 'test' to 'teSt' if it considers them
        // close enough or matching.
        // If strict fallback is used, 'test' != 'teSt', so it might be null if fallback
        // is triggered.
        // Let's observe the behavior. If standard mapper worked for the rest but failed
        // on collision,
        // we transitioned to fallback.
        // If fallback (Strict + Literal) is used:
        // Source 'test' vs Dest 'teSt'. Literal match? No.

        System.out.println("Mapped 'teSt' value: " + result.getTeSt());
    }
}
