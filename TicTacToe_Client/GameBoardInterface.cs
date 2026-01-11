public interface GameBoardInterface
{
    public void initBoard();
    public void drawBoard();
    public void makePlacement(Tuple<int, int> pos, char placementChar);
    public int getValidPlacementsSize();
}