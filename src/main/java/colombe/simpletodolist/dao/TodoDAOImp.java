package colombe.simpletodolist.dao;

import colombe.simpletodolist.entity.Todo;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TodoDAOImp implements TodoDAO {
  public List<Todo> getAllTodo() {
    String sql = "SELECT * FROM todo";
    List<Todo> todoList = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        todoList.add(mapRow(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error while fetching todos!");
    }
    return todoList;
  }

  public Todo getTodoById(int id) {
    String sql = "SELECT * FROM todo WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapRow(rs);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching the todo with id " + id);
    }
    return null;
  }

  public void createTodo(Todo todo) {
    String sql =
        "INSERT INTO TODO (title, description,start_datetime, end_datetime, done) VALUES (?, ?,?,?,?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, todo.getTitle());
      stmt.setString(2, todo.getDescription());

      if (todo.getStartDatetime() != null) {
        stmt.setTimestamp(3, toTimestamp(todo.getStartDatetime()));
      } else {
        stmt.setTimestamp(3, null);
      }

      if (todo.getEndDatetime() != null) {
        stmt.setTimestamp(4, toTimestamp(todo.getEndDatetime()));
      } else {
        stmt.setTimestamp(4, null);
      }

      stmt.setBoolean(5, todo.isDone());

      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error while adding a new todo");
    }
  }

  public void deleteTodo(int id) {
    String sql = "DELETE FROM TODO WHERE ID = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting the todo");
    }
  }

  public Todo mapRow(ResultSet rs) throws SQLException {
    return new Todo(
        rs.getInt("id"),
        rs.getString("title"),
        rs.getString("description"),
        rs.getTimestamp("start_datetime").toInstant(),
        rs.getTimestamp("end_datetime").toInstant(),
        rs.getBoolean("done"));
  }

  private Timestamp toTimestamp(Instant instant) {
    return instant == null ? null : Timestamp.from(instant);
  }
}
