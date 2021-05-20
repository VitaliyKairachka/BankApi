package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillRepository {
    boolean addBill(long userId);

    Optional<Bill> getBillById(long billId);

    List<Bill> getAllBillsByUser(long userId);

    double getBalanceBill(long billId);

    boolean plusBalance(long billId, double sum);

    boolean minusBalance(long billId, double sum);
}
