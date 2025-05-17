
package online_ders_otomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
     private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Online_Ders_Otomasyonu;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "bi";
    private static final String PASSWORD = "123456Aa";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
}
}
