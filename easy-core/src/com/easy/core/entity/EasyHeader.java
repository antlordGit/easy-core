package com.easy.core.entity;

public class EasyHeader {
    private String name;
    private String value;

    public EasyHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
