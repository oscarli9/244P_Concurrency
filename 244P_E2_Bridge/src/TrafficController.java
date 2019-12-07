import net.jcip.annotations.GuardedBy;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class TrafficController {
    private final Object o = new Object();
    @GuardedBy("o") private volatile boolean carOnBridge = false;

    public synchronized void enterLeft() {
        if (carOnBridge) {
            try {
                while (carOnBridge) {
                    wait();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            carOnBridge = true;
        }
    }

    public synchronized void enterRight() {
        if (carOnBridge) {
            try {
                while (carOnBridge) {
                    wait();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            carOnBridge = true;
        }
    }

    public synchronized void leaveLeft() {
        carOnBridge = false;
        notify();
    }

    public synchronized void leaveRight() {
        carOnBridge = false;
        notify();
    }

}