package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Operation;

import java.util.List;
import java.util.Optional;

public interface OperationRepository {
    boolean addOperation(Operation operation);

    List<Operation> getAllOperationByBill(long billId);

    List<Operation> getAllOperation();

    List<Operation> getAllOperationsByStatus(String status);

    boolean changeOperationStatus(long operationId, String status);

    Optional<Operation> getOperationById(long operationId);
}
