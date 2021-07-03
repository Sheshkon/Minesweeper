import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;

public class SettingsDialog extends JDialog {

    public boolean toChanged;
    private int part_of_bombs;
    private int size;

    private static final String[] LEVELS_ARR = {
            "Easy",
            "Medium",
            "Hard"
    };
    public static final String[] FIELD_SIZE_ARR = {
            "9x9",
            "15x15",
            "20x20",
            "30x30"
    };

    SettingsDialog() {
        super(Main.mainFrame, "Settings", true);
        init();
    }

    private void init() {
        setSize(150, 145);
        toChanged = false;
        size = GameField.size;
        part_of_bombs = GameField.part_of_bombs;

        JComboBox<String> levelsComboBox = new JComboBox<>(LEVELS_ARR);
        changeComboBoxStyle(levelsComboBox);
        levelsComboBox.addActionListener(e -> part_of_bombs = levelToPart(LEVELS_ARR[levelsComboBox.getSelectedIndex()]));
        JComboBox<String> fieldSizeComboBox = new JComboBox<>(FIELD_SIZE_ARR);
        fieldSizeComboBox.addActionListener(e -> size = sizeToInt(FIELD_SIZE_ARR[fieldSizeComboBox.getSelectedIndex()]));

        JPanel panel = new JPanel();
        JLabel levelsLabel = new JLabel("Level: ");

        panel.add(levelsLabel);
        panel.add(levelsComboBox);

        JLabel fieldSizeLabel = new JLabel("Size: ");
        changeComboBoxStyle(fieldSizeComboBox);
        panel.add(fieldSizeLabel);
        panel.add(fieldSizeComboBox);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            size = GameField.size;
            part_of_bombs = GameField.part_of_bombs;
            levelsComboBox.setSelectedIndex(partToLevel(part_of_bombs));
            fieldSizeComboBox.setSelectedItem(sizeToString(size));
            setVisible(false);

        });
        cancelButton.setBackground(Color.LIGHT_GRAY);
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            if(size == GameField.size && part_of_bombs == GameField.part_of_bombs)
                setVisible(false);
            else {
                GameField.size = size;
                GameField.part_of_bombs = part_of_bombs;
                setVisible(false);
                toChanged = true;
            }
        });

        okButton.setBackground(Color.LIGHT_GRAY);
        panel.add(cancelButton);
        panel.add(okButton);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(panel);

        add(content);
        setLocationRelativeTo(Main.mainFrame);
        setVisible(true);

    }

    private void changeComboBoxStyle(JComboBox<String> comboBox) {
        Object child = comboBox.getAccessibleContext().getAccessibleChild(0);
        BasicComboPopup popup = (BasicComboPopup) child;
        JList list = popup.getList();
        list.setSelectionBackground(Color.LIGHT_GRAY);
        comboBox.getComponent(0).setBackground(Color.LIGHT_GRAY);
        comboBox.setBorder(new LineBorder(Color.BLACK));

    }

    private static int sizeToInt(String size) {
        if (size.equals(FIELD_SIZE_ARR[0]))
            return 9;
        if (size.equals(FIELD_SIZE_ARR[1]))
            return 15;
        if (size.equals(FIELD_SIZE_ARR[2]))
            return 20;
        if (size.equals(FIELD_SIZE_ARR[3]))
            return 30;
        return 0;
    }
    private static String sizeToString(int size){
        if (size == 9)
            return FIELD_SIZE_ARR[0];
        if (size == 15)
            return FIELD_SIZE_ARR[1];
        if (size == 20)
            return FIELD_SIZE_ARR[2];
        if (size == 30)
            return FIELD_SIZE_ARR[3];
        return null;
    }

    private static int levelToPart(String level) {
        if (level.equals(LEVELS_ARR[0]))
            return 10;
        if (level.equals(LEVELS_ARR[1]))
            return 7;
        if (level.equals(LEVELS_ARR[2]))
            return 5;
        return 1;
    }

    private static int partToLevel(int part){
        if (part == 10)
            return 0;
        if (part == 7)
            return 1;
        if (part == 5)
            return 2;
        return -1;
    }

}


