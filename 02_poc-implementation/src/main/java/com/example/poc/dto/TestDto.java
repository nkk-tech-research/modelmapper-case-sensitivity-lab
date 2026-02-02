package com.example.poc.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    private String teSt;

    // Collision test fields
    private String xType;
    private String xTYPE;

    public String getTeSt() {
        return teSt;
    }

    public void setTeSt(String teSt) {
        this.teSt = teSt;
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
