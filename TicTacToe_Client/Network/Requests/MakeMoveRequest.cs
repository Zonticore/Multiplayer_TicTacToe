using Newtonsoft.Json;

public record MakeMoveRequest(
    [JsonProperty("move")] Coordinate move
);