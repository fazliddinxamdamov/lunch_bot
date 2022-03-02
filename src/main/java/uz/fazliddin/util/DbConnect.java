package uz.fazliddin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static uz.fazliddin.ignore.Constants.*;

/**
 * @author Fazliddin Xamdamov
 * @date 26.02.2022  16:28
 * @project inflex-launch-bot
 */
public class DbConnect {

    public static Connection  getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername , dbPassword);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
