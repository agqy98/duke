package duke.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.command.TaskType;

public abstract class Task {
    public String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return this.description;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public abstract TaskType getTaskType();

    public abstract String getType();

    public abstract String toFileString();

    public abstract String getPrintStatus();

    public void print() {
        System.out.println("[" + getType() + "]" + getPrintStatus());
    }
}
