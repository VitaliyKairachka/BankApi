package com.kairachka.bankapi.util;

public class QuerySQL {
    public static final String ADD_USER =
            "INSERT INTO USERS (LOGIN, PASSWORD, FIRST_NAME, LAST_NAME, MIDDLE_NAME, PASSPORT, MOBILE_PHONE, ROLE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_USER = "SELECT * FROM USERS WHERE LOGIN = ?";

    public static final String ADD_BILL = "INSERT INTO BILLS(USER_ID) VALUES (?)";

    public static final String GET_BILL_BY_ID = "SELECT * FROM BILLS WHERE ID = ?";

    public static final String GET_ALL_BILLS_BY_USER = "SELECT * FROM BILLS WHERE USER_ID = ?";
}
