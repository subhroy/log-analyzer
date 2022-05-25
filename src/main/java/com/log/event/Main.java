package com.log.event;

import com.log.event.exception.LogAnalyzerException;
import com.log.event.model.Event;
import com.log.event.service.DataParser;
import com.log.event.service.DbOperations;
import com.log.event.service.JDBCConnector;
import com.log.event.service.UserInputReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

@Slf4j
public class Main {
  static InputStream iStream = null;
  static Properties properties = new Properties();

  static {
    try {
      iStream = Main.class.getClassLoader().getResourceAsStream("application.properties");
      properties.load(iStream);
    } catch (IOException ioe) {
      log.error(ioe.getMessage());
    }
  }

  public static void main(String[] args) {
    Connection dbConnection = null;
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      File file = UserInputReader.readUserInput(bufferedReader);
      // create DB Connection
      dbConnection = JDBCConnector.getInstance().getConnection();
      log.info("HSQLDB Schema Name :" + dbConnection.getSchema());
      // Creating DB table
      DbOperations.createEventDataTable(dbConnection, properties);
      ArrayList<Event> eventList = DataParser.parseLogFileData(bufferedReader, file, dbConnection);
      if (!eventList.isEmpty()) {
        DataParser.compareEventTime(eventList, dbConnection, properties);
      }
      // Check inserted Data using select sql
      //DbOperations.fetchAndValidateEventData(dbConnection, properties);

    } catch (LogAnalyzerException | SQLException ex) {
      log.error("Exception : LogAnalyzerException ",ex.fillInStackTrace());
    } finally{
        try {
          // Close connection
          assert dbConnection != null;
          dbConnection.close();
        }
        catch (SQLException e) {
          log.error("Exception : While closing DB Connection : ",e.fillInStackTrace());
        }
      }
  }
}
