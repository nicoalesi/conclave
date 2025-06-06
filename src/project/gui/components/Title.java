package project.gui.components;

import javax.swing.*;
import java.awt.*;

public class Title extends JLabel {
    private Font font;

    public Title() { super(); }

    public Title(String text) {
        this();
        setText(text);
        setSize(300, 50);
    }

    public Title(String text, int size) {
        this(text);
        setFont(size);
    }

    public Title(String text, int size, boolean bold) {
        this(text);
        setFont(size, bold);
    }

    private void setFont(int size) {
        font = new Font("DejaVu Sans Mono", Font.PLAIN, size);
        setFont(font);
    }

    private void setFont(int size, boolean bold) {
        font = new Font("DejaVu Sans Mono", Font.BOLD, size);
        setFont(font);
    }
}
