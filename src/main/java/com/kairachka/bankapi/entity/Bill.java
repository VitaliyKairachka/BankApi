package com.kairachka.bankapi.entity;

import java.util.Objects;

public class Bill {
    long id;
    long balance;
    long userId;

    public Bill() {
    }

    public Bill(long id, long balance, long userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public Bill(long balance, long userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return id == bill.id && balance == bill.balance && userId == bill.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, userId);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "balance=" + balance +
                ", userId=" + userId +
                '}';
    }
}
