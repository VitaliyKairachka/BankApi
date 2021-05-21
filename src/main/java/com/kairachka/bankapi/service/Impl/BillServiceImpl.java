package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.BillRepository;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.UserService;

import java.util.List;
import java.util.Optional;

public class BillServiceImpl implements BillService {
    private BillRepository billRepository = new BillRepositoryImpl();
    private UserService userService = new UserServiceImpl();

    public BillServiceImpl() {
    }

    public BillServiceImpl(BillRepository billRepository, UserService userService) {
        this.billRepository = billRepository;
        this.userService = userService;
    }

    @Override
    public boolean addBill(long id) {
        return billRepository.addBill(id);
    }

    @Override
    public Bill getBillById(long id) throws BillNotFoundException {
        Optional<Bill> bill = billRepository.getBillById(id);
        if (bill.isPresent()) {
            return bill.get();
        } else {
            throw new BillNotFoundException();
        }
    }

    @Override
    public Bill getBillByIdAndLogin(long id, String login)
            throws UserNotFoundException, NoAccessException, BillNotFoundException {
        Optional<Bill> bill = billRepository.getBillById(id);
        User user = userService.getUserByLogin(login);
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

    @Override
    public List<Bill> getAllBillsByUser(long id) {
        return billRepository.getAllBillsByUser(id);
    }

    @Override
    public boolean plusBalance(long billId, double sum) {
        return billRepository.plusBalance(billId, sum);
    }

    @Override
    public boolean minusBalance(long billId, double sum) {
        return billRepository.minusBalance(billId, sum);
    }

    @Override
    public double getBalance(long billId, String login)
            throws NoAccessException, BillNotFoundException, UserNotFoundException {
        User user = userService.getUserByLogin(login);
        Optional<Bill> bill = billRepository.getBillById(billId);
        if (bill.isPresent()) {
            if (user.getId() == bill.get().getUserId()) {
                return billRepository.getBalanceBill(billId);
            } else {
                throw new NoAccessException();
            }
        } else {
            throw new BillNotFoundException();
        }
    }
}
