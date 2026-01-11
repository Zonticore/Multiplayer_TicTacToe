package tictactoe_server.ClientRequests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(
    @JsonProperty("success") 
    boolean success
) {}