package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public interface OperationService {
    boolean addOperation(HttpExchange exchange) throws BillNotFoundException;

    List<Operation> getAllOperationsByBillId(long id, String login)
            throws BillNotFoundException, OperationNotFoundException, NoAccessException, UserNotFoundException;

    List<Operation> getAllOperations();

    List<Operation> getAllOperationsByStatus(String status);

    boolean changeStatusOperation(long id, String status) throws OperationNotFoundException;
}
