import java.util.concurrent.*;

public class Main3 {
    private static final Semaphore semaphore = new Semaphore(1);

    private static void nap(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void singleAdd(HighLevelDisplay d, int i) {
        try {
            semaphore.acquire();
            d.addRow("AAAAAAAAAA " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void singleDelete(HighLevelDisplay d) {
        try {
            semaphore.acquire();
            d.deleteRow(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void addProc(HighLevelDisplay d) {

	// Add a sequence of addRow operations with short random naps.
        for(int i=0; i < 20; i++) {
            singleAdd(d, i);
            nap(100);
        }
   }

    private static void deleteProc(HighLevelDisplay d) {
	
	// Add a sequence of deletions of row 0 with short random naps.
        for(int i=0; i < 20; i++) {
            singleDelete(d);
            nap(200);
        }
    }

    public static void main(String [] args) {
	final HighLevelDisplay d = new JDisplay2();

	new Thread () {
	    public void run() {
		addProc(d);
	    }
	}.start();


	new Thread () {
	    public void run() {
		deleteProc(d);
	    }
	}.start();

    }
}