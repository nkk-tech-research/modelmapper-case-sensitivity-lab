package com.example.poc.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    private String test;

    // Collision test fields
    private String xType;
    private String xTYPE;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getXType() {
        return xType;
    }

    public void setXType(String xType) {
        this.xType = xType;
    }

    public String getXTYPE() {
        return xTYPE;
    }

    public void setXTYPE(String xTYPE) {
        this.xTYPE = xTYPE;
    }
}
