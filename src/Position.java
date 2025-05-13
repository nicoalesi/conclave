import java.util.ArrayList;
import java.util.Random;

public class Position {
    int x;
    int y;

    public Position(int x, int y) {
        setPosition(x, y);
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void moveRandomly() {
        Random rand = new Random(System.nanoTime());
        ArrayList<String> availableDirections = new ArrayList<>();

        if (x > 0) {
            availableDirections.add("East");
        }

        if (x < Board.size - 1) {
            availableDirections.add("West");
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
                y += 1;
                break;
            case "South":
                y -= 1;
                break;
            case "East":
                x += 1;
                break;
            case "West":
                x -= 1;
                break;
        }
    }
}
