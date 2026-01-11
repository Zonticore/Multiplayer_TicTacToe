using Newtonsoft.Json;

public record MakeMoveRequest(
    [JsonProperty("moveX")] int moveX,
    [JsonProperty("moveY")] int moveY
);