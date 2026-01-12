public interface GameBoardInterface
{
    public void drawBoard();
    public void makePlacement(Coordinate pos, char placementChar);
    public int getValidPlacementsSize();
}