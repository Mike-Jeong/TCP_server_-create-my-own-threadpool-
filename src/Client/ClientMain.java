package Client;

import java.util.Scanner;

public class ClientMain {


    public static void startClient() {
    
        ProcessClient.startClient();
        
        while (true) {
            Scanner sc = new Scanner(System.in);
            String message = sc.nextLine();
    
            if (message.equals("stop client")) {
                break;
            }
            ProcessClient.send(message);
        }
        ProcessClient.stopClient();
    
    
    }
    
}
    
