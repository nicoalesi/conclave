package project.gui.components;

import javax.swing.*;
import java.awt.*;

public class PopeLabel extends JLabel {
    public PopeLabel(String text, int size) {
        super(text);
        setFont(size);
        setLayout(text.length());
    }

    private void setLayout(int length) {
        setSize((int)(length * 11.1), 50);
        setLocation((600 - getWidth()) / 2, 150);
    }

    private void setFont(int size) {
        Font font = new Font("Dejavu Sans Mono", Font.BOLD, size);
        setFont(font);
    }
}
