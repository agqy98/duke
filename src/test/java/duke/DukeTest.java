package duke;

import duke.command.TaskType;
import duke.error.ErrorType;
import duke.storage.Storage;
import duke.tasklist.TaskList;
import duke.ui.UI;
import duke.task.Task;
import duke.task.Events;
import duke.task.Deadlines;
import duke.task.ToDos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class DukeTest {
    String END = "\nbye";

    @Test
    void testTypo() {
        String input = "lisy" + END;
        String expectedOutput = "OOPS! It appears there might be a typo. Did you mean to write 'list'?";

        runAndAssertTask(input, expectedOutput);
    }

    @Test
    void testTodo() {
        String input = "todo read book" + END;

        // Assert
        String expectedOutput = "[T][ ] read book";

        runAndAssertTask(input, expectedOutput);
    }

    @Test
    void testDeadline() {
        String input = "deadline return book /by 2023-06-06" + END;

        // Assert
        String expectedOutput = "[D][ ] return book (by: Jun 6 2023)";

        runAndAssertTask(input, expectedOutput);
    }

    @Test
    void testEvent() {
        String input = "event project meeting /from 2023-08-06 14:00 /to 2023-08-06 16:00" + END;

        // Assert
        String expectedOutput = "[E][ ] project meeting (from: Aug 6 2023, 02:00 PM to: 04:00 PM)";

        runAndAssertTask(input, expectedOutput);
    }

    @Test
    void testMark() {
        String input = "mark 1" + END;

        // Assert
        String expectedOutput = "Got it. I've mark this task as done:";

        runAndAssertTask(input, expectedOutput);
    }
    @Test
    void testUnmark() {
        String input = "unmark 1" + END;

        // Assert
        String expectedOutput = "Got it. I've mark this task as undone:";

        runAndAssertTask(input, expectedOutput);
    }

    private void runAndAssertTask(String input, String expectedOutput) {
        // Save the original System.in and System.out
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        // Redirect System.out to capture the console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Create a new ByteArrayInputStream for the input
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in); // Redirect System.in to provide input

            // Act
            Duke duke = new Duke();
            duke.run();
        } finally {
            // Reset System.in and System.out to restore normal behavior
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        // Assert
        assertTrue(outContent.toString().contains(expectedOutput.trim()));
    }
}
