import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class GameField extends JPanel implements MouseListener {
    private static final JLabel WIN_LABEL = new JLabel("<html><center>You win!");
    private static final JLabel LOOSE_LABEL = new JLabel("<html><center>Game over!");
    static {

        WIN_LABEL.setHorizontalAlignment(SwingConstants.CENTER);
        WIN_LABEL.setFont(new Font("", Font.BOLD, 20));
        WIN_LABEL.setForeground(new Color(20, 117, 0));
        LOOSE_LABEL.setFont(new Font("", Font.BOLD, 20));
        LOOSE_LABEL.setForeground(Color.RED);
        LOOSE_LABEL.setHorizontalAlignment(SwingConstants.CENTER);
    }
    public  static int bombs_amount = 10;
    public static int part_of_bombs = 10;
    public  int field_size;
    public static int size = 9;
    private boolean gameIsOver;
    private Cell firstOpened;
    public int flagCounter;
    private Cell[][] field;
    private boolean start;
    private boolean isLost;


    GameField() {
        setFocusable(true);
        init();
        addMouseListener(this);
    }

    public void init() {
        start = true;
        isLost = false;
        field_size = size * 30 + 2;
        bombs_amount = size * size / part_of_bombs +2;
        setPreferredSize(new Dimension(field_size, field_size));
        field = new Cell[size][size];
        gameIsOver = false;
        flagCounter = bombs_amount;
        MainFrame.flagsAmount.setText(String.valueOf(flagCounter));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = new Cell(j * 30, i * 30);
            }
        }
        repaint();
    }

    private void addBombs() {
        int count = 0;
        int x, y;
        Random random = new Random();
        while (count != bombs_amount) {
            x = random.nextInt(size);
            y = random.nextInt(size);
            if (!field[y][x].isBomb && !field[y][x].isEqual(firstOpened)) {
                field[y][x].isBomb = true;
                count++;
            }
        }
    }

    private void addNumbers() {
        int number;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j].isBomb)
                    continue;
                number = 0;
                if (isBomb(i - 1, j - 1)) number++;
                if (isBomb(i - 1, j)) number++;
                if (isBomb(i - 1, j + 1)) number++;
                if (isBomb(i, j - 1)) number++;
                if (isBomb(i, j + 1)) number++;
                if (isBomb(i + 1, j - 1)) number++;
                if (isBomb(i + 1, j)) number++;
                if (isBomb(i + 1, j + 1)) number++;
                field[i][j].number = number;
            }
        }

    }

    private boolean isNotOutOfBounce(int i, int j) {
        if (i < 0 || i > size - 1)
            return false;
        return j >= 0 && j <= size - 1;
    }

    private boolean isBomb(int i, int j) {
        if (!isNotOutOfBounce(i, j))
            return false;
        return field[i][j].isBomb;
    }

    private boolean isFlagged(int i, int j) {
        if (!isNotOutOfBounce(i, j))
            return false;
        return field[i][j].isFlagged;
    }

    private boolean isNumber(int i, int j) {
        if (!isNotOutOfBounce(i, j))
            return false;
        return !field[i][j].isBomb && field[i][j].number != 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        Font font = new Font("", Font.BOLD, 16);
        g2d.setFont(font);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j].isBomb) {
                    if (field[i][j].isExploded) {
                        g2d.setColor(Color.red);
                        g2d.fillRect(field[i][j].x + 2, field[i][j].y + 2, 28, 28);
                    }
                    g2d.drawImage(Images.BOMB_IMG, field[i][j].x + 7, field[i][j].y + 7, this);
                }

                if (!field[i][j].isBomb && field[i][j].number != 0) {
                    g2d.setColor(colorByNumber(field[i][j].number));
                    g2d.drawString(String.valueOf(field[i][j].number), field[i][j].x + 12, field[i][j].y + 22);
                }
                g2d.setColor(Color.lightGray);
                if (!gameIsOver && field[i][j].is_hidden)
                    g2d.fillRect(field[i][j].x + 2, field[i][j].y + 2, 28, 28);
                if (field[i][j].isFlagged)
                    g2d.drawImage(Images.FLAG_IMG, field[i][j].x + 7, field[i][j].y + 7, this);

            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (e.getX() < field[i][j].x + 30 && e.getX() > field[i][j].x) {
                    if (e.getY() < field[i][j].y + 30 && e.getY() > field[i][j].y) {
                        if (e.getButton() == MouseEvent.BUTTON1 && field[i][j].is_hidden) {
                            if (start) {
                                firstOpened = new Cell(field[i][j].x, field[i][j].y);
                                addBombs();
                                addNumbers();
                                start = false;
                            }
                            if (field[i][j].number == 0) {
                                if (field[i][j].isBomb && !gameIsOver) {
                                    field[i][j].isExploded = true;
                                    isLost = true;
                                    repaint();
                                    lostTheGame();
                                }
                            }

                            openByClick(i, j);

                            field[i][j].is_hidden = false;
                            field[i][j].isFlagged = false;
                        } else if (e.getButton() == MouseEvent.BUTTON1 && isNumber(i, j) && numberAndFlags(i, j) == field[i][j].number) {
                            openNear(i, j);
                        }

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            if (!gameIsOver && field[i][j].is_hidden && !field[i][j].isFlagged && flagCounter != 0) {
                                field[i][j].isFlagged = true;
                                flagCounter--;
                            } else if (field[i][j].isFlagged) {
                                field[i][j].isFlagged = false;
                                flagCounter++;
                            }
                            MainFrame.flagsAmount.setText(String.valueOf(flagCounter));
                        }
                        repaint();

                    }

                }
            }
        }
        checkWin();

    }

    private void openByClick(int i, int j) {
        if (i < 0 || i > size - 1)
            return;
        if (j < 0 || j > size - 1)
            return;

        if (field[i][j].isBomb || field[i][j].isFlagged)
            return;

        if (field[i][j].number != 0) {
            field[i][j].is_hidden = false;
            return;
        }
        if (!field[i][j].is_hidden)
            return;

        field[i][j].is_hidden = false;
        openByClick(i - 1, j - 1);
        openByClick(i - 1, j);
        openByClick(i - 1, j + 1);
        openByClick(i, j - 1);
        openByClick(i, j + 1);
        openByClick(i + 1, j - 1);
        openByClick(i + 1, j);
        openByClick(i + 1, j + 1);
    }

    private void checkWin() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!field[i][j].isBomb && field[i][j].is_hidden)
                    return;
            }
        }
        if (!start && !gameIsOver) {
           wonTheGame();
        }
    }

    private Color colorByNumber(int number) {
        if (number == 1)
            return Color.BLUE;
        if (number == 2)
            return new Color(20, 117, 0);
        if (number == 3)
            return Color.RED;
        if (number == 4)
            return new Color(0, 8, 80);
        if (number == 5)
            return new Color(105, 0, 0);
        if (number == 6)
            return new Color(200, 2, 80);
        if (number == 7)
            return new Color(190, 190, 0);
        if (number == 8)
            return new Color(10, 50, 0);

        return null;
    }

    private int numberAndFlags(int i, int j) {
        int counter = 0;
        if (isFlagged(i - 1, j - 1)) counter++;
        if (isFlagged(i - 1, j)) counter++;
        if (isFlagged(i - 1, j + 1)) counter++;
        if (isFlagged(i, j - 1)) counter++;
        if (isFlagged(i, j + 1)) counter++;
        if (isFlagged(i + 1, j - 1)) counter++;
        if (isFlagged(i + 1, j)) counter++;
        if (isFlagged(i + 1, j + 1)) counter++;
        return counter;
    }

    private void openNear(int i, int j) {
        if (isNotOutOfBounce(i - 1, j - 1) && !field[i - 1][j - 1].isFlagged) {
            if (field[i - 1][j - 1].isBomb) {
                field[i - 1][j - 1].isExploded = true;
                field[i - 1][j - 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i - 1, j - 1);
            }
        }

        if (isNotOutOfBounce(i - 1, j) && !field[i - 1][j].isFlagged) {
            if (field[i - 1][j].isBomb) {
                field[i - 1][j].isExploded = true;
                field[i - 1][j].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i - 1, j);
            }
        }
        if (isNotOutOfBounce(i - 1, j + 1) && !field[i - 1][j + 1].isFlagged) {
            if (field[i - 1][j + 1].isBomb) {
                field[i - 1][j + 1].isExploded = true;
                field[i - 1][j + 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i - 1, j + 1);
            }
        }
        if (isNotOutOfBounce(i, j - 1) && !field[i][j - 1].isFlagged) {
            if (field[i][j - 1].isBomb) {
                field[i][j - 1].isExploded = true;
                field[i][j - 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i, j - 1);
            }
        }
        if (isNotOutOfBounce(i, j + 1) && !field[i][j + 1].isFlagged) {
            if (field[i][j + 1].isBomb) {
                field[i][j + 1].isExploded = true;
                field[i][j + 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i, j + 1);

            }
        }
        if (isNotOutOfBounce(i + 1, j - 1) && !field[i + 1][j - 1].isFlagged) {
            if (field[i + 1][j - 1].isBomb) {
                field[i + 1][j - 1].isExploded = true;
                field[i + 1][j - 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i + 1, j - 1);
            }
        }
        if (isNotOutOfBounce(i + 1, j) && !field[i + 1][j].isFlagged) {
            if (field[i + 1][j].isBomb) {
                field[i + 1][j].isExploded = true;
                field[i + 1][j].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i + 1, j);
            }
        }

        if (isNotOutOfBounce(i + 1, j + 1) && !field[i + 1][j + 1].isFlagged) {
            if (field[i + 1][j + 1].isBomb) {
                field[i + 1][j + 1].isExploded = true;
                field[i + 1][j + 1].is_hidden = false;
                isLost = true;
            } else {
                openByClick(i + 1, j + 1);
            }
        }

        repaint();
        if (isLost) {
            lostTheGame();
        }

        checkWin();

    }

    private void wonTheGame(){


        MainFrame.smile.setIcon(Images.SMILE_WITH_GLASSES_IMG);
        JOptionPane.showMessageDialog(this, WIN_LABEL, "Game", JOptionPane.PLAIN_MESSAGE);
        gameIsOver = true;
        repaint();
    }

    private void lostTheGame(){
        gameIsOver = true;
        MainFrame.smile.setIcon(Images.SAD_SMILE_IMG);
        JOptionPane.showMessageDialog(this, LOOSE_LABEL, "Game", JOptionPane.PLAIN_MESSAGE);
    }



    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}