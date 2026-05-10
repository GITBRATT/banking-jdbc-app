package com.JDBC;

import com.JDBC.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
                    String userName = reader.readLine();
                    userService.createUser(userName);
                    break;
                case 2:
                    System.out.println("Enter user_id: ");
                    String userId = reader.readLine();
                    accountService.createAccount(Long.valueOf(userId));
                    break;
                case 3:
                    System.out.println("Enter From_user_id: ");
                    Long fromUserId = Long.valueOf(reader.readLine());
                    System.out.println("Enter To_user_id: ");
                    Long toUserId = Long.valueOf(reader.readLine());
                    System.out.println("Enter amount: ");
                    BigDecimal amount = new BigDecimal(reader.readLine());
                    transactionService.transfer(fromUserId,toUserId, amount);
                case 4:
                    userService.printAllUsers();
                    break;
                case 5:
                    accountService.printAllAccounts();
                    break;
                case 6:
                    transactionService.printAllTransactions();
                    break;

            }
        }
    }
}