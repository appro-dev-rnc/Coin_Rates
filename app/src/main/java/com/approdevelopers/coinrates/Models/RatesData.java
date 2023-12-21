package com.approdevelopers.coinrates.Models;

public class RatesData {

    private String currencyCode;
    private double rate;

    public RatesData(String currencyCode, double rate) {
        this.currencyCode = currencyCode;
        this.rate = rate;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RatesData{" +
                "currencyCode='" + currencyCode + '\'' +
                ", rate=" + rate +
                '}';
    }
}
