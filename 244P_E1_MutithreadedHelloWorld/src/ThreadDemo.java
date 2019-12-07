import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;

class ThreadedObject extends Thread {
    int num;

    ThreadedObject(int num) {
        super();
        this.num = num;
        start();
    }

    public void run() {
        final DateFormat stf = new SimpleDateFormat("HH:mm:ss");

        try {
            while (true) {
                Date date = new Date();
                System.out.println("Hello World! I'm thread " + num + ". The time is " + stf.format(date));
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex) {
            return;
        }
    }
}

public class ThreadDemo {
    public static void main(String[] args) {
        boolean exit = false;
        ArrayList<ThreadedObject> list = new ArrayList<>();
        int counter = 1;

        while(!exit) {
            System.out.println("Here are your options:");
            System.out.println("a - Create a new thread.");
            System.out.println("b - Stop a given thread. (Format: b 2, e.g. \"b 2\" kills thread 2)");
            System.out.println("c - Stop all threads and exit this program.");

            Scanner scan = new Scanner(System.in);
            String inputString = scan.nextLine();
            switch (inputString) {
                case "a": {
                    list.add(new ThreadedObject(counter++));
                    break;
                }
                case "c": {
                    for(ThreadedObject o : list) o.interrupt();
                    System.out.print("All threads are stopped, exiting the program.");
                    exit = true;
                    break;
                }
                default: {
                    if(inputString.length() != 3 || inputString.charAt(0) != 'b' || inputString.charAt(1) != ' '
                            || inputString.charAt(2) < '0' || inputString.charAt(2) > '9'){
                        System.out.println("This is not an option!");
                    }
                    else {
                        for(ThreadedObject o : list) {
                            if(o.num == (inputString.charAt(2) - '0')) {
                                o.interrupt();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
