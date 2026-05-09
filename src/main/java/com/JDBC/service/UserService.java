package com.JDBC.service;

import com.JDBC.config.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    public void createUser(String name) {
       String sql = """
               INSERT INTO users(name)
               VALUES (?)
               """;
       try(Connection connection = DatabaseManager.open()){
           PreparedStatement ps = connection.prepareStatement(sql);
           ps.setString(1, name);
           ps.executeUpdate();
       }catch (SQLException e){
           e.printStackTrace();
       }

    }
}
