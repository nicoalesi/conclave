import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

// Class to manage scrutinies
public class Conclave extends Thread {
    static Board board;
    ArrayList<Cardinal> cardinals;

    public Conclave(int boardSize, String csvPath) throws ConclaveSetupException {
        setBoard(boardSize);
        setCardinals(csvPath);
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                spawnCardinals();

                sleep(100000);
                for (Cardinal cardinal : cardinals) {
                    cardinal.interrupt();
                }

                for (Cardinal cardinal : cardinals) {
                    while (cardinal.isAlive()) {};
                }

                System.out.println("ALL DONE");

//                for (Square[] row : board.squares) {
//                    for (Square square : row) {
//                        for (Cardinal cardinal : square.residents) {
//                            System.out.print(cardinal.name + cardinal.surname);
//                        }
//                        System.out.println();
//                    }
//                }

                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Create room's board
    void setBoard(int side) {
        if (side < 12) {
            throw new IllegalArgumentException("Board size not allowed.");
        }

        board = new Board(side);
    }

    // Populate cardinals' list
    void setCardinals(String csvPath) throws ConclaveSetupException {
        if (csvPath.isBlank()) {
            throw new IllegalArgumentException("Blank file path.");
        }

        cardinals = new ArrayList<>();

        try {
            Reader reader = new Reader(csvPath);
            while(reader.hasRow()) {
                String[] data = reader.getRowData();
                cardinals.add(
                    new Cardinal(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2])
                    )
                );
            }
        } catch (FileNotFoundException e) {
            throw new ConclaveSetupException("CSV file path not found.");
        }
    }

    void spawnCardinals() {
        for (Cardinal cardinal : cardinals) {
            cardinal.start();
        }
    }
}
