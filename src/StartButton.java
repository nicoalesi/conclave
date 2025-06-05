import javax.swing.*;
import java.awt.*;

public class StartButton extends JButton {
    public StartButton(String text) {
        super(text);
        setLayout();
        setListener();
    }

    private void setLayout() {
        setBounds(242, 150, 100, 20);
        setSize(100, 20);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(
            BorderFactory.createLineBorder(
                Color.BLACK,
                2
            )
        );
        setFocusPainted(false);
    }

    private void setListener() {
        addActionListener(e -> {
            synchronized (Main.menu) {
                Main.menu.notify();
            }
        });
    }
}
