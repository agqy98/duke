public class Events extends Task {
    String from;
    String to;

    public Events(String instruction) {
        super("");
        String[] items = derive(instruction);
        this.description = items[0];
        this.from = items[1];
        this.to = items[2];
    }

    private String[] derive(String instruction) {
        String[] result = instruction.split("/");
        if (result.length != 3) {
            throw new IllegalArgumentException("Invalid format");
        } else {
            result[0] = result[0];
            // We are not handling if they put invalid dates
            // We simply take the 2nd and 3rd values and store it as from and to for now.
            result[1] = result[1].replaceAll("from", "").trim();
            result[2] = result[2].replaceAll("to", "").trim();
            return result;
        }
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String getPrintStatus() {
        return "[" + (isDone ? "X" : " ") + "] " + description + " (from: " + from + " to:" + to + ")";
    }
}
