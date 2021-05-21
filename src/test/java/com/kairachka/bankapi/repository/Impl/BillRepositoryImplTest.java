package com.kairachka.bankapi.repository.Impl;

import com.kairachka.bankapi.entity.Bill;
import com.kairachka.bankapi.repository.BillRepository;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BillRepositoryImplTest {
    private final BillRepository billRepository = new BillRepositoryImpl();
    private ResultSet resultSet;
    private static final String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String createTable = "src/test/resources/SQLScripts/Bill/CreateTableBill";
    private static final String deleteTable = "src/test/resources/SQLScripts/Bill/DeleteTableBIll";

    @Test
    public void addBill() {
        try {
            Connection connection = DriverManager.getConnection(url);
            billRepository.setUrl(url);
            RunScript.execute(connection, new FileReader(createTable));
            assertTrue(billRepository.addBill(1));
            RunScript.execute(connection, new FileReader(deleteTable));
            assertFalse(billRepository.addBill(1));
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBillById() {
        try {
            Connection connection = DriverManager.getConnection(url);
            billRepository.setUrl(url);
            RunScript.execute(connection, new FileReader(createTable));
            PreparedStatement addBill = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            addBill.execute();
            PreparedStatement getBill = connection.prepareStatement("SELECT * FROM BILLS WHERE ID = 1");
            resultSet = getBill.executeQuery();
            resultSet.next();
            Bill bill = new Bill(
                    resultSet.getLong(1),
                    resultSet.getLong(2),
                    resultSet.getDouble(3),
                    resultSet.getLong(4)
            );
            assertEquals(Optional.of(bill), billRepository.getBillById(1));
            addBill.close();
            getBill.close();
            RunScript.execute(connection, new FileReader(deleteTable));
            assertEquals(Optional.empty(), billRepository.getBillById(1));
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllBillsByUser() {
        try {
            Connection connection = DriverManager.getConnection(url);
            RunScript.execute(connection, new FileReader(createTable));
            billRepository.setUrl(url);
            PreparedStatement addBill1 = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            PreparedStatement addBill2 = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            addBill1.execute();
            addBill2.execute();
            PreparedStatement getAllBills = connection.prepareStatement("SELECT * FROM BILLS WHERE USER_ID = 1");
            resultSet = getAllBills.executeQuery();
            List<Bill> billList = new ArrayList<>();
            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getDouble(3),
                        resultSet.getLong(4)
                );
                billList.add(bill);
            }
            assertEquals(billList, billRepository.getAllBillsByUser(1));
            addBill1.close();
            addBill2.close();
            getAllBills.close();
            RunScript.execute(connection, new FileReader(deleteTable));
            assertNull(billRepository.getAllBillsByUser(1));
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBalanceBill() {
        try {
            Connection connection = DriverManager.getConnection(url);
            RunScript.execute(connection, new FileReader(createTable));
            billRepository.setUrl(url);
            PreparedStatement addBill = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            addBill.execute();
            assertEquals(0, billRepository.getBalanceBill(1), 0);
            PreparedStatement changeBalance =
                    connection.prepareStatement("UPDATE BILLS SET BALANCE = 100 WHERE ID = 1");
            changeBalance.executeUpdate();
            assertEquals(100, billRepository.getBalanceBill(1), 100);
            addBill.close();
            changeBalance.close();
            RunScript.execute(connection, new FileReader(deleteTable));
            assertEquals(0, billRepository.getBalanceBill(1), 0);
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void plusBalance() {
        try {
            Connection connection = DriverManager.getConnection(url);
            RunScript.execute(connection, new FileReader(createTable));
            billRepository.setUrl(url);
            PreparedStatement addBill = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            addBill.execute();
            assertTrue(billRepository.plusBalance(1, 100));
            addBill.close();
            RunScript.execute(connection, new FileReader(deleteTable));
            assertFalse(billRepository.plusBalance(1, 100));
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void minusBalance() {
        try {
            Connection connection = DriverManager.getConnection(url);
            RunScript.execute(connection, new FileReader(createTable));
            billRepository.setUrl(url);
            PreparedStatement addBill = connection.prepareStatement("INSERT INTO BILLS(USER_ID) VALUES (1)");
            addBill.execute();
            PreparedStatement changeBalance =
                    connection.prepareStatement("UPDATE BILLS SET BALANCE = 100 WHERE ID = 1");
            changeBalance.executeUpdate();
            assertTrue(billRepository.minusBalance(1, 100));
            addBill.close();
            RunScript.execute(connection, new FileReader(deleteTable));
            assertFalse(billRepository.minusBalance(1, 100));
            connection.close();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}