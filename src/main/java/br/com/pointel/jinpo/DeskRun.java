package br.com.pointel.jinpo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import br.com.pointel.jarch.mage.WizApp;
import br.com.pointel.jarch.mage.WizDesk;

public class DeskRun extends JFrame {

    private final File file;

    public DeskRun(File file) throws Exception {
        this.file = file;
        initComponents();
    }

    private final List<Item> listItems = new ArrayList<>();
    private final List<String> textParts = new ArrayList<>();
    private final JPanel panelItems = new JPanel();
    private final JScrollPane scrollItems = new JScrollPane(panelItems);

    private final JButton buttonCopy = new JButton("Copy");
    private final JPanel panelAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
    
    private void initComponents() throws Exception {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(WizDesk.getLogo());
        setSize(400, 600);
        setName(WizApp.getName() + "_run");
        setTitle(WizApp.getTitle() + " - " + file.getName());
        setLayout(new BorderLayout(4, 4));
        WizDesk.initFrame(this);
        WizDesk.initEscaper(this);
        add(scrollItems, BorderLayout.CENTER);
        buttonCopy.addActionListener(e -> actCopy());
        panelAction.add(buttonCopy);
        add(panelAction, BorderLayout.SOUTH);
        loadItems();
    }

    private void loadItems() throws Exception {
        var origin = Files.readString(file.toPath());
        var begin = origin.indexOf("|[");
        var end = -2;
        while (begin > -1) {
            textParts.add(origin.substring(end + 2, begin));
            end = origin.indexOf("]|", begin);
            if (end == -1) {
                end = origin.length();
            }
            var source = origin.substring(begin + 2, end);
            var enumeration = source.indexOf(":");
            if (enumeration > -1) {
                var title = source.substring(0, enumeration).trim();
                var options = source.substring(enumeration + 1).trim();
                var model = new DefaultComboBoxModel<String>();
                model.addElement("");
                for (var option : options.split("/")) {
                    model.addElement(option.trim());
                }
                var combo = new JComboBox<>(model);
                listItems.add(new Item(begin, title, combo));

            } else {
                var title = source.trim();
                var editText = new JTextField();
                listItems.add(new Item(begin, title, editText));
            }
            begin = origin.indexOf("|[", end);
        }
        textParts.add(origin.substring(end + 2, origin.length()));
        panelItems.setLayout(new GridLayout(listItems.size(), 2));
        for (var item : listItems) {
            var label = new JLabel(item.title + " : ");
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            panelItems.add(label);
            if (item.editText != null) {
                panelItems.add(item.editText);
            } else {
                panelItems.add(item.editCombo);
            }
        }
    }

    private void actCopy() {
        var builder = new StringBuilder();
        for (int i = 0; i < listItems.size(); i++) {
            var item = listItems.get(i);
            builder.append(textParts.get(i));
            if (item.editText != null) {
                builder.append(item.editText.getText().trim());
            } else {
                builder.append(item.editCombo.getSelectedItem().toString().trim());
            }
        }
        builder.append(textParts.get(textParts.size() - 1));
        WizDesk.copyToClipboard(builder.toString());
        WizDesk.close(this);
    }

    private class Item {

        public final Integer position;
        public final String title;
        public final JTextField editText;
        public final JComboBox<String> editCombo;

        public Item(Integer position, String title, JTextField editText) {
            this.position = position;
            this.title = title;
            this.editText = editText;
            this.editCombo = null;
        }

        public Item(Integer position, String title, JComboBox<String> editCombo) {
            this.position = position;
            this.title = title;
            this.editText = null;
            this.editCombo = editCombo;
        }

    }

}
