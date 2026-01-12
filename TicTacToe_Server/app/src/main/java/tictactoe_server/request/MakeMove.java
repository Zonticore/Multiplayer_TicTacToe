package tictactoe_server.request;

import com.fasterxml.jackson.databind.JsonNode;

import tictactoe_server.network.ClientContext;

public class MakeMove extends AbstractClientRequest<MakeMoveParams> {

    public MakeMove(JsonNode params) {
        super(params);
        System.out.println("MakeMove setup with params: " + this.params);
    }

    @Override
    protected Response execute(ClientContext ctx, MakeMoveParams params) {
        ctx.log("Make move at [" + params.move().x() + ", " + params.move().y() + "]");

        if (params.move().x() < 0 || params.move().x() > 2 || params.move().y() < 0 || params.move().y() > 2) {
            ctx.sendError("Invalid position");
            return new Response(false);
        }

        try {
            ctx.player().joinedMatch.playerMakesMove(params.move(), ctx);
        } catch(Exception ex) {
            return new Response(false);
        }

        return new Response(true);
    }
}