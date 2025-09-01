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
    String sql;
    boolean hasStart = todo.getStartDatetime() != null;
    boolean hasEnd = todo.getEndDatetime() != null;

    if (hasStart && hasEnd) {
      sql =
          "INSERT INTO todo (title, description, start_datetime, end_datetime, done) VALUES (?, ?, ?, ?, ?)";
    } else if (hasStart) {
      sql = "INSERT INTO todo (title, description, start_datetime, done) VALUES (?, ?, ?, ?)";
    } else if (hasEnd) {
      sql = "INSERT INTO todo (title, description, end_datetime, done) VALUES (?, ?, ?, ?)";
    } else {
      sql = "INSERT INTO todo (title, description, done) VALUES (?, ?, ?)";
    }

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      int index = 1;
      stmt.setString(index++, todo.getTitle());
      stmt.setString(index++, todo.getDescription());

      if (hasStart) {
        stmt.setTimestamp(index++, toTimestamp(todo.getStartDatetime()));
      }
      if (hasEnd) {
        stmt.setTimestamp(index++, toTimestamp(todo.getEndDatetime()));
      }

      stmt.setBoolean(index, todo.isDone());
      stmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException("Error creating new todo", e);
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
        toInstant(rs.getTimestamp("start_datetime")),
        toInstant(rs.getTimestamp("end_datetime")),
        rs.getBoolean("done"));
  }

  private Instant toInstant(Timestamp ts) {
    return ts == null ? null : ts.toInstant();
  }

  private Timestamp toTimestamp(Instant instant) {
    return instant == null ? null : Timestamp.from(instant);
  }
}
