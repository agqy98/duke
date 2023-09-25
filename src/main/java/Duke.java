import java.util.Scanner;

public class Duke {
    private static int count = 0;
    private static Task[] list = new Task[100];

    public static void main(String[] args) {
        printHelloWorld();

        Scanner scanner = new Scanner(System.in);
        System.out.print("");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("bye")) {
            if (input.equalsIgnoreCase("list")) {
                printList();
            } else {
                handleInput(input);
            }
            input = scanner.nextLine();
        }
        printByeWorld();
        scanner.close();
    }

    private static void formatter() {
        System.out.print("    ");
    }

    private static void printList() {
        printSeparator();
        for (int i = 0; i < count; i++) {
            formatter();
            System.out.print(i + 1);
            System.out.print(". ");
            list[i].print();
        }
        printSeparator();
    }

    private static boolean validateInput(String input, String command) {
        int size = command.length();
        return input.length() > size && input.substring(0, size).equalsIgnoreCase(command);
    }

    private static void handleInput(String input) {
        input = input.trim();
        Boolean isT = validateInput(input, "todo");
        Boolean isD = validateInput(input, "deadline");
        Boolean isE = validateInput(input, "event");

        if (isT || isD || isE) {
            Task item;
            if (isT) {
                item = new ToDos(input.substring(5));
            } else if (isD) {
                item = new Deadlines(input.substring(9));
            } else {
                item = new Events(input.substring(6));
            }
            list[count] = item;
            count++;

            printSeparator();
            formatter();
            System.out.println("Got it. I've added this task:");
            formatter();
            formatter();
            item.print();
            formatter();
            System.out.println("Now you have " + count + " task(s) in the list");
            printSeparator();

        } else {
            printSeparator();
            formatter();
            System.out.println("Invalid command");
            printSeparator();
        }
    }

    private static void printSeparator() {
        formatter();
        System.out.println("____________________________________________________________");
    }

    private static void printHelloWorld() {
        printSeparator();
        formatter();
        System.out.println("Hello! I'm AngelBot!");
        formatter();
        System.out.println("What can I do for you?");
        printSeparator();
    }

    private static void printByeWorld() {
        printSeparator();
        formatter();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();

    }
}
