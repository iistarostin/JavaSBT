package hw.threading;

public class TaskCallException extends RuntimeException {
    public TaskCallException(Exception e) {
        super(e);
    }
}
