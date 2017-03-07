package cs455.scaling.threadpool;

/**
 * Created by lozaet on 3/7/2017.
 */
public class WorkerThread extends Thread{
    private Runnable r;
    private ThreadTaskPool ttp;

    public WorkerThread(ThreadTaskPool ttp){
        this.ttp = ttp;
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
                    ttp.enqueueWorker(this);
                }
            }
        }
    }
}
