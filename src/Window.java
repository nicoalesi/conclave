import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Window {
    private JFrame frame;

    public Window(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
    }

    public Window(String title, int width, int height) {
        this(title);
        frame.setSize(width, height);
    }

    void open() {
        frame.setVisible(true);
    }

    void displayMenu() {
        Font font = new Font("DejaVu Sans Mono", Font.BOLD, 32);
        JLabel title = new JLabel();
        title.setText("Conclave");
        title.setSize(300, 50);
        title.setLocation(220, 50);
        title.setFont(font);
        frame.add(title);

        JButton button = new JButton("Start");
        button.setBounds(242, 150, 100, 20);
        button.setSize(100, 20);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(
                Color.BLACK,
                2
        ));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.repaint();
            synchronized (Main.menu) {
                Main.menu.notify();
            }
        });

        frame.add(button);
        frame.repaint();
    }

    void displayVotes(ArrayList<Triplet<String, Integer>> votes) {
        frame.getContentPane().removeAll();

        JLabel[] labels = new JLabel[5];
        JLabel[] voteLabels = new JLabel[5];
        votes.sort(Collections.reverseOrder());

        Font titleFont = new Font("DejaVu Sans Mono", Font.BOLD, 20);
        JLabel title = new JLabel();
        title.setText("Leaderboard");
        title.setFont(titleFont);
        title.setSize(300, 50);
        title.setLocation(236, 2);
        frame.add(title);

        Font font = new Font("DejaVu Sans Mono", Font.PLAIN, 18);
        for (int i = 0; i < 5; i++) {
            labels[i] = new JLabel();
            labels[i].setFont(font);
            labels[i].setText(votes.get(i).name + " " + votes.get(i).surname);
            labels[i].setSize(400, 50);
            labels[i].setLocation(20, 90 + 30 * i);
            voteLabels[i] = new JLabel();
            voteLabels[i].setText(String.format("%03d", votes.get(i).votes));
            voteLabels[i].setFont(font);
            voteLabels[i].setSize(50, 50);
            voteLabels[i].setLocation(532, 90 + 30 * i);
            frame.add(labels[i]);
            frame.add(voteLabels[i]);
            frame.repaint();
        }
    }

    void writeMass() {
        String massText = "Nos omnes et singuli in hac electione Summi Pontificis versantes Cardinales electores promittimus,\nvovemus et iuramus inviolate et ad unguem Nos esse fideliter et diligenter observaturos omnia quae\ncontinentur in Constitutione Apostolica Summi Pontificis Ioannis Pauli II, quae a verbis\n«Universi Dominici Gregis» incipit, data die XXII mensis Februarii anno MCMXCVI. Item promittimus,\nvovemus et iuramus, quicumque nostrum, Deo sic disponente, Romanus Pontifex erit electus,\neum munus Petrinum Pastoris Ecclesiae universae fideliter exsecuturum esse atque spiritualia\net temporalia iura libertatemque Sanctae Sedis integre ac strenue asserere atque tueri\nnumquam esse destiturum. Praecipue autem promittimus et iuramus Nos religiosissime\net quoad cunctos, sive clericos sive laicos, secretum esse servaturos de iis omnibus, quae ad\nelectionem Romani Pontificis quomodolibet pertinent, et de iis, quae in loco electionis aguntur,\nscrutinium directe vel indirecte respicientibus; neque idem secretum quoquo modo violaturos sive\nperdurante novi Pontificis electione, sive etiam post, nisi expressa facultas ab eodem Pontifice tributa\nsit, itemque nulli consensioni, dissensioni, aliique cuilibet intercessioni, quibus auctoritates saeculares\ncuiuslibet ordinis et gradus, vel quivis hominum coetus vel personae singulae voluerint sese Pontificis\nelectioni immiscere, auxilium vel favorem praestaturos.";
        ScrollingText text = new ScrollingText(massText);
        text.place(frame.getWidth(), frame.getHeight());
        frame.add(text);
        text.scroll();
    }
}