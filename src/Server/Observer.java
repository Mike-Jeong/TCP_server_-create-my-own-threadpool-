package assessment_server;

/*
  The Interface of Observer(Observer of Observer pattern)
 
  @Minsik Jeong (21135840)
 */
interface Observer {

    default void process(Task task) {
    }
}
