using Newtonsoft.Json;

public record Coordinate(
    [JsonProperty("x")] InputPosition x,
    [JsonProperty("y")] InputPosition y
)
{
    public static Coordinate createCoodinate(int xNumber, int yNumber, int max)
    {
        var xInput = new InputPosition(xNumber);
        xInput.assertValid(max);
        var yInput = new InputPosition(yNumber);
        yInput.assertValid(max);
        return new Coordinate(xInput, yInput);
    }
}