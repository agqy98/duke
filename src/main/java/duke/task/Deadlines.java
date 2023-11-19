package duke.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.command.TaskType;

public class Deadlines extends Task {
    LocalDate deadline;

    String INPUT_DATETIME_PATTERN = "yyyy-MM-dd";
    String OUTPUT_DATETIME_PATTERN = "MMM d yyyy";

    public Deadlines(String instruction) {
        super("");
        String[] items = derive(instruction);
        this.description = items[0];
        this.deadline = convertToDate(items[1]);
    }

    public Deadlines(String description, boolean isDone, String deadline) {
        super("", false);
        this.description = description;
        this.deadline = convertToDate(deadline);
        this.isDone = isDone;
    }

    private String[] derive(String instruction) {
        String[] result = instruction.split("/");
        if (result.length != 2) {
            throw new IllegalArgumentException("Invalid format");
        } else {
            String desc = result[0].trim();
            if (desc.isEmpty()) {
                throw new IllegalArgumentException("Invalid format");
            }
            result[0] = desc;
            result[1] = result[1].replaceAll("by", "").trim();
            return result;
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toFileString() {
        return getType() + " | " + (isDone ? "1" : "0") + " | " + description + " | " + deadline;
    }

    @Override
    public String getPrintStatus() {
        DateTimeFormatter patt = DateTimeFormatter.ofPattern(OUTPUT_DATETIME_PATTERN);
        return "[" + (isDone ? "X" : " ") + "] " + description + " (by: "
        + deadline.format(patt) + ")";
    }

    private LocalDate convertToDate(String d) {
        try {
            LocalDate date = LocalDate.parse(d);
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, use " + INPUT_DATETIME_PATTERN, e);
        }
    }
}
