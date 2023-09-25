import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Duke {
    private static ArrayList<Task> list = new ArrayList<>();
    private static final Map<TaskType, String> TASK_KEYWORDS = new HashMap<>();

    static {
        TASK_KEYWORDS.put(TaskType.TODO, "todo");
        TASK_KEYWORDS.put(TaskType.DEADLINE, "deadline");
        TASK_KEYWORDS.put(TaskType.EVENT, "event");
        TASK_KEYWORDS.put(TaskType.DELETE, "delete");
    }

    public static void main(String[] args) {
        printWelcomeMessage();

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            input = scanner.nextLine().trim();
            processInput(input);
        } while (!input.equalsIgnoreCase("bye"));

        printGoodbyeMessage();
        scanner.close();
    }

    private static void processInput(String input) {
        if (input.equalsIgnoreCase("list")) {
            printTaskList();
            return;
        }
        TaskType taskType = getTaskType(input);
        if (taskType == null) {
            printErrorMessage(ErrorType.ERR_SYSTEM_READ_FAIL, taskType);
            return;
        }

        ErrorType err = hasError(input, taskType);
        if (err != null) {
            printErrorMessage(err, taskType);
            return;
        }

        String taskDescription = input.substring(TASK_KEYWORDS.get(taskType).length() + 1).trim();
        if (taskDescription.isEmpty()) {
            printErrorMessage(ErrorType.ERR_EMPTY_DESCRIPTION, taskType);
            return;
        }
        try {
            if (taskType == TaskType.DELETE) {
                try {
                    int intValue = Integer.parseInt(taskDescription);
                    if (intValue > 0 && intValue <= list.size()) {
                        deleteTaskToList(intValue - 1);
                    } else {
                        printErrorMessage(ErrorType.ERR_EXCEED_LIMIT, taskType);
                        return;
                    }
                } catch (NumberFormatException e) {
                    printErrorMessage(ErrorType.ERR_EXPECT_NUMBER, taskType);
                    return;
                }
            } else {
                addTaskToList(createTask(taskType, taskDescription));
            }
        } catch (IllegalArgumentException e) {
            printErrorMessage(ErrorType.ERR_INVALID_FORMAT, taskType);
            return;
        }
    }

    private static Task createTask(TaskType taskType, String taskDescription) {
        switch (taskType) {
            case EVENT:
                return new Events(taskDescription);
            case DEADLINE:
                return new Deadlines(taskDescription);
            default:
                return new ToDos(taskDescription);
        }
    }

    private static ErrorType hasError(String input, TaskType t) {
        // for input begins with "[command]"
        String command = TASK_KEYWORDS.get(t);
        if (input.trim().equalsIgnoreCase(command)) {
            return ErrorType.ERR_EMPTY_DESCRIPTION;
        } else if (input.trim().length() > command.length() &&
                !input.substring(command.length(), command.length() + 1).equals(" ")) {
            return ErrorType.ERR_POSSIBLE_TYPO;
        } else {
            return null;
        }

    }

    private static TaskType getTaskType(String input) {
        for (Map.Entry<TaskType, String> entry : TASK_KEYWORDS.entrySet()) {
            if (input.toLowerCase().startsWith(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static void addTaskToList(Task task) {
        list.add(task);
        printSeparator();
        System.out.println("Got it. I've added this task:");
        task.print();
        System.out.println("Now you have " + list.size() + " task(s) in the list");
        printSeparator();
    }

    private static void deleteTaskToList(int index) {
        Task existing = list.get(index);
        list.remove(index);

        for (Task l : list) {
            System.out.println(l.description);
        }
        printSeparator();
        System.out.println("Noted. I've removed this task:");
        existing.print();
        System.out.println("Now you have " + list.size() + " task(s) in the list");
        printSeparator();
    }

    private static void printTaskList() {
        printSeparator();
        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". ");
            list.get(i).print();
        }
        printSeparator();
    }

    private static void printWelcomeMessage() {
        printSeparator();
        System.out.println("Hello! I'm AngelBot!");
        System.out.println("What can I do for you?");
        printSeparator();
    }

    private static void printGoodbyeMessage() {
        printSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }

    private static void printErrorMessage(ErrorType e, TaskType t) {
        printSeparator();
        switch (e) {
            case ERR_EMPTY_DESCRIPTION:
                System.out.println("OOPS!!! The description of a " + TASK_KEYWORDS.get(t) + " cannot be empty.");
                break;
            case ERR_INVALID_FORMAT:
                System.out.printf(
                        "OOPS! It seems you didn't provide a valid %s format. To create a %s task, use the following format:",
                        TASK_KEYWORDS.get(t), TASK_KEYWORDS.get(t));
                System.out.println("");
                switch (t) {
                    case DEADLINE:
                        System.out.println("deadline [description] /by [date]");
                        break;
                    case EVENT:
                        System.out.println("event [description] /from [start time] /to [end time]");
                        break;
                    default:
                        System.out.println("todo [description]"); // Not expecting any formatting error here
                        break;
                }
                break;
            case ERR_POSSIBLE_TYPO:
                System.out.printf("OOPS! It appears there might be a typo. Did you mean to write '%s'?",
                        TASK_KEYWORDS.get(t));
                System.out.println("");
                break;
            case ERR_EXPECT_NUMBER:
                System.out.printf("Expect a number (1-%d) after %s", list.size(), TASK_KEYWORDS.get(t));
                System.out.println("");
                break;
            case ERR_EXCEED_LIMIT:
                System.out.printf("The specified number exceeds the limit (1-%d)", list.size());
                System.out.println("");
                break;
            default:
                System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        printSeparator();
    }

    private static void printSeparator() {
        System.out.println("____________________________________________________________");
    }
}
