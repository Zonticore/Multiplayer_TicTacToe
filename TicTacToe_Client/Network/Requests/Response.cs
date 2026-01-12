using Newtonsoft.Json;

public record Response(
    [JsonProperty("success")] bool success
);