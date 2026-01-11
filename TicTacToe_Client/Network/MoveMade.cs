using Newtonsoft.Json;

public record MoveMade(
    [JsonProperty("moveX")] int moveX,
    [JsonProperty("moveY")] int moveY,
    [JsonProperty("playerInfo")] Player playerInfo
);