using Newtonsoft.Json;

public record ClientRequest(
    [JsonProperty("type")] string Type,
    [JsonProperty("params")] object? Params = null
);