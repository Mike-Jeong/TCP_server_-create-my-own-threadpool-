package Server;

/*
  ProcessServer class
 
  @Minsik Jeong (21135840)
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class ProcessServer {

    private static boolean stopRequested;
    public static final int PORT = 7777; //Input port number
    public static final int PORT2 = 7778; //Output port number
    static ServerSocket serverSocketI; // Input server socket
    static ServerSocket serverSocketO;//Output server socket
    static List<Client> connections = new Vector<Client>();//For store the client's information
    NotificationQueue n = new NotificationQueue(new LinkedList<Task>());//NotificationQueue for store task(From Client)
    NotificationQueue n2 = new NotificationQueue(new LinkedList<Task>());//NotificationQueue for store task(From Worker_add)
    static LinkedList<Task> result_l = new LinkedList<>();//LinkedList for final result of Task
    static LinkedList<Integer> result_id = new LinkedList<>();//LinkedList for final result of Task's index
    static Worker p;
    static Worker p2;

    public ProcessServer() {
        stopRequested = false;
    }

    public void startServer() throws Exception {
        stopRequested = false;
        serverSocketI = null;
        serverSocketO = null;

        try {
            serverSocketI = new ServerSocket(PORT);//Create Input ServerSocket with Input Port 
            serverSocketO = new ServerSocket(PORT2);//Create Output ServerSocket with Input Port
            System.out.println("Server started at "
                    + InetAddress.getLocalHost() + " on port " + PORT + ". Input");
            System.out.println("Server started at "
                    + InetAddress.getLocalHost() + " on port " + PORT2 + ". Input");
        } catch (IOException e) {
            System.err.println("Server can't listen on port: " + e);
            System.exit(-1);
        }

        p = new Worker_add(2, "addworker", n2);//Create Worker_add
        p2 = new Worker_multi(2, "multiworker", result_l, result_id);//Create Worker_multiply

        n.addObserver(p);//Make Workder_add Listen NotificationQueue q's changes 
        n2.addObserver(p2);//Make Workder_add Listen NotificationQueue q's changes 

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("[Starting]");
                while (!stopRequested) {
                    try {

                        Socket socket = serverSocketI.accept();
                        Socket socket2 = serverSocketO.accept();//Create input/output socket
                        System.out.println("[Accept connection: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]");
                        System.out.println("[Accept connection: " + socket2.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]");
                        Client client = new Client(socket, socket2);//Create client object
                        connections.add(client);//Store client in Lisf of client
                        System.out.println("[Number of connections: " + connections.size() + "]");// show number of connections in server
                    } catch (Exception e) {
                        if (!serverSocketI.isClosed()) {
                            requestStop();
                        }
                        break;

                    }
                }
            }
        };

        thread.start();

    }

    public static void requestStop() {
        stopRequested = true;
        try {

            Iterator<Client> iterator = connections.iterator();

            while (iterator.hasNext()) {
                Client client = iterator.next();
                client.socket.close();
                iterator.remove();
            }

            if (serverSocketI != null && !serverSocketI.isClosed()) {
                serverSocketI.close();
            }
        } catch (Exception e) {

        }

    }//Method for disconnect with client

    
    //Client Class(inner class of ProcessServer)
    class Client {

        Socket socket;
        Socket socket2;

        Client(Socket socket, Socket socket2) {
            this.socket = socket;
            this.socket2 = socket2;
            send("Please enter the number, IF you want to disconnect with server, enter : stop client");
            receive();

        }

        void receive() {

            Thread thread;
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            byte[] byteArr = new byte[100];
                            InputStream inputStream = socket.getInputStream();
                            int readByteCount = inputStream.read(byteArr);

                            if (readByteCount == -1) {
                                throw new IOException();
                            }

                            System.out.println("[Process request: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]");

                            String data = new String(byteArr, 0, readByteCount, "UTF-8");

                            int clientid = connections.indexOf(Client.this);
                            Task a = new Task(Integer.parseInt(data), clientid);// Create Task
                            n.enqueue(a);//Pass the Task to First NotificationQueue
                            sleep(3000);//Waiting for task has processed

                            int lid = result_id.indexOf(clientid);
                            result_id.remove(lid);//get index of client's task and remove the index in list

                            Task fi = result_l.get(lid);
                            result_l.remove(lid);
                            int f = fi.getnumber();
                            //get client's task and get the result of process and remove the task in list

                            System.out.println("Final result = " + f);

                            Client.this.send(Integer.toString(f));//pass the result to client

                        }
                    } catch (Exception e) {
                        try {
                            connections.remove(Client.this);
                            System.out.println("[Can not connect to client: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]");
                            socket.close();
                        } catch (IOException e2) {

                        }
                    }
                }
            };

            thread.start();
        }//Method for when recive the data from client 

        void send(String a) {

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        String data = a;

                        byte[] byteArr = data.getBytes("UTF-8");
                        OutputStream outputStream = socket2.getOutputStream();
                        //create outputstream from output socket
                        outputStream.write(byteArr);
                        outputStream.flush();
                        //send the data to client
                    } catch (Exception e) {
                        try {
                            System.out.println("[Can not connect to client2: " + socket2.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]");
                            connections.remove(Client.this);
                            socket2.close();
                        } catch (IOException e2) {

                        }
                    }
                }
            };

            thread.start();
        }
    }

}
