using Newtonsoft.Json;

public record GameOver(
    [JsonProperty("winner")] Player winner,
    [JsonProperty("winType")] WinType? winType
);