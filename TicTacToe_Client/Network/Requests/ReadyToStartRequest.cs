using Newtonsoft.Json;

public record ReadyToStartRequest(
    [JsonProperty("playerInfo")] Player playerInfo
);