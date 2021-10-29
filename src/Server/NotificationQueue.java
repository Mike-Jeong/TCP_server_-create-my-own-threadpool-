package Server;

import java.util.Iterator;
import java.util.Queue;
import java.util.AbstractQueue;
import java.util.Vector;

/*
  NotificationQueue inherits the AbstractQueue and inplemeents the Observable interface.
  The NotificationQueue objects are able to use the queue's method. 
  When NotificationQueue object's state has changed,  it call observer  
*/

public class NotificationQueue extends AbstractQueue<Task> implements Observable {

    private Queue<Task> queue; 
    private Vector<Observer> obs = new Vector<>();

    public NotificationQueue(Queue<Task> queue_i) {
        this.queue = queue_i;
    }   

    public synchronized void enqueue(Task task) throws InterruptedException {

        queue.add(task);
        System.out.println("Task has added in queue...");
        notifyObservers(this.poll());
        System.out.println("Task has polled in queue...");

    }

    public synchronized Task dequeue() throws InterruptedException {

        while (queue.isEmpty()) {

            System.out.println("dequeue wait");
            wait();

        }
        return queue.poll();
    }

    public synchronized int size() {
        return queue.size();
    }

    @Override
    public Iterator<Task> iterator() {
        return queue.iterator();
    }

    @Override
    public boolean offer(Task e) {
        return queue.offer(e);
    }

    @Override
    public Task poll() {
        return queue.poll();
    }

    @Override
    public Task peek() {
        return queue.peek();
    }

    @Override
    public void addObserver(Observer o) {

        if (o == null) {
            throw new NullPointerException();
        }
        if (!obs.contains(o)) {
            obs.add(o);
        }
    }

    @Override
    public void deleteObserver(Observer o) {
        obs.removeElement(o);
    }

    @Override
    public void notifyObservers(Task task) {
        System.out.println("Number of observer : " + obs.size() + ". Are called");
        for (Observer o : obs) {
            o.process(task);
        }
    }

}
