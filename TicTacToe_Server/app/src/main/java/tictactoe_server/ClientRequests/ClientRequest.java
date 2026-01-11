package tictactoe_server.ClientRequests;
import tictactoe_server.ClientContext;

public interface ClientRequest {
    public Response run(ClientContext clientContext);
}
