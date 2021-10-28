package Server;

interface Observer {

    default void process(Task task) {
    }
}
