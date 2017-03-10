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
                //System.out.println("WORKERTHREAD: attempting to run task");
                r.run();
                //System.out.println("WORKERTHREAD: WHY ID i get kicked out");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("WORKERTHREAD: This is an error ");
                e.printStackTrace();
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
