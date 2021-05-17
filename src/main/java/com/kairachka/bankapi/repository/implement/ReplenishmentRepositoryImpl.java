package com.kairachka.bankapi.repository.implement;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.repository.ReplenishmentRepository;

import java.util.List;

public class ReplenishmentRepositoryImpl implements ReplenishmentRepository {
    @Override
    public boolean addReplenishment(long billId) {
        return false;
    }

    @Override
    public List<Replenishment> getAllReplenishmentByUser(long billId) {
        return null;
    }
}
