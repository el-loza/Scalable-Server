package cs455.scaling.threadpool;

import java.util.LinkedList;

/**
 * Created by eloza on 3/7/17.
 */
public class SimpleThreadPool {
    private TaskThread[] threads;
    private LinkedList<Runnable> taskQueue;

    public SimpleThreadPool(int threadNumber) {
        taskQueue = new LinkedList<Runnable>();
        threads = new TaskThread[threadNumber];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TaskThread();
            threads[i].start();
        }
    }

    public void enqueue(Runnable r) {
        synchronized (taskQueue) {
            taskQueue.addLast(r);
            taskQueue.notify();
        }
    }

    public class TaskThread extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                    r = (Runnable) taskQueue.removeFirst();
                }
                try {
                    r.run();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }
}
