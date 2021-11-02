package Client;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/*
  The class that include the main method which is starting point of the Client
  This class is for Client that make able to connect with server
 
  @Minsik Jeong (21135840)
 */
public class ProcessClient {

    static Socket socket;
    static Socket socket2;

    static void startClient() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    socket = new Socket();
                    socket2 = new Socket();
                    socket.connect(new InetSocketAddress("localhost", 7777));
                    socket2.connect(new InetSocketAddress("localhost", 7778));
                } catch (Exception e) {
                    System.out.println("[No Server Reply]");
                    if (!socket.isClosed()) {
                        stopClient();
                    }
                    return;
                }

                receive();
            }
        };

        thread.start();
    }

    static void stopClient() {
        try {
            System.out.println("[Disconnection]");

            if ((socket != null && !socket.isClosed()) || (socket2 != null && !socket2.isClosed())) {
                socket.close();
                socket2.close();
            }
        } catch (IOException e) {
        }
    }

    static void receive() {
        while (true) {
            try {
                byte[] byteArr = new byte[100];
                InputStream inputStream = socket2.getInputStream();

                int readByteCount = inputStream.read(byteArr);

                if (readByteCount == -1) {
                    throw new IOException();
                }

                String data = new String(byteArr, 0, readByteCount, "UTF-8");

                System.out.println("[Recieved] " + data);
            } catch (Exception e) {
                System.out.println("[No Server Reply]");
                stopClient();
                break;
            }
        }
    }

    static void send(String data) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    byte[] byteArr = data.getBytes("UTF-8");
                    OutputStream outputStream = socket.getOutputStream();

                    outputStream.write(byteArr);
                    outputStream.flush();
                    System.out.println("[Passes the number]");
                } catch (Exception e) {
                    System.out.println("[No Server Reply]");
                    stopClient();
                }
            }
        };
        thread.start();
    }
}