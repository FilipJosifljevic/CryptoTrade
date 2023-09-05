package com.example.cryptotrade;

public class CryptoModel {
    private String coinName;
    private String symbol;
    private double price;

    public CryptoModel(String coinName, String symbol, double price) {
        this.coinName = coinName;
        this.symbol = symbol;
        this.price = price;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

