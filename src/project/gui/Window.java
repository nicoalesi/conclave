package project.gui;

import project.gui.components.*;
import project.util.Triplet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Window {
    private final JFrame frame;

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

    public void open() {
        frame.setVisible(true);
    }

    public void displayMenu() {
        Title title = new Title("Conclave", 32, true);
        title.setLocation(220, 50);
        StartButton button = new StartButton("Start");

        frame.add(title);
        frame.add(button);
        frame.repaint();
    }

    public void displayVotes(ArrayList<Triplet<String, Integer>> data) {
        frame.getContentPane().removeAll();

        LeaderboardLabel[] labels = new LeaderboardLabel[5];
        data.sort(Collections.reverseOrder());

        Title title = new Title("Leaderboard", 20, true);
        title.setLocation(236, 2);
        frame.add(title);

        for (int i = 0; i < 5; i++) {
            labels[i] = new LeaderboardLabel(
                data.get(i).name + " " + data.get(i).surname,
                data.get(i).votes
            );
            labels[i].setLocation(90 + 30 * i);
            labels[i].addTo(frame);
            frame.repaint();
        }
    }

    public void writeMass() {
        frame.getContentPane().removeAll();
        frame.repaint();
        String massText = "Nos omnes et singuli in hac electione Summi Pontificis versantes Cardinales electores promittimus,\nvovemus et iuramus inviolate et ad unguem Nos esse fideliter et diligenter observaturos omnia quae\ncontinentur in Constitutione Apostolica Summi Pontificis Ioannis Pauli II, quae a verbis\n«Universi Dominici Gregis» incipit, data die XXII mensis Februarii anno MCMXCVI. Item promittimus,\nvovemus et iuramus, quicumque nostrum, Deo sic disponente, Romanus Pontifex erit electus,\neum munus Petrinum Pastoris Ecclesiae universae fideliter exsecuturum esse atque spiritualia\net temporalia iura libertatemque Sanctae Sedis integre ac strenue asserere atque tueri\nnumquam esse destiturum. Praecipue autem promittimus et iuramus Nos religiosissime\net quoad cunctos, sive clericos sive laicos, secretum esse servaturos de iis omnibus, quae ad\nelectionem Romani Pontificis quomodolibet pertinent, et de iis, quae in loco electionis aguntur,\nscrutinium directe vel indirecte respicientibus; neque idem secretum quoquo modo violaturos sive\nperdurante novi Pontificis electione, sive etiam post, nisi expressa facultas ab eodem Pontifice tributa\nsit, itemque nulli consensioni, dissensioni, aliique cuilibet intercessioni, quibus auctoritates saeculares\ncuiuslibet ordinis et gradus, vel quivis hominum coetus vel personae singulae voluerint sese Pontificis\nelectioni immiscere, auxilium vel favorem praestaturos.";
        ScrollingText text = new ScrollingText(massText);
        text.place(frame.getWidth(), frame.getHeight());
        frame.add(text);
        text.scroll();
    }
}