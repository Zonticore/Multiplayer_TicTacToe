package tictactoe_server.request;

import com.fasterxml.jackson.databind.JsonNode;

import tictactoe_server.network.ClientContext;
import tictactoe_server.service.MatchmakingService;

public class ReadyToStartRequest extends AbstractClientRequest<ReadyToStartParams> {

    public ReadyToStartRequest(JsonNode params) {
        super(params);
        System.out.println("ReadyToStartRequest with params: " + this.params);
    }

    @Override
    protected Response execute(ClientContext ctx, ReadyToStartParams params) {
        ctx.log("ReadyToStartRequest [" + params.playerInfo() + "]");

        ctx.player().playerInfo = params.playerInfo();
        ctx.player().clientContext = ctx;
        MatchmakingService.INSTANCE.findMatchForUser(ctx.player());

        return new Response(true);
    }
}