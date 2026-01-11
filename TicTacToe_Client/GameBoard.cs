using System.Numerics;

public class GameBoard : GameBoardInterface
{
    public void makePlacement(Tuple<int, int> pos, char placementChar)
    {
        placements[pos.Item1, pos.Item2] = placementChar;
    }

    public void initBoard()
    {
        for (int y = 0; y < placementsSize; y++)
        {
            for (int x = 0; x < placementsSize; x++)
            {
                placements[x, y] = spaceChar;
            }
        }
    }

    public void drawBoard()
    {
        Console.Clear();
        drawHeader();
        for (int y = 0; y < boardSize.Y; y++)
        {
            var isRowLine = (y % 3) == 2;
            if (isRowLine && y == boardSize.Y - 1) isRowLine = false;
            var isPlacementY = (y % 3) == 1;
            var columnsDrawn = 0;
            var yPos = y / 3;
            if (isPlacementY)
            {
                Console.Write(yPos+1);
            }
            else
            {
                drawSpace(1);
            }
            for (int x = 0; x < boardSize.X; x++)
            {
                var isColumnLine = (x % 6) == 5;
                if (isColumnLine)
                {
                    Console.Write(boardYLine);
                    columnsDrawn++;
                }
                else if (isRowLine)
                {
                    Console.Write(boardXLine);
                }
                else
                {
                    var isPlacementX = (x % 6) == 2;
                    if (isPlacementY && isPlacementX)
                    {
                        var xPos = (x - columnsDrawn) / 5;
                        Console.Write(placements[xPos,yPos]);
                    }
                    else
                    {
                        drawSpace(1);
                    }
                }
            }
            Console.Write('\n');
        }
    }
    
    private void drawHeader()
    {
        drawSpace(1);
        for (int i = 0; i < columns; i++)
        {
            int spacesBefore = 0;
            if (i == 0) spacesBefore += leftHeaderPadding;
            drawSpace(spacesBefore);
            Console.Write(columnNames[i]);
            int spacesAfter = middleHeaderPadding;
            if (i == columns - 1) spacesAfter = rightHeaderPadding;
            drawSpace(spacesAfter);
        }
        Console.Write('\n');
    }

    private void drawSpace(int spacesToDraw)
    {
        for (int i = 0; i < spacesToDraw; i++)
        {
            Console.Write(spaceChar);
        }
    }
    
    public int getValidPlacementsSize()
    {
        return placementsSize;
    }

    private int leftHeaderPadding = 2;
    private int rightHeaderPadding = 2;
    private int middleHeaderPadding = 5;
    private Vector2 boardSize = new Vector2(17, 9);
    private char[,] placements = new char[placementsSize, placementsSize];
    private char boardYLine = '|';
    private char boardXLine = '_';
    private int columns = 3;
    private char spaceChar = ' ';
    public static char[] columnNames = { 'a', 'b', 'c' };
    private const int placementsSize = 3;
}