package Client;

public class ClientMain {

    ProcessClient.startClient();

    
    
    //startClient();
    while (true) {
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();

        if (message.equals("stop client")) {
            break;
        }
        send(message);
    }
    stopClient();
}
    
}
