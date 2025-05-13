import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Class to retrieve data from the CSV
public class Reader {
    Scanner scanner;

    public Reader(String filePath) throws FileNotFoundException {
        setScanner(filePath);
    }

    // Create scanner to read the CSV
    void setScanner(String path) throws FileNotFoundException{
        File file = new File(path);
        scanner = new Scanner(file);
        scanner.nextLine();
    }

    // Check whether the file is finished or not
    public boolean hasRow() {
        return scanner.hasNextLine();
    }

    // Get CSV row and split its content
    public String[] getRowData() {
        String row = scanner.nextLine();
        return row.split(";");
    }
}
