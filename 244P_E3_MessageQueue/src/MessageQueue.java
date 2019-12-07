import net.jcip.annotations.GuardedBy;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageQueue {
    private static int n_ids;
    private final Object o = new Object();
    @GuardedBy("o") private volatile int countRunningProducer;
    public volatile int countRunningConsumer;

    void decreaseCountRunningProducer() {
        synchronized (o) {
            countRunningProducer--;
        }
    }

    int getCountRunningProducer() {
        synchronized (o) {
            return countRunningProducer;
        }
    }

    public static void main(String[] args) {
        MessageQueue mq = new MessageQueue();
        ArrayList<Producer> producers = new ArrayList<>();

        if (args.length == 2) {
            mq.countRunningProducer = Integer.parseInt(args[0]);
            mq.countRunningConsumer = Integer.parseInt(args[1]);
            BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

            for (n_ids = 0; n_ids < Integer.parseInt(args[0]); n_ids++) {
                Producer producer = new Producer(queue, n_ids, mq);
                producers.add(producer);
                new Thread(producer).start();
            }
            for (n_ids = Integer.parseInt(args[0]); n_ids < Integer.parseInt(args[0]) + Integer.parseInt(args[1]); n_ids++) {
                new Thread(new Consumer(queue, n_ids)).start();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                producers.get(i).stop();
            }
        }
    }
}
