package cs455.scaling.threadpool;

/**
 * Created by eloza on 3/7/17.
 */
public class Test {
    public static void main(String[] args) {
        SimpleThreadPool stp = new SimpleThreadPool(2);
        stp.enqueue(new TestTask1());
        stp.enqueue(new TestTask2());
        for (int i = 0; i < 5; i++){
            stp.enqueue(new TestTask1());
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++){
            stp.enqueue(new TestTask2());
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
