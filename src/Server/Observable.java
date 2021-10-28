package assessment_server;

/*
  The Interface of Observable(Subject of Observer pattern)
 
  @Minsik Jeong (21135840)
 */
import java.util.Vector;

public interface Observable {

    void addObserver(Observer o);

    void deleteObserver(Observer o);

    void notifyObservers(Task task);

}
