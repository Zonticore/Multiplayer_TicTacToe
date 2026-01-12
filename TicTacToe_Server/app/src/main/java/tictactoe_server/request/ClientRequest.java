package tictactoe_server.request;
import tictactoe_server.network.ClientContext;

public interface ClientRequest {
    public Response run(ClientContext clientContext);
}
