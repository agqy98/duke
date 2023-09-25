import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Duke {
    private static int list_count = 0;
    private static Task[] list = new Task[100];
    private static final Map<TaskType, String> TASK_KEYWORDS = new HashMap<>();

    static {
        TASK_KEYWORDS.put(TaskType.TODO, "todo");
        TASK_KEYWORDS.put(TaskType.DEADLINE, "deadline");
        TASK_KEYWORDS.put(TaskType.EVENT, "event");
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
        } else {
            TaskType taskType = getTaskType(input);
            if (taskType == null) {
                printErrorMessage(ErrorType.ERR_SYSTEM_READ_FAIL, taskType);
                return;
            } else {
                ErrorType err = hasError(input, taskType);
                if (err != null) {
                    printErrorMessage(err, taskType);
                    return;
                } else {
                    try {
                        System.out.println(input);
                        System.out.println(TASK_KEYWORDS.get(taskType).length());
                        String taskDescription = input.substring(TASK_KEYWORDS.get(taskType).length() + 1);
                        if (taskDescription.isEmpty()) {
                            printErrorMessage(ErrorType.ERR_EMPTY_DESCRIPTION, taskType);
                            return;
                        } else {
                            taskDescription = taskDescription.substring(1);
                            switch (taskType) {
                                case EVENT:
                                    addTask(new Events(taskDescription));
                                    break;
                                case DEADLINE:
                                    addTask(new Deadlines(taskDescription));
                                    break;
                                default:
                                    addTask(new ToDos(taskDescription));
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        // Handle invalid format error
                        printErrorMessage(ErrorType.ERR_INVALID_FORMAT, taskType);
                        return;
                    }
                }
            }
        }
    }

    private static ErrorType hasError(String input, TaskType t) {
        // Input begins with "[command]"
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

    private static void addTask(Task task) {
        list[list_count] = task;
        list_count++;
        printSeparator();
        System.out.println("Got it. I've added this task:");
        task.print();
        System.out.println("Now you have " + list_count + " task(s) in the list");
        printSeparator();
    }

    private static void printTaskList() {
        printSeparator();
        for (int i = 0; i < list_count; i++) {
            System.out.print(i + 1 + ". ");
            list[i].print();
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
            default:
                System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        printSeparator();
    }

    private static void printSeparator() {
        System.out.println("____________________________________________________________");
    }
}
