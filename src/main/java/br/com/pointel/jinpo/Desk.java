package br.com.pointel.jinpo;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import br.com.pointel.jarch.mage.WizApp;
import br.com.pointel.jarch.mage.WizData;
import br.com.pointel.jarch.mage.WizDesk;
import br.com.pointel.jarch.mage.WizLang;

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

    private final DefaultListModel<Item> modelItems = new DefaultListModel<>();
    private final JList<Item> listItems = new JList<>(modelItems);
    private final JScrollPane scrollItems = new JScrollPane(listItems);
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(WizDesk.getLogo());
        setSize(300, 500);
        setName(WizApp.getName());
        setTitle(WizApp.getTitle());
        setLayout(new BorderLayout(4, 4));
        WizDesk.initFrame(this);
        WizDesk.initEscaper(this);
        buttonAdd.addActionListener(e -> actAdd());
        panelAction.add(buttonAdd);
        buttonDel.addActionListener(e -> actDel());
        panelAction.add(buttonDel);
        buttonRun.addActionListener(e -> actRun());
        panelAction.add(buttonRun);
        add(panelAction, BorderLayout.NORTH);
        listItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    actRun();
                }
            }
        });
        add(scrollItems, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    saveItems();
                } catch (Exception ex) {
                    WizDesk.showError(ex);
                }
            }

            public void windowOpened(WindowEvent e) {
                try {
                    loadItems();
                } catch (Exception ex) {
                    WizDesk.showError(ex);
                }
            };
        });
    }

    private void actAdd() {
        var selected = WizDesk.selectFile(null);
        if (selected != null) {
            modelItems.addElement(new Item(selected));
        }
    }

    private void actDel() {
        var selected = listItems.getSelectedValue();
        if (selected != null) {
            modelItems.removeElement(selected);
        }
    }
    
    private void actRun() {
        var selected = listItems.getSelectedValue();
        if (selected != null) {
            try {
                new DeskRun(selected.file).setVisible(true);
            } catch (Exception e) {
                WizDesk.showError(e);
            }
        }
    }

    private void loadItems() throws Exception {
        var file = new File(WizLang.getPointelAppDir(), "items.json");
        if (file.exists()) {
            var items = WizData.fromFile(ListItems.class, file);
            for (var item : items) {
                modelItems.addElement(item);
            }
        }
    }

    private void saveItems() throws Exception {
        var file = new File(WizLang.getPointelAppDir(), "items.json");
        var items = new ListItems();
        for (int i = 0; i < modelItems.size(); i++) {
            items.add(modelItems.get(i));
        }
        WizData.toFile(items, file);
    }

    private static class Item implements Serializable {

        public final File file;

        public Item(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName();
        }

    }

    private static class ListItems extends ArrayList<Item> {}

}
