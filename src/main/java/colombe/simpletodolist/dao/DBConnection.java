package colombe.simpletodolist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  public static final String DB_URL = "jdbc:postgresql://localhost:5433/springboot_todolist_two";
  public static final String DB_USER = "postgres";
  public static final String DB_PASSWORD = "1234";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }
}
