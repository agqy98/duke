package duke.task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.command.TaskType;

public class Events extends Task {
    LocalDateTime from;
    LocalDateTime to;

    String INPUT_DATE_PATTERN = "yyyy-MM-dd";
    String INPUT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm";
    String OUTPUT_DATETIME_PATTERN = "MMM d yyyy, hh:mm a";
    String OUTPUT_DATE_PATTERN = "MMM d yyyy";
    String OUTPUT_TIME_PATTERN = "hh:mm a";

    public Events(String instruction) {
        super("");
        String[] items = derive(instruction);
        this.description = items[0];
        this.from = convertToDateTime(items[1]);
        this.to = convertToDateTime(items[2]);
    }

    public Events(String description, boolean isDone, LocalDateTime from, LocalDateTime to) {
        super("", false);
        this.description = description;
        this.from = from;
        this.to = to;
        this.isDone = isDone;
    }

    public Events(String description, boolean isDone, String from, String to) {
        super("", false);
        this.description = description;
        this.from = convertToDateTime(from);
        this.to = convertToDateTime(to);
        this.isDone = isDone;
    }

    private String[] derive(String instruction) {
        String[] result = instruction.split("/");
        if (result.length != 3) {
            throw new IllegalArgumentException("Invalid format");
        } else {
            String desc = result[0].trim();
            if (desc.isEmpty()) {
                throw new IllegalArgumentException("Invalid format");
            }
            result[0] = desc;
            // We are not handling if they put invalid dates
            // We simply take the 2nd and 3rd values and store it as from and to for now.
            result[1] = result[1].replaceAll("from", "").trim();
            result[2] = result[2].replaceAll("to", "").trim();
            return result;
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toFileString() {
        return getType() + " | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String getPrintStatus() {

        // Display only the date if both events occur at midnight.
        // Show the date range if the events happen on the same date.
        // If both conditions above are met, display one date only.

        boolean isFallOnSameDate = from.toLocalDate().equals(to.toLocalDate());
        boolean isHideTime = from.toLocalTime().equals(LocalTime.MIDNIGHT)
                && to.toLocalTime().equals(LocalTime.MIDNIGHT);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_PATTERN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(OUTPUT_TIME_PATTERN);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATETIME_PATTERN);

        String dateRange = "";

        if (isFallOnSameDate && isHideTime) {
            dateRange = "on: " + from.format(dateFormatter);
        } else if (isFallOnSameDate) {
            // But show time
            dateRange = "from: " + from.format(dateTimeFormatter) + " to: " + to.format(timeFormatter);
        } else if (isHideTime) {
            dateRange = "from: " + from.format(dateFormatter) + " " +
                    "to: " + to.format(dateFormatter);
        } else {
            dateRange = "from: " + from.format(dateTimeFormatter) + " " +
                    "to: " + to.format(dateTimeFormatter);
        }

        return "[" + (isDone ? "X" : " ") + "] " + description + " (" + dateRange + ")";
    }

    private LocalDateTime convertToDateTime(String dt) {
        DateTimeFormatter patt = DateTimeFormatter.ofPattern(INPUT_DATETIME_PATTERN);

        if (dt.length() == INPUT_DATE_PATTERN.length()) {
            dt += " " + LocalTime.MIDNIGHT;
        }
        try {
            LocalDateTime datetime = LocalDateTime.parse(dt, patt);
            return datetime;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date-time format, use yyyy-MM-dd hh:mm", e);
        }
    }
}
