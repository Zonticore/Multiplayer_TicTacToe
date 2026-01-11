using Newtonsoft.Json;

public record GameOver(
    [JsonProperty("winner")] Player winner,
    [JsonProperty("winType")] WinType? winType
);

public enum WinType { ROW, COLUMN, MAIN_DIAGONAL, ANTI_DIAGONAL, NONE }