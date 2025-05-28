import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window {
    private JFrame frame;

    public Window(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
    }

    public Window(String title, int width, int height) {
        this(title);
        frame.setSize(width, height);
    }

    void open() {
        frame.setVisible(true);
    }

    void writeMass() {
        String massText = "Nos omnes et singuli in hac electione Summi Pontificis versantes Cardinales electores promittimus,\nvovemus et iuramus inviolate et ad unguem Nos esse fideliter et diligenter observaturos omnia quae\ncontinentur in Constitutione Apostolica Summi Pontificis Ioannis Pauli II, quae a verbis\n«Universi Dominici Gregis» incipit, data die XXII mensis Februarii anno MCMXCVI. Item promittimus,\nvovemus et iuramus, quicumque nostrum, Deo sic disponente, Romanus Pontifex erit electus,\neum munus Petrinum Pastoris Ecclesiae universae fideliter exsecuturum esse atque spiritualia\net temporalia iura libertatemque Sanctae Sedis integre ac strenue asserere atque tueri\nnumquam esse destiturum. Praecipue autem promittimus et iuramus Nos religiosissime\net quoad cunctos, sive clericos sive laicos, secretum esse servaturos de iis omnibus, quae ad\nelectionem Romani Pontificis quomodolibet pertinent, et de iis, quae in loco electionis aguntur,\nscrutinium directe vel indirecte respicientibus; neque idem secretum quoquo modo violaturos sive\nperdurante novi Pontificis electione, sive etiam post, nisi expressa facultas ab eodem Pontifice tributa\nsit, itemque nulli consensioni, dissensioni, aliique cuilibet intercessioni, quibus auctoritates saeculares\ncuiuslibet ordinis et gradus, vel quivis hominum coetus vel personae singulae voluerint sese Pontificis\nelectioni immiscere, auxilium vel favorem praestaturos.";
        ScrollingText text = new ScrollingText(massText);
        text.place(frame.getWidth(), frame.getHeight());
        frame.add(text);
        text.scroll();
    }

    void write(String text) {
        Font newFont = new Font("Arial", Font.BOLD, 24);
        JLabel label = new JLabel(text);
        label.setFont(newFont);
        frame.add(label);
    }
}