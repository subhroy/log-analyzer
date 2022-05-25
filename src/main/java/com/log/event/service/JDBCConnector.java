package com.log.event.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {
  private static final String dbUrl = "jdbc:hsqldb:file:C:\\Apps\\hsqldb-2.6.1\\data";
  private static final String username = "SA";
  private static final String password = "";

  private static JDBCConnector instance;
  Connection con = null;

  private JDBCConnector() throws SQLException {
    DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver());
    this.con = DriverManager.getConnection(dbUrl, username, password);
  }

  public Connection getConnection() {
    return con;
  }

  public static JDBCConnector getInstance() throws SQLException {
    if (instance == null) {
      instance = new JDBCConnector();
    } else if (instance.getConnection().isClosed()) {
      instance = new JDBCConnector();
    }
    return instance;
  }
}
