package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.OperationNotFoundException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

/**
 *
 */
public interface OperationService {
    /**
     *
     * @param exchange
     * @return
     * @throws BillNotFoundException
     */
    boolean addOperation(Operation operation) throws BillNotFoundException;

    /**
     *
     * @param id
     * @param login
     * @return
     * @throws BillNotFoundException
     * @throws OperationNotFoundException
     * @throws NoAccessException
     * @throws UserNotFoundException
     */
    List<Operation> getAllOperationsByBillId(long id, String login)
            throws BillNotFoundException, OperationNotFoundException, NoAccessException, UserNotFoundException;

    /**
     *
     * @return
     */
    List<Operation> getAllOperations();

    /**
     *
     * @param status
     * @return
     */
    List<Operation> getAllOperationsByStatus(String status);

    /**
     *
     * @param id
     * @param status
     * @return
     * @throws OperationNotFoundException
     */
    boolean changeStatusOperation(long id, String status) throws OperationNotFoundException;
}
