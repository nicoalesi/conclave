package project.conclave;

import project.Main;
import project.util.ConclaveSetupException;
import project.util.Reader;
import project.util.Triplet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

// Class to manage scrutinies
public class Conclave extends Thread {
    static Board board;
    static ArrayList<Cardinal> cardinals;
    static Thread[] threads;
    static int[] votes;
    static int pope;

    public Conclave(int boardSize, String csvPath) throws ConclaveSetupException {
        setBoard(boardSize);
        setCardinals(csvPath);
    }

    @Override
    public void run() {

        try {

            pope = 0;
            while (true) {

                spawnCardinals();
                Arrays.fill(votes, 0);
                sleep(3000);

                for (Thread thread : threads) {
                    thread.interrupt();
                }

                for (Thread thread : threads) {
                    while (thread.isAlive()) {
                    }
                }

                synchronized (Main.data) {
                    for (int i = 0; i < votes.length; i++) {
                        Main.data.add(new Triplet<>(
                                cardinals.get(i).name,
                                cardinals.get(i).surname,
                                votes[i]
                                ));
                        pope = votes[i] > votes[pope] ? i : pope;
                    }
                    Main.data.notify();
                }

                if (votes[pope] >= ((cardinals.size() / 3)) * 2) {
                    Main.pope = cardinals.get(pope).name + " " + cardinals.get(pope).surname;
                    System.out.println("Pope elected: " + cardinals.get(pope).name + " " + cardinals.get(pope).surname + " with " + votes[pope] + " votes. (target: " + (int) ((cardinals.size() / 3)) * 2 + ")");
                    break;

                } else {

                    System.out.println("No pope elected. The cardinal with the most votes is " + cardinals.get(pope).name + " " + cardinals.get(pope).surname + " with " + votes[pope] + " votes. (target: " + (int) ((cardinals.size() / 3)) * 2 + ")");
                    System.out.println("The conclave will continue.");
                }

            }


        } catch (InterruptedException e) {

            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }
    

    // Create room's board
    void setBoard(int side) {
        if (side < 2) {
            throw new IllegalArgumentException("conclave.Board size not allowed.");
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
            int i = 0;
            Reader reader = new Reader(csvPath);
            while(reader.hasRow()) {
                String[] data = reader.getRowData();
                cardinals.add(
                    new Cardinal(
                        data[1],
                        data[0],
                        Integer.parseInt(data[2]),
                            i++
                    )
                );
            }

            threads = new Thread[cardinals.size()];
        } catch (FileNotFoundException e) {
            throw new ConclaveSetupException("CSV file path not found.");
        }
        votes = new int[cardinals.size()];
    }

    void spawnCardinals() {

        for (Cardinal cardinal : cardinals) {
            Thread newThread = new Thread(cardinal);
            threads[cardinal.id] = newThread;
            newThread.start();
        }
    }
}
