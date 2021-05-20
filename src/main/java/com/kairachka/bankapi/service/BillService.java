package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;

import java.util.List;

/**
 *
 */
public interface BillService {
    /**
     *
     * @param id
     * @return
     */
    boolean addBill(long id);

    /**
     *
     * @param id
     * @return
     * @throws BillNotFoundException
     */
    Bill getBillById(long id) throws BillNotFoundException;

    /**
     *
     * @param id
     * @param login
     * @return
     * @throws NoAccessException
     * @throws BillNotFoundException
     * @throws UserNotFoundException
     */
    Bill getBillByIdAndLogin(long id, String login)
            throws NoAccessException, BillNotFoundException, UserNotFoundException;

    /**
     *
     * @param id
     * @return
     */
    List<Bill> getAllBillsByUser(long id);

    /**
     *
     * @param billId
     * @param sum
     * @return
     */
    boolean plusBalance(long billId, double sum);

    /**
     *
     * @param billId
     * @param sum
     * @return
     */
    boolean minusBalance(long billId, double sum);

    /**
     *
     * @param billId
     * @param login
     * @return
     * @throws NoAccessException
     * @throws BillNotFoundException
     * @throws UserNotFoundException
     */
    double getBalance(long billId, String login) throws NoAccessException, BillNotFoundException, UserNotFoundException;
}
