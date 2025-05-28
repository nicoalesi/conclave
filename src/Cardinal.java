import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Cardinal extends Thread {
    String name;
    String surname;
    static ArrayList<Cardinal> cardinals = new ArrayList<>();
    int id;
    int influence;
    Position position;
    // ArrayList<Cardinal> cardinalsToTalkTo;
    int cardinalsToTalkTo;
    ArrayList<Cardinal> cardinalsToListenTo;
    Opinion[] encounteredOpinions;
    int receivedMessage;
    int sentMessage;
    boolean available;
    Heap currentOpinion;

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

        void updateBoardPosition() {
            synchronized (Conclave.board.squares[y][x]) {
                Conclave.board.squares[y][x].residents.add(Cardinal.this);
            }
        }

        void cancelBoardPosition() {
            synchronized (Conclave.board.squares[y][x]) {
                Conclave.board.squares[y][x].residents.remove(Cardinal.this);
            }
        }
    }


    public Cardinal(String name, String surname, int influence) {
        this.name = name;
        this.surname = surname;
        this.influence = influence;
        this.position = new Position();
        // cardinalsToTalkTo = new ArrayList<>();
        this.cardinalsToTalkTo = 0;
        this.id = cardinals.size();
        cardinals.add(this);
        encounteredOpinions = new Opinion[Conclave.cardinals.size()];
        
        cardinalsToListenTo = new ArrayList<>();
    }

    public static Cardinal getCardinalByID(int id) {
        for (Cardinal cardinal : cardinals) {
            if (cardinal.id == id) {
                return cardinal;
            }
        }
        return null;
    }

    public void run() {
        try {

            position.setRandom();

            while (true) {

                synchronized (Conclave.board.squares[position.y][position.x]) {

                    if (Conclave.board.squares[position.y][position.x].residents.size() > 1) {
                        
                        for (Cardinal cardinal : Conclave.board.squares[position.y][position.x].residents) {
                            if (cardinal != this && cardinal.available) {
                                cardinal.cardinalsToListenTo.add(this);
                                cardinalsToTalkTo++;
                            }
                            
                        }
                        

                        /* cardinalsToTalkTo = new ArrayList<>(Conclave.board.squares[position.y][position.x].residents);
                        cardinalsToTalkTo.remove(this);
                        for (Cardinal cardinal : cardinalsToTalkTo) {
                            synchronized (cardinal.cardinalsToListenTo) {
                                cardinal.cardinalsToListenTo.add(this);
                            }
                        } */

                    }
                    available = true;
                    
                }
                
                // if (cardinalsToTalkTo.size() == 0) {
                if (cardinalsToTalkTo == 0) {

                    synchronized (this) {
                        wait((new Random()).nextInt(500, 2000));
                    }
                }

                // while (!cardinalsToTalkTo.isEmpty()) {
                synchronized (this) {
                    while (cardinalsToTalkTo > 0) {
                        wait();
                    }
                    
                }
                

                /* if (cardinalsToListenTo.size() > 20) {
                    for (Cardinal cardinal : cardinalsToListenTo) {
                        cardinal.cardinalsToTalkTo.remove(this);
                    }
                } */

                while (!cardinalsToListenTo.isEmpty()) {
                    Cardinal target = cardinalsToListenTo.get(0);
                        if (target.cardinalsToTalkTo > 0) {

                            // sentMessage = currentOpinion.getTop().id;
                            // target.receivedMessage = sentMessage;

                            target.exchangeInformation(this);
                            cardinalsToListenTo.remove(0);
                            synchronized (Conclave.board.squares[position.y][position.x]) {
                                if (cardinalsToListenTo.isEmpty())
                                    available = false;
                            }
                        
                    }

                }
                available = false;
                
                position.moveRandomly();

                synchronized (System.out) {
                    int count = 0;
                    for (Square[] row : Conclave.board.squares) {
                        for (Square square : row) {
                            count += square.residents.size();
                            System.out.print(square.residents.size() + " ");
                        }
                        System.out.println();
                    }
                    System.out.println(count);
                    System.out.println();
                }

                
            }

        } catch (InterruptedException e) {
            System.out.println(name + "done");
        }
    }

    synchronized void exchangeInformation(Cardinal caller) {

        if (caller == null) {
            boolean passive = true;
        }
        
        System.out.println("AAAA");

        cardinalsToTalkTo--;
        notify();
    }
}
