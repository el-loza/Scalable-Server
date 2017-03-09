package cs455.scaling.threadpool;

/**
 * Created by lozaet on 3/7/2017.
 */
public class WorkerThread extends Thread{
    private Runnable r;
    private ThreadTaskPool ttp;
    private final Object taskLock = new Object();

    public WorkerThread(ThreadTaskPool ttp){
        this.ttp = ttp;
        r = null;
    }

    public void addTask(Runnable task){
        synchronized (taskLock){
            r = task;
            taskLock.notify();
        }
    }

    public void run() {

        while (true) {
            synchronized (taskLock) {
                while (r == null) {
                    try {
                        taskLock.wait();
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
                synchronized (taskLock){
                    r = null;
                    ttp.enqueueWorker(this);
                }
            }
        }
    }
}
