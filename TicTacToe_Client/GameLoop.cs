using Newtonsoft.Json.Linq;

public class GameLoop : GameLoopInterface
{
    public async void startGame()
    {
        gameIsLive = true;

        playerInfo = new Player();
        pickPlayerName();
        pickPlayerSymbol();
        networkLayer = new NetworkLayer();
        networkLayer.OnEventReceived += HandleServerEvent;
        await networkLayer.RunAsync();
        Console.WriteLine($"startGame, {playerInfo}");
        readyGameBoard();
        gameBoard.drawBoard();
        Console.WriteLine("Waiting for your turn...");
        Console.WriteLine($"sending player info to the server... {playerInfo}");
        await networkLayer.sendRequestAsync("ready-to-start", new ReadyToStartRequest(playerInfo));
    }

    private async void onYourTurn(JObject data)
    {
        while (true)
        {
            var move = readMove();
            Console.WriteLine($"Made the move {move}");
            var makeMove = networkLayer.sendRequestAsync("make-move", new MakeMoveRequest(move.Item1, move.Item2));
            await makeMove;
            if (makeMove.Result?.success == true) break;
            Console.WriteLine($"Failed to make that move");
        }
    }

    private void onOpponentTurn(JObject data)
    {
        var turnData = data.ToObject<OpponentsTurn>();
        if (turnData == null) throw new Exception("onOpponentTurn but no data");
        Console.WriteLine($"{turnData.playerInfo.getPlayerName()}'s Turn");
    }

    private void onMoveMade(JObject data)
    {
        var moveMade = data.ToObject<MoveMade>();
        if (moveMade == null) throw new Exception("Move made but no moveMade data!");
        Console.WriteLine($"onMoveMade, {moveMade}, {moveMade.playerInfo.getPlayerName()}");
        gameBoard.makePlacement(new Tuple<int, int>(moveMade.moveX, moveMade.moveY), moveMade.playerInfo.getPlayerChar());
        gameBoard.drawBoard();
    }
    
    private void onGameOver(JObject data)
    {
        var gameOverData = data.ToObject<GameOver>();
        if (gameOverData == null) throw new Exception("invalid gameOverData");
        if(gameOverData.winner == null)
        {
            Console.WriteLine("Match Was A Draw!");
        }
        else
        {
            Console.WriteLine($"Winner:{gameOverData.winner} winType:{gameOverData.winType}");
        }
    }

    private void pickPlayerName()
    {
        while (true)
        {
            Console.WriteLine("Enter Your Username:");
            var inputName = Console.ReadLine();
            if (string.IsNullOrWhiteSpace(inputName))
            {
                Console.WriteLine("invalid username");
            }
            else
            {
                playerInfo.setPlayerName(inputName);
                break;
            }
        }
    }
    
    private void pickPlayerSymbol()
    {
        while (true)
        {
            Console.WriteLine("Enter Your Display Symbol:");
            var inputChar = Console.ReadLine();
            if (inputChar == null || inputChar.Length == 0 || inputChar[0] == ' ')
            {
                Console.WriteLine("invalid display symbol");
            }
            else
            {
                playerInfo.setPlayerChar(inputChar[0]);
                break;
            }
        }
    }
    
    private Tuple<int, int> readMove()
    {
        Console.WriteLine("Pick a pos (x(a-b),y(1-3))");
        var inputLine = Console.ReadLine();
        if (inputLine == null || inputLine.Length == 0) throw new InvalidMove("You have to write something...");
        var inputValues = inputLine.Split(',');
        if (inputValues.Length == 0) throw new InvalidMove("Make sure you use a comma to separate x and y");
        List<string> failReasons = [];
        int inputX = -1;
        for (int i = 0; i < GameBoard.columnNames.Length; i++)
        {
            if (GameBoard.columnNames[i] == inputValues[0].ToLower()[0]) inputX = i+1;
        }
        if (inputX == -1)
        {
            if (!int.TryParse(inputValues[0], out inputX)) failReasons.Add($"<Invalid X Value of [{inputValues[0]}]>");
        }
        if (!int.TryParse(inputValues[1], out int inputY)) failReasons.Add($"<Invalid Y Value of [{inputValues[1]}]>");
        if (failReasons.Count > 0)
        {
            string failMessage = "Failed to parse input values,";
            foreach (var message in failMessage)
            {
                failMessage += message;
            }
            throw new InvalidMove(failMessage);
        }
        var max = gameBoard.getValidPlacementsSize();
        var min = 1;
        void checkRange(int value, string name)
        {
            if (value < min) failReasons.Add($"{name} must be at least {min}");
            if (value > max) failReasons.Add($"{name} must be at most {max}");
        }
        checkRange(inputX, "X");
        checkRange(inputY, "Y");

        return new Tuple<int, int>(inputX - 1, inputY - 1);
    }

    private void readyGameBoard()
    {
        gameBoard = new GameBoard();
        gameBoard.initBoard();
    }

    public bool getGameStillRunning()
    {
        return gameIsLive;
    }

    private void HandleServerEvent(string eventType, JObject data)
    {
        switch (eventType)
        {
            case "YourTurn":
                onYourTurn(data);
                break;
            case "OpponentsTurn":
                onOpponentTurn(data);
                break;
            case "MoveMade":
                onMoveMade(data);
                break;
            case "GameOver":
                onGameOver(data);
                break;
            default:
                Console.WriteLine($"Unhandled event type: {eventType}");
                break;
        }
    }

    private class InvalidMove : Exception
    {
        public InvalidMove(string message) : base(message)
        {

        }
    }
    
    private Player playerInfo;
    private bool gameIsLive;
    private GameBoardInterface gameBoard;
    private NetworkLayer networkLayer;
}