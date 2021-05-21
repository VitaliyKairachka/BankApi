package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.exception.BillNotFoundException;
import com.kairachka.bankapi.exception.NoAccessException;
import com.kairachka.bankapi.exception.UserNotFoundException;
import com.kairachka.bankapi.repository.Impl.BillRepositoryImpl;

import com.kairachka.bankapi.repository.Impl.UserRepositoryImpl;
import com.kairachka.bankapi.service.BillService;
import com.kairachka.bankapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

public class BillServiceImplTest {
    @Mock
    BillRepositoryImpl billRepository = Mockito.mock(BillRepositoryImpl.class);

    @Mock
    UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);

    BillService billService = new BillServiceImpl(billRepository, userService);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addBill() {
        Mockito.when(billRepository.addBill(1)).thenReturn(true);
        assertTrue(billService.addBill(1));
    }

    @Test
    public void getBillById() {
        Bill bill = new Bill(1, 1, 0, 1);
        Mockito.when(billRepository.getBillById(1)).thenReturn(Optional.of(bill));
        try {
            assertEquals(bill, billService.getBillById(1));
        } catch (BillNotFoundException e) {
            e.printStackTrace();
        }
        Mockito.when(billRepository.getBillById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BillNotFoundException.class, () -> billService.getBillById(2));
    }

    @Test
    public void getBillByIdAndLogin() {
        Bill bill = new Bill(1, 1, 0, 1);
        Mockito.when(billRepository.getBillById(1)).thenReturn(Optional.of(bill));
        try {
            assertEquals(bill, billService.getBillByIdAndLogin(1, "login"));
        } catch (UserNotFoundException | BillNotFoundException | NoAccessException e) {
            e.printStackTrace();
        }
        Mockito.when(billRepository.getBillById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BillNotFoundException.class, () -> billService.getBillById(2));
    }

    @Test
    public void getAllBillsByUser() {
    }

    @Test
    public void plusBalance() {
    }

    @Test
    public void minusBalance() {
    }

    @Test
    public void getBalance() {
    }
}