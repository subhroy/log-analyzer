package com.log.event.service;

import com.log.event.model.Event;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Properties;

@Slf4j
public class DbOperations {
  public static void createEventDataTable(Connection connection, Properties properties) {
    Statement statement = null;
    try {
      if (connection != null) {
        statement = connection.createStatement();
        statement.executeUpdate(properties.getProperty("drop_event_details_sql"));
        log.info("Dropped exiting HSQL DB table....");
        statement.executeUpdate(properties.getProperty("create_event_details_sql"));
        log.info("Created HSQL DB table....");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        assert statement != null;
        statement.close();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
    }
  }

  public static void insertEventData(
      Connection connection, Event event, Long eventExecTime, Properties properties) {
    PreparedStatement preparedStatement = null;
    try {
      if (connection != null) {
        preparedStatement =
            connection.prepareStatement(properties.getProperty("insert_event_data_sql"));
        if (preparedStatement != null) {
          preparedStatement.setString(1, event.getId());
          preparedStatement.setString(2, event.getState());
          preparedStatement.setString(3, event.getType());
          preparedStatement.setString(4, event.getHost());
          preparedStatement.setInt(5, Math.toIntExact(eventExecTime));
          log.info(preparedStatement.toString());
          preparedStatement.executeUpdate();
        }
        // int result =  statement.executeUpdate();
        log.info("Data inserted successfully");
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        assert preparedStatement != null;
        preparedStatement.close();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
    }
  }

  public static void fetchAndValidateEventData(Connection connection, Properties properties) {
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      if (connection != null) {
        statement = connection.createStatement();
        log.info(
            "fetch_event_details_sql --> " + properties.getProperty("fetch_event_details_sql"));
        resultSet = statement.executeQuery(properties.getProperty("fetch_event_details_sql"));
        while (resultSet != null && resultSet.next()) {
          log.info(
              "result : "
                  + resultSet.getString("event_id")
                  + " | "
                  + resultSet.getString("e_state")
                  + " | "
                  + resultSet.getString("exe_time"));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        assert statement != null;
        statement.close();
        assert resultSet != null;
        resultSet.close();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
    }
  }
}
