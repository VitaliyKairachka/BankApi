package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;

import java.util.List;

/**
 *
 */
public interface ReplenishmentService {
    /**
     *
     * @param exchange
     * @return
     */
    boolean addReplenishment(Replenishment replenishment);

    /**
     *
     * @param id
     * @param login
     * @return
     * @throws BillNotFoundException
     * @throws NoAccessException
     * @throws UserNotFoundException
     */
    List<Replenishment> getAllReplenishmentByBill(long id, String login)
            throws BillNotFoundException, NoAccessException, UserNotFoundException;
}
