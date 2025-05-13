public class Board {
    static int size;
    Square[][] squares;

    public Board(int side) {
        size = side;
        squares = new Square[side][side];
    }
}
