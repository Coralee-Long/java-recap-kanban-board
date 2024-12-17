package spring.recapprojectspringtodo.service;

import spring.recapprojectspringtodo.repo.TodoRepo;
import spring.recapprojectspringtodo.model.Todo;
import spring.recapprojectspringtodo.model.Todo.StatusEnum;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    // Inject the repository
    private final TodoRepo repo;

    // Constructor Injection
    public TodoService(TodoRepo repo) {
        this.repo = repo;
    }

    /**
     * FETCH ALL TODOS from the database.
     *
     * @return A list of all Todo records.
     */
    public List<Todo> getAllTodos() {
        return repo.findAll();
    }

    /**
     * FETCH a SINGLE TODO from the database by its ID.
     *
     * @param id The ID of the Todo to fetch.
     * @return A single Todo object if found.
     * @throws RuntimeException if no Todo is found with the given ID.
     */
    public Todo getTodoById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    /**
     * FETCH TODOS filtered by their STATUS.
     *
     * @param status The status to filter (e.g., OPEN, IN-PROGRESS, DONE).
     * @return A list of Todos matching the specified status.
     */
    public List<Todo> getAllTodosByStatus(StatusEnum status) {
        return repo.findAll().stream()
                .filter(todo -> todo.status().equals(status))
                .toList();
    }

    /**
     * CREATE a new TODO item and save it to the database.
     *
     * @param description The description of the todo task.
     * @param status      The status of the task (e.g., OPEN, IN-PROGRESS, DONE).
     * @return The saved Todo object with a generated unique ID.
     */
    public Todo createTodo(String description, StatusEnum status) {
        // Generate a random UUID for the todo id
        String id = UUID.randomUUID().toString();

        // Create a new Todo record (no need for a setter or constructor)
        Todo todo = new Todo(id, description, status);

        // Save the Todo object to the repository and return it
        return repo.save(todo);
    }

    /**
     * EDIT an EXISTING TODO item by its ID.
     *
     * @param id   The ID of the Todo to edit.
     * @param todo The updated Todo object containing new values.
     * @return The updated Todo object after saving.
     * @throws RuntimeException if no Todo is found with the given ID.
     */
    public Todo editTodoById(String id, Todo todo) {
        if (repo.existsById(id)) {
            Todo updatedTodo = new Todo(id, todo.description(), todo.status());
            return repo.save(updatedTodo);
        } else {
            throw new RuntimeException("Todo not found with id: " + id);
        }
    }

    /**
     * DELETE a TODO item by its ID.
     *
     * @param id The ID of the Todo to delete.
     * @throws RuntimeException if no Todo is found with the given ID.
     */
    public void deleteTodoById(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new RuntimeException("Todo not found with id: " + id);
        }
    }
}
