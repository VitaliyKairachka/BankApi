package com.kairachka.bankapi.entity;

import java.util.Objects;

public class Card {
    long id;
    long billId;

    public Card() {
    }

    public Card(long billId) {
        this.billId = billId;
    }

    public Card(long id, long billId) {
        this.id = id;
        this.billId = billId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && billId == card.billId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, billId);
    }

    @Override
    public String toString() {
        return "Card{" +
                "billId=" + billId +
                '}';
    }
}
