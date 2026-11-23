package br.com.boletim_flow.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL = ConfigUtil.getProperty("db.url");
    private static final String USER = ConfigUtil.getProperty("db.user");
    private static final String PASSWORD = ConfigUtil.getProperty("db.password");

    private ConexaoBD() {}

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Configurações do banco não carregadas. Verifique o database.properties.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}