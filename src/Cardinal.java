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
            setRandom();
        }

        public Position(int x, int y) {
            setPosition(x, y);
        }

        void setPosition(int x, int y) {
            this.x = x;
            this.y = y;

            updateBoardPosition();
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

        void updateBoardPosition() {
            synchronized (Conclave.board.squares[x][y]) {
                Conclave.board.squares[x][y].residents.add(Cardinal.this);
            }
        }

        void cancelBoardPosition() {
            synchronized (Conclave.board.squares[x][y]) {
                Conclave.board.squares[x][y].residents.remove(Cardinal.this);
            }
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
            System.out.println(position.x + " " + position.y + " " + name + " " + surname);
            while (!interrupted()) {
                if (Conclave.board.squares[position.x][position.y].residents.size() < 2) {
                    sleep((new Random()).nextInt(500, 2000));
                } else {
                    synchronized (Conclave.board.squares[position.x][position.y]) {
                        cardinalsToTalkTo = new ArrayList<>(Conclave.board.squares[position.x][position.y].residents);
                    }
                    cardinalsToTalkTo.remove(this);


                }

                for (Cardinal cardinal : cardinalsToTalkTo) {
                    // System.out.print(cardinal + " ");
                    cardinal.cardinalsToListenTo.add(this);
                }
                // System.out.println("\n\n\n");

                while (!cardinalsToTalkTo.isEmpty()) {
                    System.out.println("|" + name + "," + surname + "is waiting");
                    for (Cardinal cardinal : cardinalsToTalkTo) {
                        System.out.print("|" + cardinal.name + "," + cardinal.surname + " ");
                    }
                    System.out.println("\n\n");
                    wait();
                }

                while (!cardinalsToListenTo.isEmpty()) {
                    cardinalsToListenTo.get(0).exchangeInformation(this);
                    cardinalsToListenTo.remove(0);
                }

                // System.out.println(position.x + " " + position.y + " " + name + " " + surname);

                synchronized (System.out)
                {
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
