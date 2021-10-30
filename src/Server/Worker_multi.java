package assessment_server;

import java.util.LinkedList;

/*
  Worker_multi class
 
  @Minsik Jeong (21135840)
 */
public class Worker_multi extends Worker
{
    LinkedList<Task> rlist;
    LinkedList<Integer> result_id;//For Pass the result 

    public Worker_multi(int max_num_thread, String w_name,  LinkedList<Task> result_list, LinkedList<Integer> result_ID) throws Exception 
    {
        super(max_num_thread, w_name);
        this.rlist = result_list;
        this.result_id = result_ID;
        this.in_wq = new LinkedList<>();
        
    }

    @Override
    public synchronized void processStep(Task a) throws InterruptedException 
    {
        System.out.println("wake up : " + workername);
        a.setnumber(a.getnumber()*2);
        System.out.println("Final resutl is : "+(a.getnumber()));

        rlist.add(a);//Pass the task to Result List
        result_id.add(rlist.indexOf(a), a.getclient_id());
    }

    
}
