package duke.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import duke.command.TaskType;
import duke.error.ErrorType;
import duke.task.Task;

public class UI {
    public final Map<TaskType, String> TASK_KEYWORDS = new HashMap<>();
    {
        TASK_KEYWORDS.put(TaskType.LIST, "list");
        TASK_KEYWORDS.put(TaskType.TODO, "todo");
        TASK_KEYWORDS.put(TaskType.DEADLINE, "deadline");
        TASK_KEYWORDS.put(TaskType.EVENT, "event");
        TASK_KEYWORDS.put(TaskType.DELETE, "delete");
        TASK_KEYWORDS.put(TaskType.FIND, "find");
        TASK_KEYWORDS.put(TaskType.MARK, "mark");
        TASK_KEYWORDS.put(TaskType.UNMARK, "unmark");
    }

    public void printTaskList(List<Task> list) {
        printSeparator();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". ");
            list.get(i).print();
        }
        printSeparator();
    }
    public void printSelectedTasks(List<Task> originalList, List<Task> printList, TaskType t) {
        printSeparator();
        if (t == TaskType.FIND) {
            System.out.println("Here are the matching tasks in your list:");
        } else if (t == TaskType.TODO ||
                t == TaskType.EVENT ||
                t == TaskType.DEADLINE) {
            System.out.println("Unable to add due to a duplicate record.");
            System.out.println("Please provide a unique description or delete the existing record to proceed:\n");
        }
        for (int i = 0; i < originalList.size(); i++) {
            if (printList.size() == 0){
                break;
            }
            if (originalList.get(i).equals(printList.get(0))) {
                System.out.print(i + 1 + ". ");
                originalList.get(i).print();
                printList.remove(0);
            }
        }
        printSeparator();
    }

    public void printWelcomeMessage(String duke) {
        printSeparator();
        System.out.println(duke);
        System.out.println("I'm AngelBot!");
        System.out.println("What can I do for you?");
        printSeparator();
    }

    public void printGoodbyeMessage() {
        printSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }

    public void printErrorMessage(ErrorType e, TaskType t, int size) {
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
                        System.out.println("Date Format: yyyy-MM-dd");
                        break;
                        case EVENT:
                        System.out.println("event [description] /from [start date] /to [end date]");
                        System.out.println("Date Format: yyyy-MM-dd OR yyyy-MM-dd HH:mm");
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
                System.out.printf("Expect a number (1-%d) after %s", size, TASK_KEYWORDS.get(t));
                System.out.println("");
                break;
            case ERR_EXCEED_LIMIT:
                System.out.printf("The specified number exceeds the limit (1-%d)", size);
                System.out.println("");
                break;
            default:
                System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        printSeparator();
    }

    public static void printSeparator() {
        System.out.println("____________________________________________________________");
    }

}