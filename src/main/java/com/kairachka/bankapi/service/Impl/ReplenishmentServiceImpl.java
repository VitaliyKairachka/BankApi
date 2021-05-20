package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.mapper.ReplenishmentMapper;
import com.kairachka.bankapi.repository.Impl.ReplenishmentRepositoryImpl;
import com.kairachka.bankapi.repository.ReplenishmentRepository;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.ReplenishmentService;
import com.kairachka.bankapi.service.UserService;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class ReplenishmentServiceImpl implements ReplenishmentService {
    private final ReplenishmentRepository replenishmentRepository = new ReplenishmentRepositoryImpl();
    private final ReplenishmentMapper replenishmentMapper = new ReplenishmentMapper();
    private final BillService billService = new BillServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
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

    @Override
    public List<Replenishment> getAllReplenishmentByBill(long id, String login)
            throws BillNotFoundException, NoAccessException, UserNotFoundException {
        User user = userService.getUserByLogin(login);
        Bill bill = billService.getBillById(id);
        if (user.getId() == bill.getUserId()) {
            return replenishmentRepository.getAllReplenishmentByBill(id);
        } else {
            throw new NoAccessException();
        }
    }
}
