import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class WThreadPool implements Executor {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    private final Vector<Thread> threads = new Vector<Thread>();
    //private final Queue<Thread> threads = new ConcurrentLinkedQueue<>();
    private volatile boolean isRunning = true;

    public WThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            //new Thread(new TaskWorker(), Integer.toString(i));
            threads.add(new Thread(new TaskWorker(), Integer.toString(i)));
        }
        //System.out.println("created wtp with size = "+threads.size());
    }
    synchronized public void execute(Runnable command) {
        //int free = threads.size();
        //System.out.println("threads capacity = "+ threads.size());
        workQueue.offer(command);
        if ((isRunning)/* && (free >= threads.size())*/) {
            //Runnable nextTask = workQueue.poll();
            int i;
            for (i = 0; i < threads.size(); i++){
                //System.out.println("check thread " + i + "for being terminated");
                if (threads.get(i).getState().equals(Thread.State.WAITING)) continue;
                if (threads.get(i).getState().equals(Thread.State.NEW)) {
                    threads.get(i).start();
                    break;
                }
            }
            //free = i;

        }
//        while (isRunning) {
//            Thread t = threads.poll();
//            if (t != null) {
//                System.out.println("got thread "+t.getName()+ " with state "+ t.getState());
//                t.start();
//                break;
//            }
            //System.out.println("found thread "+free);

        //}

    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {
        public void run() {
            while (isRunning) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    //System.out.println("started task");
                    nextTask.run();
                    //System.out.println("finished task");
                    //Thread t = Thread.currentThread();
                    //threads.offer(t);
                    //break;
                }

            }
        }
    }
}
