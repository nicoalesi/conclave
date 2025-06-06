package project.gui.components;

import javax.swing.*;
import java.awt.*;

public class LeaderboardLabel {
    private final JLabel nameLabel;
    private final JLabel votesLabel;

    public LeaderboardLabel(String name, int votes) {
        nameLabel = new JLabel(name);
        votesLabel = new JLabel(String.format("%03d", votes));
        setDefault();
    }

    public void addTo(JFrame frame) {
        frame.add(nameLabel);
        frame.add(votesLabel);
    }

    public void setLocation(int y) {
        nameLabel.setLocation(20, y);
        votesLabel.setLocation(532, y);
    }

    private void setDefault() {
        Font font = new Font("DejaVu Sans Mono", Font.PLAIN, 18);
        nameLabel.setFont(font);
        votesLabel.setFont(font);
        nameLabel.setSize(400, 50);
        votesLabel.setSize(50, 50);
    }
}
