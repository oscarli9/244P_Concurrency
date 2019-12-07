import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private BlockingQueue<Message> queue;
    private boolean running = true;
    private final int id;
    private final MessageQueue mq;

    public Producer(BlockingQueue<Message> q, int n, MessageQueue mq) {
		queue = q;
		id = n;
		this.mq = mq;
    }

    public void stop() {
	running = false;
    }

    public void run() {
		int count = 0;
		int countRunningProducer;
		while (running) {
			int n = RandomUtils.randomInteger();
			try {
				Thread.sleep(n);
				Message msg = new Message("message-" + n);
				queue.put(msg); // Put the message in the queue
				count++;
				RandomUtils.print("Produced " + msg.get(), id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Put the stop message in the queue
        synchronized (mq) {
            countRunningProducer = mq.getCountRunningProducer();
            if (countRunningProducer > 1) {
                Message msg = new Message("stop");
                try {
                    queue.put(msg); // Put this final message in the queue
                    mq.decreaseCountRunningProducer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (int i=0; i<mq.countRunningConsumer; i++) {
                    Message msg = new Message("stop");
                    try {
                        queue.put(msg); // Put this final message in the queue
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mq.decreaseCountRunningProducer();
            }
        }
		RandomUtils.print("Messages sent: " + count, id);
	}
}
