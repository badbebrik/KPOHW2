package org.example.repository;

public interface MoneyStorage {
    void addCash(int cash);

    void addNonCash(int nonCash);

    int getTotalMoney();

    void updateMoneyStorage();

    int getCash();

    int getNonCash();
}
