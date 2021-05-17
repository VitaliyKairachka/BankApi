package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Bill;

import java.util.List;

public interface BillRepository {
    List<Bill> getAllBillsByUser(long userId);

    boolean addBill(long userId);
}
