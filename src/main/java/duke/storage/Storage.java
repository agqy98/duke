package duke.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import duke.parser.Parser;
import duke.task.Task;

public class Storage {
    private static final String FOLDER_PATH = "./data";
    private static final String FILE_PATH = FOLDER_PATH + "/duke.txt";
    private static final String DUKE_PATH = "./text-ui-test/EXPECTED.TXT";

    public List<Task> loadTasks() {
        List<Task> list = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Check if the file exists before attempting to load
        if (!file.exists()) {
            // System.out.println("No saved tasks found. Starting with an empty task
            // list.");
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Parser.createTaskFromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveTasks(List<Task> list) {
        try {
            // Ensure the directory exists
            File directory = new File(FOLDER_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Now, save the tasks to the file
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                for (Task task : list) {
                    writer.write(task.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadDuke() {
        String result = "";
        File file = new File(DUKE_PATH);
        // Check if the file exists before attempting to load
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(DUKE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        return result;
    }

}
