package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountService {
    public void createAccount(Long user_id) {
        String sql = """
                INSERT INTO accounts(user_id, balance)
                VALUES (?, 0)
                """;

        try (Connection connection = DatabaseManager.open()){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, user_id);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
