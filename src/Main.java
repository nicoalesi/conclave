public class Main {
    public static void main(String[] args) {
        try {
            Window window = new Window("Conclave", 600, 400);
            Conclave conclave = new Conclave(12, "assets/cardinals.csv");

            window.open();
            window.writeMass();
            conclave.start();
            
            // conclave.interrupt();

        } catch (Exception e) {
            System.out.println("A");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
