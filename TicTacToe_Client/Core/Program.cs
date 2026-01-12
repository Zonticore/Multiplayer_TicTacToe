class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Start Program");
        GameLoopInterface gameLoop = new GameLoop();
        gameLoop.startGame();
        while (gameLoop.getGameStillRunning())
        {
            //keep the program running
        }
        Console.WriteLine("End Program");
    }
}