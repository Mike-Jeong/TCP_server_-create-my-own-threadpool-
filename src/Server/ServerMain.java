package Server;
/*
  The class that include the main method which is starting point of the server
 */

public class ServerMain {

    
    public static void main(String[] args) throws Exception {

        ProcessServer server = new ProcessServer(); // Instance Process server
        server.startServer(); //Start server

    }

}
