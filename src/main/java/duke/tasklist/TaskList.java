package duke.tasklist;

import java.util.ArrayList;
import java.util.List;

import duke.task.Task;
import duke.ui.UI;

public class TaskList {

    private List<Task> list;

    public TaskList() {
        this.list = new ArrayList<>();
    }

    public TaskList(List<Task> list) {
        this.list = list;
    }

    public int getListSize() {
        return list.size();
    }

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    public List<Task> searchList(String keyword, boolean isExactSearch, boolean skipSlashCheck) {
        List<Task> resultList = new ArrayList<>();
        if (!list.isEmpty()) {

            if (!skipSlashCheck) {
                keyword = keyword.indexOf("/") != -1 ? keyword.split("/")[0] : keyword;
            }
            keyword = keyword.toLowerCase().trim();

            for (Task task : list) {
                String taskDescription = task.getDescription().toLowerCase();

                if (isExactSearch && taskDescription.equals(keyword)) {
                    resultList.add(task);
                } else if (!isExactSearch && taskDescription.contains(keyword)) {
                    resultList.add(task);
                }
            }
        }
        return resultList;
    }

    public void addTask(Task task) {
        list.add(task);
        UI.printSeparator();
        System.out.println("Got it. I've added this task:");
        task.print();
        System.out.println("Now you have " + list.size() + " task(s) in the list");
        UI.printSeparator();
    }

    public void deleteTask(int index) {
        Task existing = list.get(index);
        list.remove(index);

        UI.printSeparator();
        System.out.println("Noted. I've removed this task:");
        existing.print();
        System.out.println("Now you have " + list.size() + " task(s) in the list");
        UI.printSeparator();
    }

    public void markTask(int index, boolean isDone) {
        Task task = list.get(index);
        task.setIsDone(isDone);

        UI.printSeparator();
        System.out.println("Got it. I've mark this task as " + (isDone ? "done" : "undone") + ":");
        task.print();
        UI.printSeparator();
    }
}
