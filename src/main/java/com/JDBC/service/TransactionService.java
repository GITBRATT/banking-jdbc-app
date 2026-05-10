package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionService {
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
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

        String balance = """
                SELECT balance
                FROM accounts
                WHERE id = ?
                """;

        String transaction = """
                INSERT INTO transactions(from_account_id, to_account_id, amount)
                VALUES(?,?,?)
                """;

        try(Connection connection = DatabaseManager.open()) {
            // Отключаем автоматическую отправку commit - чтобы проверить выполнение всех транзакций
            connection.setAutoCommit(false);
            BigDecimal fromBalance;

            try (PreparedStatement preparedWithdraw = connection.prepareStatement(withdraw);
                 PreparedStatement preparedDeposit = connection.prepareStatement(deposit);
                 PreparedStatement preparedBalance = connection.prepareStatement(balance);
                 PreparedStatement preparedTransactions = connection.prepareStatement(transaction)){

                // Проверка баланса
                preparedBalance.setLong(1, fromId);
                var resultSet = preparedBalance.executeQuery();
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("account not found");
                }
                fromBalance = resultSet.getBigDecimal("balance");
                if (fromBalance.compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Not enough money");
                }

                // Снять
                preparedWithdraw.setBigDecimal(1, amount);
                preparedWithdraw.setLong(2, fromId);
                preparedWithdraw.executeUpdate();
                // Пополнить
                preparedDeposit.setBigDecimal(1, amount);
                preparedDeposit.setLong(2, toId);
                preparedDeposit.executeUpdate();

                preparedTransactions.setLong(1, fromId);
                preparedTransactions.setLong(2, toId);
                preparedTransactions.setBigDecimal(3, amount);
                preparedTransactions.executeUpdate();

                // После успешной подготовки commit отправляем их в базу
                connection.commit();
                System.out.println("Transfer completed");
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void printAllTransactions() {
        String sql = """
                SELECT 
                    id,
                    from_account_id,
                    (
                        SELECT u.name
                        FROM users u
                        WHERE u.id = (
                            SELECT a.user_id
                            FROM accounts a
                            WHERE a.id = t.from_account_id
                        )
                    ) AS from_user,
                    to_account_id,
                    (
                        SELECT u.name
                        FROM users u
                        WHERE u.id = (
                            SELECT a.user_id
                            FROM accounts a
                            WHERE a.id = t.to_account_id
                        )
                    ) AS to_user,
                    amount,
                    created_at
                FROM transactions t
                ORDER BY id
                """;



        try(Connection connection = DatabaseManager.open();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(
                        "Transaction{id=%d, from_account_id=%d, from_user='%s', to_account_id=%d, to_user='%s', amount=%s, created_at=%s}"
                                .formatted(
                                        rs.getLong("id"),
                                        rs.getLong("from_account_id"),
                                        rs.getString("from_user"),
                                        rs.getLong("to_account_id"),
                                        rs.getString("to_user"),
                                        rs.getBigDecimal("amount"),
                                        rs.getObject("created_at")
                                )
                );
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
