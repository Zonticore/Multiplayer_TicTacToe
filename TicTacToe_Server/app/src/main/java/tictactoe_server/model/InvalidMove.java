package tictactoe_server.model;

public class InvalidMove extends RuntimeException {
    public InvalidMove(String message) {
        super(message);
    }

    public InvalidMove(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMove(Throwable cause) {
        super(cause);
    }

    public InvalidMove() {
        super();
    }
}