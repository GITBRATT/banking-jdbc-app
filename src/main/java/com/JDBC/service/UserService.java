package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.sql.*;

public class UserService {
    public void createUser(String name) {
       String sql = """
               INSERT INTO users(name)
               VALUES (?)
               """;
       try(Connection connection = DatabaseManager.open()){
           PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ps.setString(1, name);
           ps.executeUpdate();
           var generatedKeys = ps.getGeneratedKeys(); // Получаем текущий id - закрываеться вместе с Statemebt
           if (generatedKeys.next()) {
               var id = generatedKeys.getInt("id");
               System.out.println("User created, id = " + id);
           }
       }catch (SQLException e){
           e.printStackTrace();
       }
    }

    public void printAllUsers() {
        String sql = """
                SELECT id,name
                FROM users
                """;
        try (Connection connection = DatabaseManager.open();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                System.out.println(
                        "User{id=%d, user='%s'}"
                                .formatted(rs.getLong("id"), rs.getString("name"))
                );
            }

        }catch (Exception e) {
        e.printStackTrace();
        }
    }
}
