package duke;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import duke.command.TaskType;
import duke.error.ErrorType;
import duke.ui.UI;
import duke.storage.Storage;
import duke.tasklist.TaskList;

import duke.task.Events;
import duke.task.Deadlines;
import duke.task.ToDos;
import duke.task.Task;

/**
 * The main class for the Duke task management application.
 * Duke allows users to manage and organize their tasks through a command-line
 * interface.
 */
public class Duke {
    /**
     * The user interface component for Duke.
     */
    private UI ui;
    /**
     * The storage component for Duke, responsible for loading and saving tasks.
     */
    private Storage storage;
    /**
     * The task list component for Duke, containing the user's tasks.
     */
    private TaskList taskList;

    /**
     * Constructs a Duke instance, initializing the UI, storage, and task list.
     */
    public Duke() {
        this.ui = new UI();
        this.storage = new Storage();
        this.taskList = new TaskList(storage.loadTasks());
    }

    /**
     * Runs the Duke application, handling user input and processing commands.
     * Displays welcome and goodbye messages, and saves tasks to file after each
     * operation.
     */
    public void run() {
        ui.printWelcomeMessage(storage.loadDuke());

        Scanner scanner = new Scanner(System.in);
        String input;

        try {
            while (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
                processInput(input);
                if (input.equalsIgnoreCase("bye")) {
                    break;
                }
            }
        } finally {
            scanner.close();
        }
        ui.printGoodbyeMessage();
    }

    public static void main(String[] args) {
        new Duke().run();
    }

    /**
     * Processes the user input and performs actions based on the recognized
     * commands.
     * 1. List
     * 2. Identify Task Type (Skip List)
     * 3. Confirm it is in a valid format
     * 4. Map input based on command
     * 
     * @param input The input provided by the user.
     */
    public void processInput(String input) {
        if (input.toLowerCase().startsWith(ui.TASK_KEYWORDS.get(TaskType.LIST))) {
            ui.printTaskList(taskList.getList());
            return;
        }

        TaskType taskType = anyTaskType(input, ui.TASK_KEYWORDS);
        if (taskType == null) {
            ui.printErrorMessage(ErrorType.ERR_SYSTEM_READ_FAIL, taskType, taskList.getListSize());
            return;
        }

        String command = ui.TASK_KEYWORDS.get(taskType);
        ErrorType err = anyError(input, command);
        if (err != null) {
            ui.printErrorMessage(err, taskType, taskList.getListSize());
            return;
        }

        try {
            String taskDescription = input.substring(command.length() + 1).trim();
            // FIND
            if (taskType == TaskType.FIND) {
                taskFind(taskDescription, taskType == TaskType.TODO);
            }
            // DELETE, MARK, UNMARK
            else if (taskType == TaskType.DELETE ||
                    taskType == TaskType.MARK ||
                    taskType == TaskType.UNMARK) {
                try {
                    int intValue = Integer.parseInt(taskDescription);
                    processIndexCommand(intValue, taskType);
                } catch (NumberFormatException e) {
                    ui.printErrorMessage(ErrorType.ERR_EXPECT_NUMBER, taskType, taskList.getListSize());
                }
            }
            // OTHERS
            else {
                List<Task> duplicatedTasks = anyDuplicate(taskDescription, taskType);

                if (!duplicatedTasks.isEmpty()) {
                    ui.printSelectedTasks(taskList.getList(), duplicatedTasks, taskType);
                } else {
                    taskAdd(taskDescription, taskType);
                }
            }
        } catch (IllegalArgumentException e) {
            ui.printErrorMessage(ErrorType.ERR_INVALID_FORMAT, taskType, taskList.getListSize());
            return;
        }
    }

    /**
     * Adds a task to the task list based on the provided task description and task
     * type.
     *
     * @param taskDescription The description of the task to be added.
     * @param taskType        The type of the task to be added.
     */
    public void taskAdd(String taskDescription, TaskType taskType) {
        assert taskDescription != null : "Task description is null in taskAdd method";
        assert taskType != null : "TaskType is null in taskAdd method";
        Task t = instantiateTask(taskType, taskDescription);
        taskList.addTask(t);
        storage.saveTasks(taskList.getList()); // Save tasks to file after each
    }

    /**
     * Finds and prints tasks in the task list that match the provided task
     * description,
     * considering whether to skip slash check based on the specified flag.
     *
     * @param taskDescription The description of the task to be found.
     * @param skipSlashCheck  A flag indicating whether to skip slash check during
     *                        the search.
     */
    public void taskFind(String taskDescription, boolean skipSlashCheck) {
        assert taskDescription != null : "Task description is null in taskFind method";
        List<Task> resultTaskList = taskList.searchList(taskDescription, false, skipSlashCheck);
        ui.printSelectedTasks(taskList.getList(), resultTaskList, TaskType.FIND);
    }

    /**
     * Marks or unmarks a task at the specified index in the task list based on the
     * provided flag.
     *
     * @param index  The index of the task to be marked or unmarked.
     * @param isDone A flag indicating whether to mark or unmark the task.
     */
    public void taskMark(int index, boolean isDone) {
        assert index >= 0 : "Index is negative in taskMark method";
        taskList.markTask(index, isDone);
        storage.saveTasks(taskList.getList()); // Save tasks to file after each change
    }

    /**
     * Deletes a task at the specified index in the task list.
     *
     * @param index The index of the task to be deleted.
     */
    public void taskDelete(int index) {
        assert index >= 0 : "Index is negative in taskDelete method";
        taskList.deleteTask(index);
        storage.saveTasks(taskList.getList()); // Save tasks to file after each change
    }

    /**
     * Processes the index-based commands such as DELETE, MARK, and UNMARK based on
     * the provided task description
     * and task type.
     *
     * @param taskDescription The index of the task to be
     *                        processed.
     * @throws AssertionError If the assertion fails
     *                        (taskDescription is not greater than 0).
     * 
     * @param taskType The type of the task to be processed (DELETE, MARK, or
     *                 UNMARK).
     */
    public void processIndexCommand(int taskDescription, TaskType taskType) {
        assert taskDescription > 0 : "selection should be greater than 0";
        if (taskDescription > 0 && taskDescription <= taskList.getListSize()) {
            int index = taskDescription - 1;
            // DELETE
            if (taskType == TaskType.DELETE) {
                taskDelete(index);
            }
            // MARK / UNMARK
            else if (taskType == TaskType.MARK ||
                    taskType == TaskType.UNMARK) {
                taskMark(index, taskType == TaskType.MARK);
            }
        } else {
            ui.printErrorMessage(ErrorType.ERR_EXCEED_LIMIT, taskType, taskList.getListSize());
        }
    }

    /**
     * Instantiates a new Task based on the provided task type and task description.
     *
     * @param taskType        The type of the task (EVENT, DEADLINE, TODO, etc)
     * @param taskDescription The description of the task.
     * @return A new Task object corresponding to the given task type and
     *         description.
     */
    public static Task instantiateTask(TaskType taskType, String taskDescription) {
        switch (taskType) {
            case EVENT:
                return new Events(taskDescription);
            case DEADLINE:
                return new Deadlines(taskDescription);
            default:
                return new ToDos(taskDescription);
        }
    }

    /**
     * Checks for any duplicate tasks in the task list based on the provided task
     * description and type.
     *
     * @param taskDescription The description of the task.
     * @param taskType        The type of the task (EVENT, DEADLINE, TODO, etc)
     * @return A list of duplicate tasks from the task list, filtered by description
     *         and type.
     */
    private List<Task> anyDuplicate(String taskDescription, TaskType taskType) {
        List<Task> resultTaskList = taskList.searchList(taskDescription, true, taskType == TaskType.TODO);

        // Create an iterator to safely remove elements while iterating
        Iterator<Task> iterator = resultTaskList.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (!task.getTaskType().equals(taskType)) {
                iterator.remove();
            }
        }

        return resultTaskList;
    }

    /**
     * Checks for any errors in the user input based on the provided input and
     * command.
     *
     * @param input   The user input.
     * @param command The expected command.
     * @return The type of error encountered (ERR_EMPTY_DESCRIPTION,
     *         ERR_POSSIBLE_TYPO), or null if no error.
     */
    private ErrorType anyError(String input, String command) {
        if (input.trim().equalsIgnoreCase(command)) {
            return ErrorType.ERR_EMPTY_DESCRIPTION;
        }
        // A valid command should have a space afterward
        else if (!input.startsWith(command) || (input.trim().length() > command.length() &&
                !input.substring(command.length(), command.length() + 1).equals(" "))) {
            return ErrorType.ERR_POSSIBLE_TYPO;
        }

        return null;

    }

    /**
     * Determines the task type based on the provided input and a map of task types
     * and their corresponding commands.
     *
     * @param input The user input.
     * @param list  A map containing task types and their corresponding commands.
     * @return The identified task type or null if no match is found.
     */
    private TaskType anyTaskType(String input, Map<TaskType, String> list) {
        for (Map.Entry<TaskType, String> entry : list.entrySet()) {
            String value1 = entry.getValue();
            if (input.length() >= value1.length()) {
                String value2 = input.toLowerCase().substring(0, value1.length());

                if (areStringsSimilar(value1, value2)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Checks if two strings are similar, allowing for a single character
     * difference.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return True if the strings are similar, false otherwise.
     */
    private static boolean areStringsSimilar(String s1, String s2) {
        if (s1.equals(s2)) {
            return true;
        }

        int differences = 0;
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
                if (differences > 1) {
                    return false; // More than one difference, not similar
                }
            }
        }

        return differences == 1; // Only one difference, strings are similar
    }
}
