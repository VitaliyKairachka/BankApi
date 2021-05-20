package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.service.BillService;

import java.util.List;
import java.util.Optional;

public class BillServiceImpl implements BillService {
    private final BillRepositoryImpl billRepositoryImpl = new BillRepositoryImpl();
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();

    public boolean addBill(long id) {
        return billRepositoryImpl.addBill(id);
    }

    public Bill getBillById(long id) throws BillNotFoundException {
        Optional<Bill> bill = billRepositoryImpl.getBillById(id);
        if (bill.isPresent()) {
            return bill.get();
        } else {
            throw new BillNotFoundException();
        }
    }

    @Override
    public Bill getBillByIdAndLogin(long id, String login)
            throws UserNotFoundException, NoAccessException, BillNotFoundException {
        Optional<Bill> bill = billRepositoryImpl.getBillById(id);
        User user = userServiceImpl.getUserByLogin(login);
        if (bill.isPresent()) {
            if (user.getId() == bill.get().getUserId()) {
                return bill.get();
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new BillNotFoundException();
        }
    }

    public List<Bill> getAllBillsByUser(long id) {
        return billRepositoryImpl.getAllBillsByUser(id);
    }

    public boolean plusBalance(long billId, double sum) {
        return billRepositoryImpl.plusBalance(billId, sum);
    }

    public boolean minusBalance(long billId, double sum) {
        return billRepositoryImpl.minusBalance(billId, sum);
    }

    public double getBalance(long billId, String login) throws NoAccessException, BillNotFoundException, UserNotFoundException {
        User user = userServiceImpl.getUserByLogin(login);
        Optional<Bill> bill = billRepositoryImpl.getBillById(billId);
        if (bill.isPresent()) {
            if (user.getId() == bill.get().getUserId()) {
                return billRepositoryImpl.getBalanceBill(billId);
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new BillNotFoundException();
        }
    }
}
