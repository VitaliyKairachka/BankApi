package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.repository.BillRepository;

import java.util.List;
import java.util.Optional;

public class BillService {
    BillRepository billRepository = new BillRepository();
    UserService userService = new UserService();

    public boolean addBill(long id) {
        return billRepository.addBill(id);
    }

    public Bill getBillById(long id) {
        Optional<Bill> bill = billRepository.getBillById(id);
        if (bill.isPresent()) {
            return bill.get();
        } else {
            throw new BillNotFoundException("Bill not found exception");
        }
    }

    public List<Bill> getAllBillsByUser(long id) {
        return billRepository.getAllBillsByUser(id);
    }

    public boolean plusBalance(long billId, double sum) {
        return billRepository.plusBalance(billId, sum);
    }

    public boolean minusBalance(long billId, double sum) {
        return billRepository.minusBalance(billId, sum);
    }

    public double getBalance(long billId, String login) {
        User user = userService.getUserByLogin(login);
        Optional<Bill> bill = billRepository.getBillById(billId);
        if (bill.isPresent()) {
            if (user.getId() == bill.get().getUserId()) {
                return billRepository.getBalanceBill(billId);
            } else {
                throw new BillNotFoundException("Bill not found exception");
            }
        } else {
            throw new BillNotFoundException("Bill not found exception");
        }
    }
}
