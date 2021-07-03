import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    public static JMenuBar menuBar;
    public static GameField game;
    public static JLabel flagsAmount = new JLabel();
    public static JLabel smile;
    public static JLabel settings;
    public static JLabel flag;
    public static SettingsDialog settingsDialog;

    MainFrame() {
        setTitle("Minesweeper");
        game = new GameField();
        add(game);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createMenu();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        settings = new JLabel();
        flag = new JLabel();
        smile = new JLabel();

        settings.setIcon(Images.SETTINGS_IMG);
        flag.setIcon(Images.FLAG_IMG_ICON);
        settings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (settingsDialog == null)
                    settingsDialog = new SettingsDialog();
                else {
                    settingsDialog.setLocationRelativeTo(Main.mainFrame);
                    settingsDialog.setVisible(true);
                }
                if (settingsDialog.toChanged) {
                    setVisible(false);
                    settingsDialog.toChanged = false;
                    remove(game);
                    game = new GameField();
                    add(game);
                    setMenu();

                    pack();
                    setLocationRelativeTo(null);
                    setVisible(true);

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                settings.setIcon(Images.SETTINGS_BLUR_IMG);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                settings.setIcon(Images.SETTINGS_IMG);
            }
        });


        smile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                game.init();

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                smile.setIcon(Images.FUNNY_SMILE_BLUR_IMG);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                smile.setIcon(Images.FUNNY_SMILE_IMG);
            }
        });
        setMenu();
    }

    private void setMenu() {
        smile.setIcon(Images.FUNNY_SMILE_IMG);
        menuBar = new JMenuBar();
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(settings);
        menuBar.add(Box.createHorizontalStrut((MainFrame.game.field_size) / 2 - 55));
        menuBar.add(smile);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(flag);
        menuBar.add(Box.createHorizontalStrut(5));
        flagsAmount.setText(String.valueOf(game.flagCounter));
        menuBar.add(flagsAmount);
        menuBar.add(Box.createHorizontalStrut(10));
        setJMenuBar(menuBar);
    }
}
