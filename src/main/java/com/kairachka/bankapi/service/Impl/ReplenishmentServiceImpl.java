package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.repository.Impl.ReplenishmentRepositoryImpl;
import com.kairachka.bankapi.service.ReplenishmentService;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class ReplenishmentServiceImpl implements ReplenishmentService {
    private final ReplenishmentRepositoryImpl replenishmentRepositoryImpl = new ReplenishmentRepositoryImpl();
    private final ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();
    private final BillServiceImpl billServiceImpl = new BillServiceImpl();
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();

    public boolean addReplenishment(HttpExchange exchange) {
        Replenishment replenishment = replenishmentMapper.JsonToReplenishment(exchange);
        if (replenishment.getSum() > 0) {
            if (billServiceImpl.plusBalance(replenishment.getBillId(), replenishment.getSum())) {
                return replenishmentRepositoryImpl.addReplenishment(replenishment);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<Replenishment> getAllReplenishmentByBill(long id, String login) {
        User user = userServiceImpl.getUserByLogin(login);
        Bill bill = billServiceImpl.getBillById(id);
        if (user.getId() == bill.getUserId()) {
            return replenishmentRepositoryImpl.getAllReplenishmentByBill(id);
        } else {
            throw new BillNotFoundException();
        }
    }
}
