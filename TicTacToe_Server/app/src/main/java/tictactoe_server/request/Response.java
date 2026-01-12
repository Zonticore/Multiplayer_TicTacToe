package tictactoe_server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(
    @JsonProperty("success") 
    boolean success
) {}