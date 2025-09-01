package colombe.simpletodolist.controller;

import colombe.simpletodolist.entity.Todo;
import colombe.simpletodolist.service.TodoService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/todos")
public class TodoController {
  private final TodoService todoService = new TodoService();

  @GetMapping
  public List<Todo> getAllTodo() {
    return todoService.getAllTodo();
  }

  @GetMapping("/{id}")
  public Todo getTodoById(@PathVariable int id) {
    return todoService.getTodoById(id);
  }

  @PostMapping
  public void createTodo(@RequestBody Todo todo) {
    todoService.createTodo(todo);
  }

  @DeleteMapping("/{id}")
  public void deleteTodo(@PathVariable int id) {
    todoService.deleteTodo(id);
  }
}
