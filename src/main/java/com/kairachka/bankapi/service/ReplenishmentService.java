package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.repository.ReplenishmentRepository;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class ReplenishmentService {
    ReplenishmentRepository replenishmentRepository = new ReplenishmentRepository();
    ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();
    BillService billService = new BillService();

    public boolean addReplenishment(HttpExchange exchange) {
        Replenishment replenishment = replenishmentMapper.JsonToReplenishment(exchange);
        if (replenishment.getSum() > 0) {
            billService.plusBalance(replenishment.getBillId(), replenishment.getSum());
            return replenishmentRepository.addReplenishment(replenishment);
        } else {
            return false;
        }

    }

    public List<Replenishment> getAllReplenishmentByBill(long id){
        return replenishmentRepository.getAllReplenishmentByBill(id);
    }


}
