package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.OperationStatus;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.mapper.OperationMapper;
import com.kairachka.bankapi.repository.OperationRepository;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Optional;

public class OperationService {
    OperationRepository operationRepository = new OperationRepository();
    OperationMapper operationMapper = new OperationMapper();
    BillService billService = new BillService();
    UserService userService = new UserService();

    public boolean addOperation(HttpExchange exchange) {
        Operation operation = operationMapper.JsonToOperation(exchange);
        Bill bill = billService.getBillById(operation.getSourceId());
        if (bill.getBalance() - operation.getSum() >= 0) {
            billService.minusBalance(bill.getId(), operation.getSum());
            return operationRepository.addOperation(operation);
        } else {
            return false;
        }
    }

    public List<Operation> getAllOperationsByBillId(long id) {
        return operationRepository.getAllOperationByBill(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.getAllOperation();
    }

    public List<Operation> getAllOperationsByStatus(String status) {
        return operationRepository.getAllOperationsByStatus(status);
    }

    public boolean changeStatusOperation(long id, String status, String login) {
        User user = userService.getUserByLogin(login);
        Optional<Operation> operation = operationRepository.getOperationById(id);
        if (status.equals(OperationStatus.APPROVED.toString())) {
            operationRepository.changeOperationStatus(id, status, user.getId());
            return true;
        } else if (status.equals(OperationStatus.DECLINE.toString())) {
            if (operation.isPresent()) {
                billService.plusBalance(operation.get().getSourceId(), operation.get().getSum());
                return operationRepository.changeOperationStatus(id, status, user.getId());
            } else {
                throw new OperationNotFoundException("Operation not found exception");
            }
        } else {
            return false;
        }
    }
}
