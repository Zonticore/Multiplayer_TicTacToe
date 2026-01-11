package tictactoe_server.ClientRequests;

import com.fasterxml.jackson.databind.JsonNode;

import tictactoe_server.AbstractClientRequest;
import tictactoe_server.ClientContext;
import tictactoe_server.MatchmakingService;

public class ReadyToStartRequest extends AbstractClientRequest<ReadyToStartParams> {

    public ReadyToStartRequest(JsonNode params) {
        super(params);
        System.out.println("ReadyToStartRequest with params: " + this.params);
    }

    @Override
    protected Response execute(ClientContext ctx, ReadyToStartParams params) {
        ctx.log("ReadyToStartRequest [" + params.playerInfo() + "]");

        MatchmakingService.INSTANCE.findMatchForUser(params.playerInfo(), ctx);

        return new Response(true);
    }
}