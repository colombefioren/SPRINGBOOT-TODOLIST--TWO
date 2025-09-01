package colombe.simpletodolist.dao;

import colombe.simpletodolist.entity.Todo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    String sql = "INSERT INTO TODO (title, description) VALUES (?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, todo.getTitle());
      stmt.setString(2, todo.getDescription());
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
}
