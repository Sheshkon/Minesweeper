public class Cell {
    int x,y;
    int number = 0;
    boolean isBomb = false;
    boolean is_hidden = true;
    boolean isFlagged = false;
    boolean isExploded = false;

    Cell(int x, int y){
        this.x = x;
        this.y = y;
    }
    public boolean isEqual(Cell cell){
        return this.x == cell.x && this.y == cell.y;
    }

}
