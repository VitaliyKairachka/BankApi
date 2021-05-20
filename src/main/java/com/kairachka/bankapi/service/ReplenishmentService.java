package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.repository.ReplenishmentRepository;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class ReplenishmentService {
    ReplenishmentRepository replenishmentRepository = new ReplenishmentRepository();
    ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();
    BillService billService = new BillService();
    UserService userService = new UserService();

    public boolean addReplenishment(HttpExchange exchange) {
        Replenishment replenishment = replenishmentMapper.JsonToReplenishment(exchange);
        if (replenishment.getSum() > 0) {
            if (billService.plusBalance(replenishment.getBillId(), replenishment.getSum())) {
                return replenishmentRepository.addReplenishment(replenishment);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<Replenishment> getAllReplenishmentByBill(long id, String login) {
        User user = userService.getUserByLogin(login);
        Bill bill = billService.getBillById(id);
        if (user.getId() == bill.getUserId()) {
            return replenishmentRepository.getAllReplenishmentByBill(id);
        } else {
            throw new BillNotFoundException("Bill not found exception");
        }
    }
}
