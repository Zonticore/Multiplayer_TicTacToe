using Newtonsoft.Json;

public record Coordinate(
    [JsonProperty("x")] int x,
    [JsonProperty("y")] int y
);