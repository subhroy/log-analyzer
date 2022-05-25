import com.log.event.service.JDBCConnector;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JDBCConnectorTest {

  @Test
  public void getDBConnectionTest() {
    Connection dbConnection = null;
    try {
      dbConnection = JDBCConnector.getInstance().getConnection();
      assertNotNull(dbConnection, "Connection is successful.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
