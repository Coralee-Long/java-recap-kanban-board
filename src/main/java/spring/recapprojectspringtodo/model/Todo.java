package spring.recapprojectspringtodo.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ToDos")  // Maps this class to the "ToDos" collection in MongoDB
public record Todo(
        @Id String id,  // Maps to MongoDB's "_id" field
        @Field("description") String description,  // Explicit field mapping (optional)
        StatusEnum status  // Defaults to the field name "status"
) {

    // set up ENUM for task statuses
    public enum StatusEnum {
        OPEN, // Task not started
        IN_PROGRESS, // Task in progress
        DONE, // Task completed


    }

}
