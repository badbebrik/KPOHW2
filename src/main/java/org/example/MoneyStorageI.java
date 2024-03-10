package org.example;

public interface MoneyStorageI {
    void addCash(int cash);

    void addNonCash(int nonCash);

    int getTotalMoney();

    void updateMoneyStorage();
}
