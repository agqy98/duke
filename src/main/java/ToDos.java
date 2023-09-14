public class ToDos extends Task {
    public ToDos(String description) {
        super(description);
    }

    @Override
    public String getType() {
        return "T";
    }

    @Override
    public String getPrintStatus() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}