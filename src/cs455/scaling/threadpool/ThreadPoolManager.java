package cs455.scaling.threadpool;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by eloza on 3/7/17.
 */
public class ThreadPoolManager extends Thread {
    private ThreadTaskPool ttp;

    public ThreadPoolManager(int numOfThreads) {
        ttp = new ThreadTaskPool(numOfThreads);
        WorkerThread tempThread;
    }

    public void startWorkers(){
        ttp.startThreadPool();
    }

    public void enqueueTask(Runnable r){
        ttp.enqueueTask(r);
    }

    public void run(){
        Runnable r;
        WorkerThread wt;
        while(true){
            r = ttp.dequeueTask();
            wt = ttp.dequeueWorker();
            wt.addTask(r);

        }

    }


}
