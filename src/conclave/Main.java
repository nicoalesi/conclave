package conclave;

import conclave.gui.Window;
import conclave.util.Triplet;

import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class Main {
    public static ArrayList<Triplet<String, Integer>> data = new ArrayList<>();
    public static final Object menu = new Object();

    public static void main(String[] args) {
        try {
            Window window = new Window(
                "Conclave",
                600,
                400
            );
            Conclave conclave = new Conclave(
                12,
                "assets/cardinals.csv"
            );

            window.open();
            window.displayMenu();

            synchronized (menu) {
                menu.wait();
            }

            window.writeMass();

            sleep(12000);
            conclave.start();

            while (true) {
                synchronized (data) {
                    data.wait();

                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    window.displayVotes(data);
                    // System.out.println(data.get(0));
                    data.clear();
                }
            }
            // conclave.interrupt();

        } catch (Exception e) {
            System.out.println("A");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
