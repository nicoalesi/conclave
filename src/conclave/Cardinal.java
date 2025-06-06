package conclave;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

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
        this.cardinalsToTalkTo = 0;
        this.id = id;
        cardinalsToListenTo = new ArrayList<>();
    }

    public void run() {

        try {

            if (firstIteration) {
                encounteredOpinions = new int[Conclave.cardinals.size()];
                Arrays.fill(encounteredOpinions, -1);
                encounteredOpinions[id] = new Random().nextInt(Conclave.cardinals.size());
                currentOpinion = new Heap(Conclave.cardinals.size());
                currentOpinion.add(encounteredOpinions[id], influence);
                firstIteration = false;

            }
            cardinalsToListenTo.clear();
            cardinalsToTalkTo = 0;
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

                    }
                    available = true;

                }

                if (cardinalsToTalkTo == 0) {

                    synchronized (this) {
                        wait((new Random()).nextInt(250, 750));
                    }
                }

                synchronized (this) {
                    while (cardinalsToTalkTo > 0) {
                        wait();
                    }

                }

                while (!cardinalsToListenTo.isEmpty()) {
                    Cardinal target = cardinalsToListenTo.get(0);
                    if (target.cardinalsToTalkTo > 0) {
                        target.exchangeInformation(this);
                        cardinalsToListenTo.remove(target);
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

            System.out.println(name + " done");

            position.cancelBoardPosition();

            synchronized (Conclave.votes) {

                Conclave.votes[currentOpinion.getTop().id]++;

            }

        }
    }

    synchronized void exchangeInformation(Cardinal caller) {

        int threshold = this.influence / (caller.influence + this.influence) * 100;

        if (new Random().nextInt(100) <= threshold) { // if caller wins

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

        cardinalsToTalkTo--;
        notify();
    }
}