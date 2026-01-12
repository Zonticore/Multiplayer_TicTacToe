using Newtonsoft.Json;

public class Player : PlayerInterface
{
    [JsonProperty("playerName")] private string? playerName;
    [JsonProperty("playerChar")] private char playerChar;

    public override string ToString()
    {
        return $"playerName:[{playerName}] playerChar:[{playerChar}]";
    }

    public char getPlayerChar()
    {
        return playerChar;
    }

    public string? getPlayerName()
    {
        return playerName;
    }

    public void setPlayerChar(char newChar)
    {
        playerChar = newChar;
    }

    public void setPlayerName(string newName)
    {
        playerName = newName;
    }
}