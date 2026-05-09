package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionService {
    public void transfer(Long fromId, Long toId, Double amount) {
        // Снять
        String withdraw = """
                UPDATE accounts 
                SET balance = balance - ?
                WHERE id = ?
                """;
        // Пополнить
        String deposit = """
                UPDATE accounts 
                SET balance = balance + ?
                WHERE id = ?
                """;

        try(Connection connection = DatabaseManager.open()) {
            // Отключаем автоматическую отправку commit
            connection.setAutoCommit(false);

            try (PreparedStatement preparedWithdraw = connection.prepareStatement(withdraw);
                 PreparedStatement preparedDeposit = connection.prepareStatement(deposit)){
                preparedWithdraw.setDouble(1, fromId);
                preparedWithdraw.setLong(2, toId);
                preparedWithdraw.executeUpdate();

                preparedDeposit.setDouble(1, fromId);
                preparedDeposit.setLong(2, toId);
                preparedDeposit.executeUpdate();

                // После успешной подготовки commit отправляем их в базу
                connection.commit();
            }catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
