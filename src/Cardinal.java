import java.util.ArrayList;
import java.util.Random;

public class Cardinal extends Thread {
    String name;
    String surname;
    int influence;
    Position position;
    ArrayList<Cardinal> cardinalsToTalkTo;
    ArrayList<Cardinal> cardinalsToListenTo;

    public class Position {
        int x;
        int y;

        public Position() {
            x = -1;
            y = -1;
        }

        public Position(int x, int y) {
            setPosition(x, y);
        }

        void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void setRandom() {
            setPosition(
                (new Random()).nextInt(Board.size),
                (new Random()).nextInt(Board.size)
            );
        }

        void moveRandomly() {
            Random rand = new Random(System.nanoTime());
            ArrayList<String> availableDirections = new ArrayList<>();

            cancelBoardPosition();

            if (x > 0) {
                availableDirections.add("West");
            }

            if (x < Board.size - 1) {
                availableDirections.add("East");
            }

            if (y > 0) {
                availableDirections.add("North");
            }

            if ( y < Board.size - 1) {
                availableDirections.add("South");
            }

            int num = rand.nextInt(availableDirections.size());
            switch (availableDirections.get(num)) {
                case "North":
                    y -= 1;
                    break;
                case "South":
                    y += 1;
                    break;
                case "East":
                    x += 1;
                    break;
                case "West":
                    x -= 1;
                    break;
            }

            updateBoardPosition();
        }

        synchronized void updateBoardPosition() {
            Conclave.board.squares[y][x].residents.add(Cardinal.this);
        }

        synchronized void cancelBoardPosition() {
            Conclave.board.squares[y][x].residents.remove(Cardinal.this);
        }
    }


    public Cardinal(String name, String surname, int influence) {
        this.name = name;
        this.surname = surname;
        this.influence = influence;
        this.position = new Position();
        cardinalsToTalkTo = new ArrayList<>();
        cardinalsToListenTo = new ArrayList<>();
    }

    public synchronized void run() {
        try {
            position.setRandom();
            while (!interrupted()) {
                if (Conclave.board.squares[position.y][position.x].residents.size() < 2) {
                    sleep((new Random()).nextInt(500, 2000));
                } else {
                    synchronized (Conclave.board.squares[position.y][position.x]) {
                        cardinalsToTalkTo = new ArrayList<>(Conclave.board.squares[position.y][position.x].residents);
                    }
                    cardinalsToTalkTo.remove(this);


                }

                for (Cardinal cardinal : cardinalsToTalkTo) {
                    cardinal.cardinalsToListenTo.add(this);
                }

                while (!cardinalsToTalkTo.isEmpty()) {
                    wait();
                }

                if (cardinalsToListenTo.size() > 20) {
                    for (Cardinal cardinal : cardinalsToListenTo) {
                        cardinal.cardinalsToTalkTo.remove(this);
                    }
                }

                while (!cardinalsToListenTo.isEmpty()) {
                    cardinalsToListenTo.get(0).exchangeInformation(this);
                    cardinalsToListenTo.remove(0);
                }

                synchronized (System.out) {
                    for (Square[] row : Conclave.board.squares) {
                        for (Square square : row) {
                            System.out.print(square.residents.size() + " ");
                        }
                        System.out.println();
                    }
                    System.out.println();
                }

                position.moveRandomly();
            }

        } catch (InterruptedException e) {
            System.out.println(name + "done");
        }
    }

    synchronized void exchangeInformation(Cardinal caller) {
        notify();
        System.out.println("AAAA");
        cardinalsToTalkTo.remove(caller);
    }
}
