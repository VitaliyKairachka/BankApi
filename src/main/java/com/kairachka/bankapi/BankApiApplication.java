package com.kairachka.bankapi;

import com.kairachka.bankapi.service.Server;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BankApiApplication {
    public static void main(String[] args) {
        Server.start();
    }
}
