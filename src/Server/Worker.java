package Server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Worker {

    private List<Workerthreads> workerthreads = new ArrayList<Workerthreads>();//List of threads that belong to worker
    private boolean running = true;
    protected String workername;
    protected int max_num_thread;
    protected Queue<Task> in_wq = new LinkedList<>();//Inner queue for Task which is coming from NotificationQueu

    public Worker(int max_num_thread, String w_name) throws Exception {

        workername = w_name;
        this.max_num_thread = max_num_thread;
        if (!running) {
            throw new Exception(String.format("{0} is not working. ", workername));
        }

        for (int i = 0; i < max_num_thread; i++) {
            workerthreads.add(new Workerthreads(i));
        }

        for (Workerthreads wt : workerthreads) {
            new Thread(wt).start();
        }
        
        //create number of thread and store it in workerthreads
        //And start Threads

    }

    public synchronized Task dequeue() throws InterruptedException {

        while (in_wq.isEmpty() || in_wq == null) {

            System.out.println("dequeue wait");
            wait();

        }

        return in_wq.poll();
        
        //dequeue the task from innerqueue
    }

    public synchronized void process(Task task) {

        in_wq.add(task);
        notify();
        //Add the task in innerqueue and wake up the thread 

    }

    public abstract void processStep(Task a) throws InterruptedException;

    public synchronized void requestStop() throws InterruptedException {
        running = false;

        for (Workerthreads wt : workerthreads) {
            wt.stop();
        }

    }//method for requestStop

    
    //Innerclass of Worker
    public class Workerthreads implements Runnable {

        private int id;

        private volatile boolean running = true;

        public Workerthreads(int THREAD_ID) {
            this.id = THREAD_ID;

            System.out.println("ThreadPoolRunnable[" + id + "] is created. " + workername);
        }

        @Override
        public void run() {

            while (running) {
                try {

                    System.out.println("ThreadPoolRunnable[" + id + "] is working. " + workername);

                    processStep(dequeue());

                    System.out.println("ThreadPoolRunnable[" + id + "] done. " + workername);

                } catch (InterruptedException e) {
                    stop(); // 인터럽트 예외 발생시 해당 Runnable 정지
                }
            }
            System.out.println("ThreadPoolRunnable[" + id + "] is dead. " + workername);
        }

        public void stop() {
            running = false;
            System.out.println("ThreadPoolRunnable[" + id + "] is stopped. " + workername);
        }

    }

}
