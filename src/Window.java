import javax.swing.*;

public class Window {
    private JFrame frame;

    public Window(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Window(String title, int width, int height) {
        this(title);
        frame.setSize(width, height);
    }

    void open() {
        frame.setVisible(true);
    }
}