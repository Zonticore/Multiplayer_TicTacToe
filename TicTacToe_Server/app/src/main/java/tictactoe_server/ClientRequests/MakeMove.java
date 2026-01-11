package tictactoe_server.ClientRequests;

import com.fasterxml.jackson.databind.JsonNode;

import tictactoe_server.AbstractClientRequest;
import tictactoe_server.ClientContext;
import tictactoe_server.MatchmakingService;

public class MakeMove extends AbstractClientRequest<MakeMoveParams> {

    public MakeMove(JsonNode params) {
        super(params);
        System.out.println("MakeMove setup with params: " + this.params);
    }

    @Override
    protected Response execute(ClientContext ctx, MakeMoveParams params) {
        ctx.log("Make move at [" + params.moveX() + ", " + params.moveY() + "]");

        if (params.moveX() < 0 || params.moveX() > 2 || params.moveY() < 0 || params.moveY() > 2) {
            ctx.sendError("Invalid position");
            return new Response(false);
        }

        var match = MatchmakingService.INSTANCE.getMatchForPlayer(ctx);

        try {
            match.playerMakesMove(params.moveX(), params.moveY(), ctx);
        } catch(Exception ex) {
            return new Response(false);
        }

        return new Response(true);
    }
}