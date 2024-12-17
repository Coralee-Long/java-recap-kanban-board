package spring.recapprojectspringtodo.service;

import org.mockito.Mockito;
import spring.recapprojectspringtodo.model.Todo;
import spring.recapprojectspringtodo.repo.TodoRepo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    // Mock Repo
    private final TodoRepo repo = mock(TodoRepo.class);

    // Service to Test
    private final TodoService service = new TodoService(repo);

     @Test
     void getAllToDos_shouldReturnEmptyList() {
         // GIVEN: Repo returns empty list
         when(repo.findAll()).thenReturn(Collections.emptyList());
         // WHEN: Service is called
         List<Todo> result = service.getAllTodos();
         // THEN: The result should be an empty list
         assertTrue(result.isEmpty(), "The list should be empty");
         // Verify repo was called once
         Mockito.verify(repo, Mockito.times(1)).findAll();
     }

     @Test
     void getAllToDos_ShouldReturnListOfTodos() {
        // GIVEN: Repo returns a list of predefined Todos
        List<Todo> todos = List.of(
        new Todo("id-1", "description 1", Todo.StatusEnum.OPEN),
        new Todo("id-2", "description 2", Todo.StatusEnum.IN_PROGRESS),
        new Todo("id-3", "description 3", Todo.StatusEnum.DONE)
        );
        when(repo.findAll()).thenReturn(todos); // Mocked repo
        // WHEN: Service is called
        List<Todo> result = service.getAllTodos();
        // THEN:
        assertEquals(3, result.size(), "The list should contain 3 elements");
        assertEquals("description 1", result.get(0).description());
        assertEquals(Todo.StatusEnum.OPEN, result.get(0).status());
        assertEquals("description 2", result.get(1).description());
        assertEquals(Todo.StatusEnum.IN_PROGRESS, result.get(1).status());
        assertEquals("description 3", result.get(2).description());
        assertEquals(Todo.StatusEnum.DONE, result.get(2).status());
        // Verify that the repository's findAll method was called once
        Mockito.verify(repo, Mockito.times(1)).findAll();
     }

     @Test
     void createTodo_shouldReturnAddedTodo() {
         // GIVEN: Input data
         String description = "description 1"; // Declare description
         Todo.StatusEnum status = Todo.StatusEnum.OPEN; // Declare status
         // Expected Todo object (simulate what the service will save and return)
         Todo expectedTodo = new Todo("id-1", description, status);
         // Mock the repo.save() method to return the expected Todo object
         when(repo.save(any(Todo.class))).thenReturn(expectedTodo);
         // WHEN: Service is called to create a Todo
         Todo result = service.createTodo(description, status);
         // THEN: Verify the result
         assertEquals(expectedTodo, result, "The returned Todo should match the saved Todo");
         // Verify that repo.save() was called exactly once with a Todo object
         verify(repo, times(1)).save(any(Todo.class));



     }
}