/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelathinh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Mypc
 */
public class ButtonEvent extends JPanel implements ActionListener {

    private Point p1 = null;
    private Point p2 = null;
    private int row;
    private int col;
    private int bound = 2;
    private int size = 50;
    private JButton[][] btn;
    private Controller controller;
    private Color backGroundColor = Color.lightGray;
    private Color backGroundColor2 = Color.white;

    private GameFrame frame;
    boolean checking = false;
    int score = 0;

    public ButtonEvent(GameFrame frame, int row, int col) {
        this.frame = frame;
        this.row = row;
        this.col = col;

        setLayout(new GridLayout(row, col, bound, bound));
        setBackground(backGroundColor);
        setPreferredSize(new Dimension((size + bound) * col, (size + bound)
                * row));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setAlignmentY(JPanel.CENTER_ALIGNMENT);

        newGame();

    }

    public void newGame() {
        controller = new Controller(this.row, this.col);
        generateColor();
        addArrayButton();
    }
    int r = 0, g = 0, b = 0;

    public void execute(Point p1, Point p2) {
        setDisable(btn[p1.x][p1.y]);
        setDisable(btn[p2.x][p2.y]);
    }

    private void setDisable(JButton btn) {
        btn.setBorder(new LineBorder(Color.BLUE));
        btn.setEnabled(false);
    }

    private void addArrayButton() {
        btn = new JButton[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                btn[i][j] = createButton(i + "," + j);
                Color color = getColorD(controller.getMatrix()[i][j]);
                add(btn[i][j]);
                btn[i][j].setBackground(backGroundColor2);

            }
        }
    }

    public void resetColor() {
        Random random = new Random();
        r = random.nextInt(254);
        g = random.nextInt(254);
        b = random.nextInt(254);
    }
    Color[] color = new Color[22];

    public void generateColor() {
        color[0] = backGroundColor2;
        for (int i = 1; i <= 21; i++) {
            resetColor();
            color[i] = new Color(r, g, b);
        }
    }

    public Color getColorD(int index) {
        return color[index];
    }

    private JButton createButton(String action) {
        JButton btn = new JButton();
        btn.setActionCommand(action);
        btn.setBorder(null);
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btnIndex = e.getActionCommand();
        int indexDot = btnIndex.lastIndexOf(",");
        int x = Integer.parseInt(btnIndex.substring(0, indexDot));
        int y = Integer.parseInt(btnIndex.substring(indexDot + 1,
                btnIndex.length()));
        if (p1 == null) {
            p1 = new Point(x, y);
            btn[p1.x][p1.y].setBorder(new LineBorder(Color.red));
            btn[p1.x][p1.y].setBackground(getColorD(controller.getMatrix()[p1.x][p1.y]));
        } else {
            p2 = new Point(x, y);
            checking = controller.checkTwoPoint(p1, p2);
            if (checking) {
                btn[p2.x][p2.y].setBackground(getColorD(controller.getMatrix()[p2.x][p2.y]));
                execute(p1, p2);
                checking = false;
                score += 2;
                p1 = null;
                p2 = null;
            } else {
                btn[p2.x][p2.y].setBorder(new LineBorder(Color.yellow));
                btn[p1.x][p1.y].setBorder(null);
                btn[p1.x][p1.y].setBackground(backGroundColor2);

                p1 = null;
                p2 = null;
            }
            if (score == 64) {
                showDialogNewGame("Chơi lại?", "Chiến thắng", 1);
            }
        }
    }

    public void showDialogNewGame(String message, String title, int t) {
        int select = JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                null, null);
        if (select == 0) {
            newGame();
        } else {
            if (t == 1) {
                System.exit(0);
            }
        }
    }
}
