package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Replenishment;

import java.util.List;

public interface ReplenishmentRepository {
    boolean addReplenishment(long billId);

    List<Replenishment> getAllReplenishmentByUser(long billId);
}
