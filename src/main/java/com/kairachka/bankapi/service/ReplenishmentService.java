package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Replenishment;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public interface ReplenishmentService {
    boolean addReplenishment(HttpExchange exchange);

    List<Replenishment> getAllReplenishmentByBill(long id, String login);
}
