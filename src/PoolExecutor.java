import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class PoolExecutor implements Executor {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    //private final List<Thread> threads = new ArrayList<Thread>();
    private volatile boolean isRunning = true;

    public PoolExecutor(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            new Thread(new TaskWorker(), Integer.toString(i)).start();
            //threads.add(new Thread(new TaskWorker(), Integer.toString(i)));
        }
    }
    public void execute(Runnable command) {
        if (isRunning) {
            workQueue.offer(command);
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {
        public void run() {
            while (isRunning) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    nextTask.run();
                }
            }
        }
    }
}