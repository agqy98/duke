package duke.parser;

import java.time.LocalDateTime;

import duke.command.TaskType;
import duke.task.Deadlines;
import duke.task.Events;
import duke.task.Task;
import duke.task.ToDos;

public class Parser {
    public static Task createTask(TaskType taskType, String taskDescription) {
        switch (taskType) {
            case EVENT:
                return new Events(taskDescription);
            case DEADLINE:
                return new Deadlines(taskDescription);
            default:
                return new ToDos(taskDescription);
        }
    }

    public static Task createTaskFromLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        switch (type) {
            case "T":
                return new ToDos(description, isDone);
            case "D":
                String deadline = parts[3];
                return new Deadlines(description, isDone, deadline);
            case "E":
                String fromStr = parts[3];
                String toStr = parts[4];
                LocalDateTime from = LocalDateTime.parse(fromStr);
                LocalDateTime to = LocalDateTime.parse(toStr);
                return new Events(description, isDone, from, to);
            default:
                throw new IllegalArgumentException("Invalid task type: " + type);
        }
    }
}
