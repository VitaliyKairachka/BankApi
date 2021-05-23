package com.kairachka.bankapi;

import com.kairachka.bankapi.util.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.sql.SQLException;


public class BankApiApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(BankApiApplication.class);
        try {
            Server.start();
        } catch (FileNotFoundException | SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
