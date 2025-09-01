package colombe.simpletodolist.dao;

import colombe.simpletodolist.entity.Todo;
import java.util.List;

public interface TodoDAO {
  List<Todo> getAllTodo();

  Todo getTodoById(int id);

  void createTodo(Todo todo);

  void deleteTodo(int id);
}
