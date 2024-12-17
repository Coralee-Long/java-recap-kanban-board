package spring.recapprojectspringtodo.service;

import org.mockito.Mockito;
import spring.recapprojectspringtodo.model.Todo;
import spring.recapprojectspringtodo.repo.TodoRepo;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    // Mock Repo
    private final TodoRepo repo = mock(TodoRepo.class);

    // Service to Test
    private final TodoService service = new TodoService(repo);

    @Test
    void getAllToDos_shouldReturnEmptyList() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        List<Todo> result = service.getAllTodos();
        assertTrue(result.isEmpty(), "The list should be empty");
        Mockito.verify(repo, Mockito.times(1)).findAll();
    }

    @Test
    void getAllToDos_ShouldReturnListOfTodos() {
        List<Todo> todos = List.of(
                new Todo("id-1", "description 1", Todo.StatusEnum.OPEN),
                new Todo("id-2", "description 2", Todo.StatusEnum.IN_PROGRESS),
                new Todo("id-3", "description 3", Todo.StatusEnum.DONE)
        );
        when(repo.findAll()).thenReturn(todos);
        List<Todo> result = service.getAllTodos();
        assertEquals(3, result.size(), "The list should contain 3 elements");
        Mockito.verify(repo, Mockito.times(1)).findAll();
    }

    @Test
    void getTodoById_shouldReturnTodoWhenFound() {
        // GIVEN
        String id = "id-1";
        Todo expectedTodo = new Todo(id, "Test Todo", Todo.StatusEnum.OPEN);
        when(repo.findById(id)).thenReturn(Optional.of(expectedTodo));

        // WHEN
        Todo result = service.getTodoById(id);

        // THEN
        assertEquals(expectedTodo, result, "Returned todo should match the expected one.");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void getTodoById_shouldThrowExceptionWhenNotFound() {
        // GIVEN
        String id = "non-existent-id";
        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getTodoById(id));
        assertEquals("Todo not found with id: " + id, exception.getMessage());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void getAllTodosByStatus_shouldReturnFilteredTodos() {
        // GIVEN
        Todo.StatusEnum status = Todo.StatusEnum.OPEN;
        List<Todo> todos = List.of(
                new Todo("id-1", "Todo 1", Todo.StatusEnum.OPEN),
                new Todo("id-2", "Todo 2", Todo.StatusEnum.DONE)
        );
        when(repo.findAll()).thenReturn(todos);

        // WHEN
        List<Todo> result = service.getAllTodosByStatus(status);

        // THEN
        assertEquals(1, result.size(), "Should return only OPEN todos.");
        assertEquals("id-1", result.get(0).id());
        verify(repo, times(1)).findAll();
    }

    @Test
    void createTodo_shouldReturnAddedTodo() {
        String description = "New Todo";
        Todo.StatusEnum status = Todo.StatusEnum.IN_PROGRESS;
        Todo savedTodo = new Todo("id-123", description, status);

        when(repo.save(any(Todo.class))).thenReturn(savedTodo);

        Todo result = service.createTodo(description, status);

        assertEquals(savedTodo, result, "The returned todo should match the saved todo.");
        verify(repo, times(1)).save(any(Todo.class));
    }

    @Test
    void editTodoById_shouldUpdateAndReturnTodo() {
        // GIVEN
        String id = "id-1";
        Todo existingTodo = new Todo(id, "Old Description", Todo.StatusEnum.OPEN);
        Todo updatedTodo = new Todo(id, "Updated Description", Todo.StatusEnum.DONE);

        when(repo.existsById(id)).thenReturn(true);
        when(repo.save(any(Todo.class))).thenReturn(updatedTodo);

        // WHEN
        Todo result = service.editTodoById(id, updatedTodo);

        // THEN
        assertEquals(updatedTodo, result, "Updated todo should be returned.");
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).save(any(Todo.class));
    }

    @Test
    void editTodoById_shouldThrowExceptionIfTodoNotFound() {
        // GIVEN
        String id = "id-1";
        Todo updatedTodo = new Todo(id, "Updated Description", Todo.StatusEnum.DONE);
        when(repo.existsById(id)).thenReturn(false);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.editTodoById(id, updatedTodo));
        assertEquals("Todo not found with id: " + id, exception.getMessage());
        verify(repo, times(1)).existsById(id);
        verify(repo, never()).save(any(Todo.class));
    }

    @Test
    void deleteTodoById_shouldDeleteTodoWhenFound() {
        // GIVEN
        String id = "id-1";
        when(repo.existsById(id)).thenReturn(true);

        // WHEN
        assertDoesNotThrow(() -> service.deleteTodoById(id));

        // THEN
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    void deleteTodoById_shouldThrowExceptionIfTodoNotFound() {
        // GIVEN
        String id = "non-existent-id";
        when(repo.existsById(id)).thenReturn(false);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteTodoById(id));
        assertEquals("Todo not found with id: " + id, exception.getMessage());
        verify(repo, times(1)).existsById(id);
        verify(repo, never()).deleteById(any());
    }
}
