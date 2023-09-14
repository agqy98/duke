public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public abstract String getType();

    public abstract String getPrintStatus();

    public void print() {
        System.out.println("[" + getType() + "]" + getPrintStatus());
    }
}
