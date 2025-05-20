public class Board {
    static int size;
    Square[][] squares;

    public Board(int side) {
        size = side;
        squares = new Square[side][side];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squares[i][j] = new Square();
            }
        }
    }
}
