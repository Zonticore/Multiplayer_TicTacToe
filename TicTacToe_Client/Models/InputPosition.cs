using Newtonsoft.Json;

public class InputPosition
{
    [JsonProperty("pos")] public int pos;

    public InputPosition(int pos)
    {
        this.pos = pos;
    }
}