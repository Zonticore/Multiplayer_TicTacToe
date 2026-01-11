package tictactoe_server;

public class App {
    public static void main(String[] args) {
        NetworkLayer networkLayer = new NetworkLayer();
        networkLayer.start();
    }
}
