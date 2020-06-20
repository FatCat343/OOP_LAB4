import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
class status{
    static int wait = 1;
    static int inprogress = 0;
    static int neverused = -1;
}
public class WThreadPool implements Executor {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    private final Vector<Thread> threads = new Vector<Thread>();
    private final Vector<Integer> threadstatus = new Vector<Integer>();
    //private final Queue<Thread> threads = new ConcurrentLinkedQueue<>();
    private volatile boolean isRunning = true;

    public WThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            //new Thread(new TaskWorker(), Integer.toString(i));
            threads.add(new Thread(new TaskWorker(), Integer.toString(i)));
            threadstatus.add(status.neverused);
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
//                if (threads.get(i).getState().equals(Thread.State.WAITING)) {
//                    System.out.println("Thread is waiting " + i);
//                    continue;
//                }
//                if (threads.get(i).getState().equals(Thread.State.NEW)) {
//                    System.out.println("Thread is new = " + i);
//                    threads.get(i).start();
//                    break;
//                }
                if (threadstatus.get(i).equals(status.wait)) {
                    threadstatus.set(i, status.inprogress);
                    threads.get(i).interrupt();
                    //System.out.println("Thread was waiting " + i);
                    continue;
                }
                if (threadstatus.get(i).equals(status.neverused)) {
                    threadstatus.set(i, status.inprogress);
                    //System.out.println("Thread was new = " + i);
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
        synchronized public void run() {
            String name = Thread.currentThread().getName();
            int position = Integer.parseInt(name);
            while (isRunning) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    //System.out.println("started task");
                    nextTask.run();
                    //threadstatus.set(position, status.wait);
                    //System.out.println("finished task");
                    //Thread t = Thread.currentThread();
                    //threads.offer(t);
                    //break;
                }
                else System.out.println("no task");
                threadstatus.set(position, status.wait);
                try {
                    wait();
                } catch (InterruptedException e) {
                }

            }
        }
    }
}
