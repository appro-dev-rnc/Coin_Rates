package com.approdevelopers.coinrates.Models;

import java.util.Map;

public class CoinsDataList {

    private boolean success;
    private Map<String,CryptoItem> crypto;

    public CoinsDataList() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, CryptoItem> getCrypto() {
        return crypto;
    }

    @Override
    public String toString() {
        return "CoinsDataList{" +
                "success=" + success +
                ", crypto=" + crypto +
                '}';
    }

    public void setCrypto(Map<String, CryptoItem> crypto) {
        this.crypto = crypto;
    }

    public class CryptoItem{
        private String symbol;
        private String name;
        private String name_full;
        private String max_supply;
        private String icon_url;

        public CryptoItem() {
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_full() {
            return name_full;
        }

        public void setName_full(String name_full) {
            this.name_full = name_full;
        }

        public String getMax_supply() {
            return max_supply;
        }

        public void setMax_supply(String max_supply) {
            this.max_supply = max_supply;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        @Override
        public String toString() {
            return "CryptoItem{" +
                    "symbol='" + symbol + '\'' +
                    ", name='" + name + '\'' +
                    ", name_full='" + name_full + '\'' +
                    ", max_supply=" + max_supply +
                    ", icon_url='" + icon_url + '\'' +
                    '}';
        }
    }
}
