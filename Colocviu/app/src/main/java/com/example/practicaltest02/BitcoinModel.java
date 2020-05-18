package com.example.practicaltest02;

public class BitcoinModel {

    private String value;

    public BitcoinModel() {
        this.value = null;
    }

    public BitcoinModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String temperature) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Bitcoin{" +
                "value='" + value + '\'' +
                '}';
    }

}
