package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/car_rental",
                "root",
                "raiyan027301"  
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
