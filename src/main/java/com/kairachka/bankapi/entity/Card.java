package com.kairachka.bankapi.entity;

import java.util.Objects;

public class Card {
    long id;
    long billId;
    long number;

    public Card(long billId, long number) {
        this.billId = billId;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && billId == card.billId && number == card.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, billId, number);
    }
}
