package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.OperationStatus;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.mapper.OperationMapper;
import com.kairachka.bankapi.repository.Impl.OperationRepositoryImpl;
import com.kairachka.bankapi.service.OperationService;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Optional;

public class OperationServiceImpl implements OperationService {
    private final OperationRepositoryImpl operationRepositoryImpl = new OperationRepositoryImpl();
    private final OperationMapper operationMapper = new OperationMapper();
    private final BillServiceImpl billServiceImpl = new BillServiceImpl();
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();

    public boolean addOperation(HttpExchange exchange) {
        Operation operation = operationMapper.JsonToOperation(exchange);
        Bill bill = billServiceImpl.getBillById(operation.getSourceId());
        if (bill.getBalance() - operation.getSum() >= 0) {
            if (operationRepositoryImpl.addOperation(operation)) {
                return billServiceImpl.minusBalance(bill.getId(), operation.getSum());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<Operation> getAllOperationsByBillId(long id, String login) {
        User user = userServiceImpl.getUserByLogin(login);
        Bill bill = billServiceImpl.getBillById(id);
        if (user.getId() == bill.getUserId()) {
            List<Operation> operationList = operationRepositoryImpl.getAllOperationByBill(id);
            if (!operationList.isEmpty()) {
                return operationList;
            } else {
                throw new OperationNotFoundException("Operation not found exception");
            }
        } else {
            throw new BillNotFoundException();
        }
    }

    public List<Operation> getAllOperations() {
        return operationRepositoryImpl.getAllOperation();
    }

    public List<Operation> getAllOperationsByStatus(String status) {
        return operationRepositoryImpl.getAllOperationsByStatus(status.toUpperCase());
    }

    public boolean changeStatusOperation(long id, String status) {
        Optional<Operation> operation = operationRepositoryImpl.getOperationById(id);
        if (operation.isPresent()) {
            String statusUpdatableOperation = operation.get().getStatus();
            if (status.toUpperCase().equals(OperationStatus.APPROVED.toString())) {
                if (statusUpdatableOperation.toUpperCase().equals(OperationStatus.DECLINE.toString())) {
                    return false;
                } else {
                    operationRepositoryImpl.changeOperationStatus(id, status.toUpperCase());
                    return true;
                }
            } else if (status.toUpperCase().equals(OperationStatus.DECLINE.toString())) {
                if (statusUpdatableOperation.toUpperCase().equals(OperationStatus.APPROVED.toString())) {
                    return false;
                } else {
                    if (billServiceImpl.plusBalance(operation.get().getSourceId(), operation.get().getSum())) {
                        return operationRepositoryImpl.changeOperationStatus(id, status.toUpperCase());
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
