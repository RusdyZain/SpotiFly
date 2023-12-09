import java.util.TimerTask;

public class TimerTaskRunner extends TimerTask {
    private Runnable task;

    public TimerTaskRunner(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }
}
