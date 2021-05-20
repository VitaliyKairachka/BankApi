package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Operation;
import com.kairachka.bankapi.util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperationRepository {
    PropertiesManager propertiesManager = new PropertiesManager();
    String url = propertiesManager.getUrl();
    ResultSet resultSet = null;

    public boolean addOperation(Operation operation) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO OPERATIONS (SOURCE, TARGET, SUM)  VALUES ( ?, ?, ? )"
             )) {
            preparedStatement.setLong(1, operation.getSourceId());
            preparedStatement.setLong(2, operation.getTargetId());
            preparedStatement.setDouble(3, operation.getSum());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Operation> getAllOperationByBill(long billId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM OPERATIONS WHERE SOURCE = ?"
             )) {
            preparedStatement.setLong(1, billId);
            resultSet = preparedStatement.executeQuery();
            List<Operation> operationList = new ArrayList<>();
            while (resultSet.next()) {
                Operation operation = new Operation(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getLong(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5)
                );
                operationList.add(operation);
            }
            return operationList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Operation> getAllOperation() {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM OPERATIONS"
             )) {
            resultSet = preparedStatement.executeQuery();
            List<Operation> operationList = new ArrayList<>();
            while (resultSet.next()) {
                Operation operation = new Operation(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getLong(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5)
                );
                operationList.add(operation);
            }
            return operationList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Operation> getAllOperationsByStatus(String status) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM OPERATIONS WHERE STATUS = ?"
             )) {
            preparedStatement.setString(1, status);
            resultSet = preparedStatement.executeQuery();
            List<Operation> operationList = new ArrayList<>();
            while (resultSet.next()) {
                Operation operation = new Operation(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getLong(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5)
                );
                operationList.add(operation);
            }
            return operationList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean changeOperationStatus(long operationId, String status) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE OPERATIONS SET STATUS = ? WHERE  ID = ?"
             )) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, operationId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Operation> getOperationById(long operationId){
        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM  OPERATIONS WHERE ID = ?"
        )) {
            preparedStatement.setLong(1, operationId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Operation operation = new Operation(
                    resultSet.getLong(1),
                    resultSet.getLong(2),
                    resultSet.getLong(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
            return Optional.of(operation);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
