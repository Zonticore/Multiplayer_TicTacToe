using Newtonsoft.Json.Linq;

public class GameLoop : GameLoopInterface
{

    public GameLoop()
    {
        playerInfo = new Player();
        gameBoard = new GameBoard();
        networkLayer = new NetworkLayer();
    }

    public async void startGame()
    {
        gameIsLive = true;
        pickPlayerName();
        pickPlayerSymbol();
        networkLayer.OnEventReceived += handleServerEvent;
        await networkLayer.RunAsync();
        Console.WriteLine($"startGame, {playerInfo}");
        gameBoard.drawBoard();
        Console.WriteLine("Waiting for your turn...");
        Console.WriteLine($"sending player info to the server... {playerInfo}");
        await networkLayer.sendRequestAsync("ready-to-start", new ReadyToStartRequest(playerInfo));
    }

    private async void onYourTurn(JObject data)
    {
        while (true)
        {
            Coordinate? move = null;
            try
            {
                move = readMove();
            }
            catch (InvalidMove invalidMove)
            {
                Console.WriteLine(invalidMove.Message);
            }
            if (move == null) continue;
            Console.WriteLine($"Made the move {move}");
            var makeMove = networkLayer.sendRequestAsync("make-move", new MakeMoveRequest(move));
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
        gameBoard.makePlacement(moveMade.move, moveMade.playerInfo.getPlayerChar());
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

    private Coordinate readMove()
    {
        Console.WriteLine("Pick a pos (x(a-b),y(1-3))");
        var inputLine = Console.ReadLine();
        if (inputLine == null || inputLine.Length == 0) throw new InvalidMove("You have to write something...");
        var inputValues = inputLine.Split(',');
        if (inputValues.Length == 0) throw new InvalidMove("Make sure you use a comma to separate x and y");
        if (inputValues[0].Length == 0) throw new InvalidMove("Needs an X");
        if (inputValues.Length < 2 || inputValues[1].Length == 0) throw new InvalidMove("Needs a Y");
        int inputX;
        if (!inputToNumber(inputValues[0].ToLower()[0], out inputX) && !int.TryParse(inputValues[0], out inputX))
        {
            throw new InvalidMove("Invalid Input X");
        }
        if (!int.TryParse(inputValues[1], out int inputY)) throw new InvalidMove("Invalid Input Y");
        return Coordinate.createCoodinate(inputX - 1, inputY - 1, gameBoard.getValidPlacementsSize()); ;
    }
    
    private bool inputToNumber(int input, out int output)
    {
        for (int i = 0; i < GameBoard.columnNames.Length; i++)
        {
            if (GameBoard.columnNames[i] == input)
            {
                output = i + 1;
                return true;
            }
        }
        output = -1;
        return false;
    }

    public bool getGameStillRunning()
    {
        return gameIsLive;
    }

    private void handleServerEvent(string eventType, JObject data)
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
    
    private Player playerInfo;
    private bool gameIsLive;
    private GameBoardInterface gameBoard;
    private NetworkLayer networkLayer;
}