package br.com.pointel.jinpo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import br.com.pointel.jarch.mage.WizApp;
import br.com.pointel.jarch.mage.WizDesk;

public class Desk extends JFrame {

    public static void start(String[] args) {
        WizDesk.start(() -> new Desk().setVisible(true));
    }
    
    public Desk() {
        initComponents();
    }

    private final JButton buttonAdd = new JButton("+");
    private final JButton buttonDel = new JButton("-");
    private final JButton buttonRun = new JButton(">");
    private final JPanel panelAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(WizDesk.getLogo());
        setSize(300, 500);
        setName(WizApp.getName());
        setTitle(WizApp.getTitle());
        setLayout(new BorderLayout(4, 4));
        WizDesk.initFrame(this);
        WizDesk.initEscaper(this);
        panelAction.add(buttonAdd);
        panelAction.add(buttonDel);
        panelAction.add(buttonRun);
        add(panelAction, BorderLayout.NORTH);
    }

    private static class Item {

        public final File file;

        public Item(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName();
        }

    }

}
