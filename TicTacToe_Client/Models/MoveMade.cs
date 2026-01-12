using Newtonsoft.Json;

public record MoveMade(
    [JsonProperty("move")] Coordinate move,
    [JsonProperty("playerInfo")] Player playerInfo
);