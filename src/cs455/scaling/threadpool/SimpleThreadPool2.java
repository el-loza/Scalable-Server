package cs455.scaling.threadpool;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by eloza on 3/7/17.
 */
public class SimpleThreadPool2 {
    private LinkedList<WorkerThread2> workers;
    private LinkedList<Runnable> tasks;

    public SimpleThreadPool2(int numOfThreads) {
        tasks = new LinkedList<Runnable>();
        workers = new LinkedList<WorkerThread2>;
        WorkerThread2 tempThread;
        for (int i = 0; i < numOfThreads; i++) {
            tempThread = new WorkerThread2();
            workers.push(tempThread);
        }
    }

    public void startThreadPool(){
        ListIterator<WorkerThread2> listIterator = workers.listIterator();
        while (listIterator.hasNext()){
            listIterator.next().start();
        }
    }

    public void enqueue(Runnable r) {
        synchronized (tasks) {
            tasks.addLast(r);
            tasks.notify();
        }
    }

    public void startManager(){

    }

    public class WorkerThread2 extends Thread {
        private Runnable r;

        public WorkerThread2(){
            r = null;
        }

        public void addTask(Runnable task){
            synchronized (r){
                r = task;
                r.notify();
            }
        }

        public void run() {

            while (true) {
                synchronized (r) {
                    while (r == null) {
                        try {
                            r.wait();
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                }
                try {
                    r.run();
                } catch (Exception e) {
                    // ignore
                } finally {
                    synchronized (r){
                        r = null;
                    }
                }
            }
        }
    }
}
