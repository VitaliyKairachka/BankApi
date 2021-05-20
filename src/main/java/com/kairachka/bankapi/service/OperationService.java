package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Operation;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public interface OperationService {
    boolean addOperation(HttpExchange exchange);

    List<Operation> getAllOperationsByBillId(long id, String login);

    List<Operation> getAllOperations();

    List<Operation> getAllOperationsByStatus(String status);

    boolean changeStatusOperation(long id, String status);
}
