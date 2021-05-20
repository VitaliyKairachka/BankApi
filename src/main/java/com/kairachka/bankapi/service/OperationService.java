package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.OperationStatus;
import com.kairachka.bankapi.exception.BillNotFoundException;
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
            if (operationRepository.addOperation(operation)) {
                return billService.minusBalance(bill.getId(), operation.getSum());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<Operation> getAllOperationsByBillId(long id, String login) {
        User user = userService.getUserByLogin(login);
        Bill bill = billService.getBillById(id);
        if (user.getId() == bill.getUserId()) {
            List<Operation> operationList = operationRepository.getAllOperationByBill(id);
            if (!operationList.isEmpty()) {
                return operationList;
            } else {
                throw new OperationNotFoundException("Operation not found exception");
            }
        } else {
            throw new BillNotFoundException("Bill not found exception");
        }
    }

    public List<Operation> getAllOperations() {
        return operationRepository.getAllOperation();
    }

    public List<Operation> getAllOperationsByStatus(String status) {
        return operationRepository.getAllOperationsByStatus(status.toUpperCase());
    }

    public boolean changeStatusOperation(long id, String status) {
        Optional<Operation> operation = operationRepository.getOperationById(id);
        if (operation.isPresent()) {
            String statusUpdatableOperation = operation.get().getStatus();
            if (status.toUpperCase().equals(OperationStatus.APPROVED.toString())) {
                if (statusUpdatableOperation.toUpperCase().equals(OperationStatus.DECLINE.toString())) {
                    return false;
                } else {
                    operationRepository.changeOperationStatus(id, status.toUpperCase());
                    return true;
                }
            } else if (status.toUpperCase().equals(OperationStatus.DECLINE.toString())) {
                if (statusUpdatableOperation.toUpperCase().equals(OperationStatus.APPROVED.toString())) {
                    return false;
                } else {
                    if (billService.plusBalance(operation.get().getSourceId(), operation.get().getSum())) {
                        return operationRepository.changeOperationStatus(id, status.toUpperCase());
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            throw new OperationNotFoundException("Operation not found exception");
        }
    }
}
