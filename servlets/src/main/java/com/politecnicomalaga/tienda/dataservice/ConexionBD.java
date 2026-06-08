package com.politecnicomalaga.tienda.dataservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    //Programar esta clase con respecto a la infraestructura disponible
    private static final String URL = "jdbc:mysql://bbdd:3306/burger_db";
    private static final String USER = "tienda_user";
    private static final String PASSWORD = "onlyforyoureyes";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}