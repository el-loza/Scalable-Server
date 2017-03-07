package cs455.scaling.threadpool;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by lozaet on 3/7/2017.
 */
public class ThreadTaskPool {

    private LinkedList<WorkerThread> workers;
    private LinkedList<Runnable> tasks;

    public ThreadTaskPool(int numOfThreads){
        tasks = new LinkedList<Runnable>();
        workers = new LinkedList<WorkerThread>();
        WorkerThread tempThread;
        for (int i = 0; i < numOfThreads; i++) {
            tempThread = new WorkerThread(this);
            workers.push(tempThread);
        }
    }

    public void startThreadPool(){
        synchronized (workers){
            ListIterator<WorkerThread> listIterator = workers.listIterator();
            while (listIterator.hasNext()){
                listIterator.next().start();
            }
        }
    }

    public WorkerThread dequeueWorker(){
        synchronized (workers){
            while(workers.isEmpty()){
                try {
                    workers.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return workers.remove();
        }
    }

    public void enqueueWorker(WorkerThread worker){
        synchronized (workers){
            workers.addLast(worker);
            workers.notify();
        }
    }

    public Runnable dequeueTask(){
        synchronized (tasks){
            while(tasks.isEmpty()){
                try {
                    tasks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return tasks.remove();
        }
    }

    public void enqueueTask(Runnable task){
        synchronized (tasks){
            tasks.addLast(task);
            tasks.notify();
        }
    }
}
