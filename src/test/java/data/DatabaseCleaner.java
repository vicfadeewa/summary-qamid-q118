package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseCleaner {

    private static final String DB_URL = System.getProperty("db.url");
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    @SneakyThrows
    public static void clearPaymentTable() {
        String sql = "DELETE FROM payment_entity";
        QueryRunner runner = new QueryRunner();

        try (var connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            runner.update(connection, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static String fetchPaymentStatus() {
        String query = "SELECT status FROM payment_entity";
        return executeQuery(query);
    }

    @SneakyThrows
    private static String executeQuery(String sql) {
        QueryRunner runner = new QueryRunner();

        try (var connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return runner.query(connection, sql, new ScalarHandler<>());
        }
    }
}
