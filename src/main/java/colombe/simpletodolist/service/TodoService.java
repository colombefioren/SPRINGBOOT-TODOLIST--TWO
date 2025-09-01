package colombe.simpletodolist.service;

import colombe.simpletodolist.dao.TodoDAOImp;
import colombe.simpletodolist.entity.Todo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
  private final TodoDAOImp TodoDAO;

  public TodoService() {
    this.TodoDAO = new TodoDAOImp();
  }

  public List<Todo> getAllTodo() {
    return TodoDAO.getAllTodo();
  }

  public Todo getTodoById(int id) {
    return TodoDAO.getTodoById(id);
  }

  public void createTodo(Todo todo) {
    if (todo.getTitle() == null || todo.getTitle().isEmpty()) {
      throw new IllegalArgumentException("Title cannot be empty");
    }
    TodoDAO.createTodo(todo);
  }

  public void deleteTodo(int id) {
    TodoDAO.deleteTodo(id);
  }
}
