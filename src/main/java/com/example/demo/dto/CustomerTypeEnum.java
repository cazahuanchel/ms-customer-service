package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerTypeEnum {
    PERSONAL("PERSONAL"),
    ENTERPRISE("ENTERPRISE");

    private final String value;

    CustomerTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}