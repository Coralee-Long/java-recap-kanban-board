package spring.recapprojectspringtodo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spring.recapprojectspringtodo.model.Todo;

import java.util.List;

@Repository
public interface TodoRepo extends MongoRepository<Todo, String> {

    List<Todo> getAllTodosByStatus(Todo.StatusEnum status); // Query method for filtering by status
}
