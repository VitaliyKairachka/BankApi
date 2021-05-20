package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.exception.NoAccessException;

import java.util.List;

public interface BillService {
    boolean addBill(long id);

    Bill getBillById(long id);

    Bill getBillByIdAndLogin(long id, String login) throws NoAccessException;

    List<Bill> getAllBillsByUser(long id);

    boolean plusBalance(long billId, double sum);

    boolean minusBalance(long billId, double sum);

    double getBalance(long billId, String login);
}
