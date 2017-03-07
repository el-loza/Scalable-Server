package cs455.scaling.threadpool;

/**
 * Created by lozaet on 3/7/2017.
 */
public class Test2 {
    public static void main(String[] args) {
        ThreadPoolManager tpm = new ThreadPoolManager(2);
        tpm.startWorkers();
        tpm.start();
        tpm.enqueueTask(new Test2.TestTask1());
        tpm.enqueueTask(new Test2.TestTask2());
        for (int i = 0; i < 5; i++){
            tpm.enqueueTask(new Test2.TestTask1());
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++){
            tpm.enqueueTask(new Test2.TestTask2());
        }
    }

    private static class TestTask1 implements Runnable {
        @Override
        public void run() {
            System.out.println("Starting Thread");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A");
            }
            System.out.println("Ending Thread");
        }
    }

    private static class TestTask2 implements Runnable {
        @Override
        public void run() {
            System.out.println("Starting Thread");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B");
            }
            System.out.println("Ending Thread");
        }
    }
}
