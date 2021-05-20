package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;

import java.util.List;

public interface BillService {
    boolean addBill(long id);

    Bill getBillById(long id) throws BillNotFoundException;

    Bill getBillByIdAndLogin(long id, String login)
            throws NoAccessException, BillNotFoundException, UserNotFoundException;

    List<Bill> getAllBillsByUser(long id);

    boolean plusBalance(long billId, double sum);

    boolean minusBalance(long billId, double sum);

    double getBalance(long billId, String login) throws NoAccessException, BillNotFoundException, UserNotFoundException;
}
