package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.entity.Card;
import com.kairachka.bankapi.entity.Replenishment;
import com.kairachka.bankapi.entity.User;
import com.kairachka.bankapi.enums.Role;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.Impl.ReplenishmentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReplenishmentServiceImplTest {
    @Mock
    ReplenishmentRepositoryImpl replenishmentRepository;
    @Mock
    BillServiceImpl billService;
    @Mock
    UserServiceImpl userService;
    @InjectMocks
    ReplenishmentServiceImpl replenishmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addReplenishment() {
    }

    @Test
    void getAllReplenishmentByBill() throws BillNotFoundException, UserNotFoundException, NoAccessException {
        Bill bill = new Bill(1, 1, 0, 1);
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        List<Replenishment> replenishmentList = new ArrayList<>();
        replenishmentList.add(new Replenishment());
        replenishmentList.add(new Replenishment());
        Mockito.when(billService.getBillById(1)).thenReturn(Optional.of(bill).get());
        Mockito.when(userService.getUserByLogin("login")).thenReturn(user);
        Mockito.when(replenishmentRepository.getAllReplenishmentByBill(1)).thenReturn(replenishmentList);
        assertEquals(replenishmentList, replenishmentService.getAllReplenishmentByBill(1, "login"));
    }

    @Test
    void getAllReplenishmentByBillNoAccess() throws BillNotFoundException, UserNotFoundException, NoAccessException {
        Bill bill = new Bill(1, 1, 0, 2);
        User user = new User(
                1, "login", "p", "f", "l",
                "m", "pa", "mb", Role.USER);
        List<Replenishment> replenishmentList = new ArrayList<>();
        replenishmentList.add(new Replenishment());
        replenishmentList.add(new Replenishment());
        Mockito.when(billService.getBillById(1)).thenReturn(Optional.of(bill).get());
        Mockito.when(userService.getUserByLogin("login")).thenReturn(user);
        Mockito.when(replenishmentRepository.getAllReplenishmentByBill(1)).thenReturn(replenishmentList);
        assertThrows(NoAccessException.class, () -> replenishmentService.getAllReplenishmentByBill(1, "login"));
    }
}