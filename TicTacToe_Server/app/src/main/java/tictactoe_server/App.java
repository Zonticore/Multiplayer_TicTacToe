package tictactoe_server;

import tictactoe_server.network.NetworkLayer;

public class App {
    public static void main(String[] args) {
        NetworkLayer networkLayer = new NetworkLayer();
        networkLayer.start();
    }
}