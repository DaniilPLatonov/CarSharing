package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DataBase {
    static String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void createTable() {

        String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY (ID INT PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR NOT NULL UNIQUE);";
        String sqlCar = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "COMPANY_ID INTEGER NOT NULL, " +
                "CONSTRAINT fk_company " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID))";

        String sqlCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                "CONSTRAINT fk_car " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID))";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();) {
            statement.executeUpdate(sqlCompany);
            statement.executeUpdate(sqlCar);
            statement.executeUpdate(sqlCustomer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeConnection() {

        if (getConnection() != null) {
            try {
                getConnection().close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }

}