import client.SocketClient;

public class Main {

    public static void main(String args[]) {
        String host = "127.0.0.1";
        int port = 8081;
        new SocketClient(host, port);
    }
}
