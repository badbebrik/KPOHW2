package org.example;

import lombok.Data;

@Data
public class MoneyStorage implements MoneyStorageI {
    private int cash;
    private int nonCash;
    private int totalMoney;

    public MoneyStorage() {
        this.cash = DataBaseHandler.getCash();
        this.nonCash = DataBaseHandler.getNonCash();
        this.totalMoney = cash + nonCash;
        updateMoneyStorage();
    }

    public void addCash(int cash) {
        this.cash += cash;
        this.totalMoney += cash;
        updateMoneyStorage();
    }

    public void addNonCash(int nonCash) {
        this.nonCash += nonCash;
        this.totalMoney += nonCash;
        updateMoneyStorage();
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void updateMoneyStorage() {
        DataBaseHandler.updateMoneyStorage(this);
    }
}
