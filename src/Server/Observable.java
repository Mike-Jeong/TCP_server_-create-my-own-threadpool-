package Server;

public interface Observable {

    void addObserver(Observer o);

    void deleteObserver(Observer o);

    void notifyObservers(Task task);

}
