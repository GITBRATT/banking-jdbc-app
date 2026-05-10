package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.sql.*;

public class AccountService {
    public void createAccount(Long user_id) {
        String sql = """
                INSERT INTO accounts(user_id, balance)
                VALUES (?, 0)
                """;

        try (Connection connection = DatabaseManager.open();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            var generatedKeys = ps.getGeneratedKeys();
            ps.setLong(1, user_id);
            ps.executeUpdate();
            if (generatedKeys.next()) {
                var id = generatedKeys.getInt("id");
                System.out.println("Account created, id = " + id);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void printAllAccounts() {
        String sql = """
                SELECT 
                    a.id,
                    u.name,
                    a.balance
                FROM accounts a
                JOIN users u on a.user_id = u.id
                """;
        try (Connection connection = DatabaseManager.open();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                System.out.println(
                        "Account{id=%d, user='%s', balance=%s}"
                                .formatted(rs.getLong("id"), rs.getString("name"), rs.getDouble("balance"))
                );
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
