package duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for Todo object.
 *
 * @author Gao Mengqi
 * @version CS2103T AY22/23 Semester 2
 */
public class Todo extends Task {
    private String taskType = "T";

    /**
     * Constructor for Todo object.
     *
     * @param description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTaskType() {
        return this.taskType;
    }

    public void editInfo(String content) {
        this.description = content;
    }

    /**
     * Message printed when a new Todo task is added.
     *
     * @return String representing the Todo task information.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
