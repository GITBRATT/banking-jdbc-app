package com.JDBC;

import com.JDBC.service.*;

import java.io.IOException;
import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        UserService userService = new UserService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();

        while (true) {
            System.out.println("""
                    =========================
                    1 - Create user
                    2 - Create account
                    3 - Transfer money
                    4 - Show users
                    5 - Show accounts
                    6 - Show transactions
                    7 - Show account balancez
                    0 - Exit
                    =========================
                    """);
            System.out.println("Enter your choice: ");
            StringTokenizer st = new StringTokenizer(reader.readLine());
            int choice = Integer.parseInt(st.nextToken());
            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    return;
                case 1:
                    System.out.println("Enter username: ");
                    String username = reader.readLine();
                    userService.createUser(username);
            }
        }
    }
}