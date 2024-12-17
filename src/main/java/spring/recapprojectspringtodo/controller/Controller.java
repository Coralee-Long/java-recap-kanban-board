package spring.recapprojectspringtodo.controller;

import org.springframework.web.bind.annotation.*;
import spring.recapprojectspringtodo.model.Todo;
import spring.recapprojectspringtodo.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class Controller {

    // Set up Service
    private final TodoService service;

    // Constructor injection for Service
    public Controller(TodoService service) {
        this.service = service;
    }

    /**
     * FETCH ALL TODOS or FILTER TODOS BY STATUS.
     *
     * @param status Optional status to filter Todos (e.g., OPEN, IN_PROGRESS, DONE).
     * @return A list of Todos.
     */
    @GetMapping
    public List<Todo> fetchAllTodos(@RequestParam(required = false) Todo.StatusEnum status) {
        if (status != null) {
            return service.getAllTodosByStatus(status);
        }
        return service.getAllTodos();
    }

    /**
     * FETCH A SINGLE TODO BY ID.
     *
     * @param id The ID of the Todo to fetch.
     * @return The Todo object.
     */
    @GetMapping("/{id}")
    public Todo fetchTodoById(@PathVariable String id) {
        return service.getTodoById(id);
    }

    /**
     * CREATE A NEW TODO.
     *
     * @param todo The Todo object with description and status.
     * @return The saved Todo object.
     */
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return service.createTodo(todo.description(), todo.status());
    }

    /**
     * UPDATE AN EXISTING TODO BY ID.
     *
     * @param id   The ID of the Todo to update.
     * @param todo The updated Todo object.
     * @return The updated Todo.
     */
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return service.editTodoById(id, todo);
    }

    /**
     * DELETE A TODO BY ID.
     *
     * @param id The ID of the Todo to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        service.deleteTodoById(id);
    }
}

