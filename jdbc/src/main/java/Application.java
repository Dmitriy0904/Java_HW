import controller.Controller;
import db.ConnectionSetup;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {
    public static void run(Long userId, String username, String password){
        ConnectionSetup connectionSetup = new ConnectionSetup(username, password);
        Connection connection = connectionSetup.getConnection();

        try
        {
            Controller controller = new Controller(userId, connection);
            controller.userInterface();

        } finally {  try {
                connection.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
