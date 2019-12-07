import net.jcip.annotations.GuardedBy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class TrafficController {
    private final BlockingQueue<Thread> queue = new ArrayBlockingQueue<>(20);
    private final Object o = new Object();

    public synchronized void enterLeft() {
        //synchronized (queue) {
            queue.add(Thread.currentThread());
            while (queue.peek() != Thread.currentThread()) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        //}
    }
    public synchronized void enterRight() {
        queue.add(Thread.currentThread());
        while (queue.peek() != Thread.currentThread()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public synchronized void leaveLeft() {
        queue.poll();
        notifyAll();
    }
    public synchronized void leaveRight() {
        queue.poll();
        notifyAll();
    }

}