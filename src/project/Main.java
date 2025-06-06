package project;

import project.conclave.Conclave;
import project.gui.Window;
import project.util.Triplet;

import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class Main {
    public static final ArrayList<Triplet<String, Integer>> data = new ArrayList<>();
    public static final Object menu = new Object();
    public static String pope = "";

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

            while (pope.isBlank()) {
                synchronized (data) {
                    data.wait();
                    window.displayVotes(data);
                    data.clear();
                }
            }

            window.displayPope();
            System.out.println(pope);
            // conclave.interrupt();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
