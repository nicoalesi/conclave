public class Cardinal extends Thread {
    String name;
    String surname;
    int influence;
    Position position;

    public Cardinal(String name, String surname, int influence) {
        this.name = name;
        this.surname = surname;
        this.influence = influence;
    }

    public void run() {
        System.out.println("Hi I am " + name + " " + surname);
    }
}
