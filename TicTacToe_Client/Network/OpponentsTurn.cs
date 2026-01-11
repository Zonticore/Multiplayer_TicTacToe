using Newtonsoft.Json;

public record OpponentsTurn(
    [JsonProperty("playerInfo")] Player playerInfo
);