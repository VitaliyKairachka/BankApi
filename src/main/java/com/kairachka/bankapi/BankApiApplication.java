package com.kairachka.bankapi;

import com.kairachka.bankapi.util.Server;

import java.io.FileNotFoundException;
import java.sql.SQLException;


public class BankApiApplication {
    public static void main(String[] args) {
        try {
            Server.start();
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("Server error");
        }
    }
}
