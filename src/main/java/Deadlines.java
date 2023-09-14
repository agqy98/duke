public class Deadlines extends Task {
    String deadline;

    public Deadlines(String instruction) {
        super("");
        String[] items = derive(instruction);
        this.description = items[0];
        this.deadline = items[1];
    }

    private String[] derive(String instruction) {
        String[] result = instruction.split("/");
        if (result.length != 2) {
            throw new IllegalArgumentException("Invalid format");
        } else {
            result[0] = result[0].trim();
            result[1] = result[1].replaceAll("by", "").trim();
            return result;
        }
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String getPrintStatus() {
        return "[" + (isDone ? "X" : " ") + "] " + description + " (by: " + deadline + ")";
    }
}
