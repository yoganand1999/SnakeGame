package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    private Image apple;
    private Image dot;
    private Image head;
    private int dots;
    private final int AllDots = 900;
    private final int DotSize = 10;
    private int appleX;
    private int appleY;
    private int score = 0;

    private final int x[] = new int[AllDots];
    private final int y[] = new int[AllDots];
    private final int RandomPosition = 45;
    private Timer timer;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);
        loadImage();
        initializeGame();
    }

    public void loadImage() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("SnakeGame/icons/apple.png"));
        apple = i1.getImage();
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("SnakeGame/icons/dot.png"));
        dot = i2.getImage();
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("SnakeGame/icons/head.png"));
        head = i3.getImage();
    }

    public void initializeGame() {
        // Display a dialog box to ask the user to select the level
        String[] options = {"Easy", "Medium", "Hard"};
        int level = JOptionPane.showOptionDialog(
                null,
                "Select the level:",
                "Snake Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (level) {
            case 0:
                timer = new Timer(140, this);
                break;
            case 1:

                timer = new Timer((int) (140 / 1.5), this);
                break;
            case 2:

                timer = new Timer(140 / 2, this);
                break;
            default:

                timer = new Timer(140, this);
                break;
        }

        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DotSize;
        }

        locateApple();
        timer.start();
    }



    public void locateApple() {
        int r = (int) (Math.random() * RandomPosition);
        appleX = r * DotSize;

        r = (int) (Math.random() * RandomPosition);
        appleY = r * DotSize;
        if (appleX >= 300 - DotSize) {
            appleX = 300 - DotSize;
        }
        if (appleY >= 300 - DotSize) {
            appleY = 300 - DotSize;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }


            String scoreString = "Score: " + score;
            Font font = new Font("SAN SERIF", Font.BOLD, 14);
            FontMetrics metrics = getFontMetrics(font);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(scoreString, (300 - metrics.stringWidth(scoreString)) / 2, 20);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over - Your Score: " + score;
        Font font = new Font("SAN SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300 - metrics.stringWidth(msg)) / 2, 300 / 2);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] = x[0] - DotSize;
        }
        if (rightDirection) {
            x[0] = x[0] + DotSize;
        }
        if (upDirection) {
            y[0] = y[0] - DotSize;
        }
        if (downDirection) {
            y[0] = y[0] + DotSize;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            locateApple();
            score++; // Increment the score when the apple is eaten
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
            if (x[0] <= 0) {
                inGame = false;
            }
            if (y[0] <= 0) {
                inGame = false;
            }
            if (x[0] >= 300) {
                inGame = false;
            }
            if (y[0] >= 300) {
                inGame = false;
            }
            if (!inGame) {
                timer.stop();
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
            repaint();

        }
    }


    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}

