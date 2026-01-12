public static class InputPositionValidation
{
    public static void assertValid(this InputPosition inputPosition, int max)
    {
        if (inputPosition.pos < 0) throw new InvalidMove($"Position:{inputPosition.pos} is too small");
        if (inputPosition.pos >= max) throw new InvalidMove($"Position:{inputPosition.pos} is too big");
    }
}