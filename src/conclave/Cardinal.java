package conclave;

import java.util.ArrayList;
import java.util.Random;

public class Cardinal implements Runnable {
    String name;
    String surname;
    int id;
    int influence;
    Position position;
    int cardinalsToTalkTo;
    ArrayList<Cardinal> cardinalsToListenTo;
    int[] encounteredOpinions;
    int receivedMessage;
    int sentMessage;
    boolean available;
    Heap currentOpinion;
    boolean firstIteration = true;

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


    public Cardinal(String name, String surname, int influence, int id) {
        this.name = name;
        this.surname = surname;
        this.influence = influence;
        this.position = new Position();
        // cardinalsToTalkTo = new ArrayList<>();
        this.cardinalsToTalkTo = 0;
        this.id = id;

        cardinalsToListenTo = new ArrayList<>();
    }

    public void run() {

        try {

            if (!firstIteration) {
                System.out.println(name + " " + surname + " is despawning");
                // position.cancelBoardPosition();
            } else {

                encounteredOpinions = new int[Conclave.cardinals.size()];
                for (int i = 0; i < encounteredOpinions.length; i++) {
                    encounteredOpinions[i] = -1;
                }
                encounteredOpinions[id] = new Random().nextInt(Conclave.cardinals.size());
                currentOpinion = new Heap(Conclave.cardinals.size());
                currentOpinion.add(encounteredOpinions[id], influence);
                firstIteration = false;

            }

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


                        /* cardinalsToTalkTo = new ArrayList<>(conclave.Conclave.board.squares[position.y][position.x].residents);
                        cardinalsToTalkTo.remove(this);
                        for (conclave.Cardinal cardinal : cardinalsToTalkTo) {
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
                        wait((new Random()).nextInt(250, 750));
                    }
                }

                // while (!cardinalsToTalkTo.isEmpty()) {
                synchronized (this) {
                    while (cardinalsToTalkTo > 0) {
                        wait();
                    }

                }


                /* if (cardinalsToListenTo.size() > 20) {
                    for (conclave.Cardinal cardinal : cardinalsToListenTo) {
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
                    System.out.println(Conclave.cardinals.size());
                    System.out.println();
                }


            }

        } catch (InterruptedException e) {

            System.out.println(name + " done");

            position.cancelBoardPosition();

            synchronized (Conclave.votes) {

                Conclave.votes[currentOpinion.getTop().id]++;

            }

        }
    }

    synchronized void exchangeInformation(Cardinal caller) {

       /*  if (caller == null) {
            boolean passive = true;
        } */

        int threshold = (int) Math.floor(this.influence / (caller.influence + this.influence) * 100);

        if (new Random().nextInt(100) <= threshold) { // if caller wins

            System.out.println("A");
            receivedMessage = caller.currentOpinion.getTop().id;

            if (encounteredOpinions[caller.id] != receivedMessage) {

                int tempID = encounteredOpinions[caller.id];
                encounteredOpinions[caller.id] = receivedMessage;

                if (tempID != -1) {

                    currentOpinion.add(tempID, (currentOpinion.remove(tempID).value - caller.influence));
                }

                currentOpinion.add(receivedMessage, currentOpinion.remove(receivedMessage).value + caller.influence);

            }

        } else { // if called wins

            System.out.println("B");
            sentMessage = currentOpinion.getTop().id;

            if (caller.encounteredOpinions[id] != sentMessage) {

                int tempID = caller.encounteredOpinions[id];
                caller.encounteredOpinions[id] = sentMessage;


                if (tempID != -1) {

                    caller.currentOpinion.add(tempID, caller.currentOpinion.remove(tempID).value - influence);

                }

                caller.currentOpinion.add(sentMessage, caller.currentOpinion.remove(sentMessage).value + influence);

            }
        }

        System.out.println("AAAA");

        cardinalsToTalkTo--;
        notify();
    }
}