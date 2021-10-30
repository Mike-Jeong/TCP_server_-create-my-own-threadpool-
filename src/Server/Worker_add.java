package assessment_server;

import java.util.LinkedList;

/*
  Worker_add class
 
  @Minsik Jeong (21135840)
 */
public class Worker_add extends Worker {

    NotificationQueue queue2;//The queue that is passed the task from Worker_add

    public Worker_add(int max_num_thread, String w_name, NotificationQueue queue2) throws Exception {
        super(max_num_thread, w_name);
        this.queue2 = queue2;
        this.in_wq = new LinkedList<>();

    }

    @Override
    public synchronized void processStep(Task b) throws InterruptedException {
        
        
        System.out.println("wake up : " + workername);
        System.out.println("get number : " + b.getnumber());
        int c = b.getnumber() + 10;
        b.setnumber(c);

        System.out.println("The number has added 10. Now number is : " + b.getnumber());
        queue2.enqueue(b);//Pass the task to queue
        System.out.println("Add process done");
    }

}
