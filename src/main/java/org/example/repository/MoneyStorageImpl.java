package org.example.repository;

import lombok.Data;
import org.example.database.DataBaseHandler;

@Data
public class MoneyStorageImpl implements MoneyStorage {
    private int cash;
    private int nonCash;
    private int totalMoney;

    public MoneyStorageImpl() {
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
