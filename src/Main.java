public class Main {
    public static void main(String[] args) {
        try {
            Window window = new Window("Conclave", 600, 400);
            Conclave conclave = new Conclave(6, "assets/cardinals.csv");

            window.open();
            conclave.start();
            Thread.sleep(100000);
            conclave.interrupt();
        } catch (Exception e) {
            System.out.println("A");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
