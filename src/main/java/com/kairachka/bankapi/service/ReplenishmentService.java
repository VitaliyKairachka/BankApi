package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public interface ReplenishmentService {
    boolean addReplenishment(HttpExchange exchange);

    List<Replenishment> getAllReplenishmentByBill(long id, String login)
            throws BillNotFoundException, NoAccessException, UserNotFoundException;
}
