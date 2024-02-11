import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class BrickBreakerGame extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 500;
    private  int PADDLE_WIDTH = 120;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 40;
    private static final int BRICK_HEIGHT = 15;
    private  int NUM_BRICKS = 30;
    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int paddleY = HEIGHT - 20;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballSpeedX = 2;
    private int ballSpeedY = 2;
    private int score;
    private List<Rectangle> bricks;
    private int currentLevel = 1;
    private Label Level, Score;

    public BrickBreakerGame() {
        setTitle("Brick Breaker Game");
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        startGame();
        bricks = new ArrayList<>();
        score=0;
        Level = new Label("Level : "+currentLevel);
        Level.setBounds(10,5,150,50);
        Level.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
        Level.setForeground(Color.GREEN);
        add(Level);
        Score = new Label("Score : "+score);
        Score.setBounds(280,5,150,50);
        Score.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
        Score.setForeground(Color.GREEN);
        add(Score);
        createBricks(currentLevel);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 0) {
                    paddleX -= 10;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX < WIDTH - PADDLE_WIDTH) {
                    paddleX += 10;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        setFocusable(true);
    }

    private void createBricks(int level) {
        int initialX = 50;
        int initialY = 100;
        int bricksPerRow=6;

        for (int i = 0; i < NUM_BRICKS; i++) {
            int brickX = initialX + (i % bricksPerRow) * (BRICK_WIDTH + 10);
            int brickY = initialY + (i / bricksPerRow) * (BRICK_HEIGHT + 10);

            // Adjust the brick position based on the level
            switch (level) {
                case 1:
                    // Level 1
                    break;
                case 2:
                    // Level 2
                    NUM_BRICKS=21;
                    PADDLE_WIDTH=80;
                    Level.setText("Level : "+currentLevel);
                    bricksPerRow=3;
                    brickX+=80;
                    brickY+=20;
                    break;
                case 3:
                    // Level 3
                    NUM_BRICKS=10;
                    PADDLE_WIDTH=60;
                    Level.setText("Level : "+currentLevel);
                    bricksPerRow=2;
                    brickX+=10;
                    brickY+=30;
                    break;
                default:
                    break;
            }
            bricks.add(new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT));
        }
    }

    private void startGame() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        timer.start();
    }

    private void updateGame() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball and paddle collision
        if (new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE).intersects
                (new Rectangle(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT)))
        {
            ballSpeedY = -ballSpeedY;
        }

        // Ball and brick collision
        for (int i = 0; i < bricks.size(); i++) {
            if (new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE).intersects(bricks.get(i))) {
                score+=10;
                Score.setText("Score : "+score);
                bricks.remove(i);
                ballSpeedY = -ballSpeedY;
            }
        }

        // Wall collisions
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            ballSpeedX = -ballSpeedX;
        }
        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        // Check if the player has won the current level
        if (bricks.isEmpty()) {
            currentLevel++;
            if (currentLevel <= 3) {
                JOptionPane.showMessageDialog(null, "Congratulations! You won!"
                        +"\nPress OK to Proceed to Next Level");
                bricks.clear();
                createBricks(currentLevel);
            }
            else {
                // Player has won all levels
                JOptionPane.showMessageDialog(null, "Congratulations! You won!" +
                        "\nYour Score is : "+score);
                System.exit(0);
            }
        }

        // Check if the game is over
        if (ballY >= HEIGHT) {
            JOptionPane.showMessageDialog(null, "Game Over. Try again!"
                    + "\nYour score is : "+ score);
            System.exit(0);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw paddle
        g2d.setColor(Color.BLUE);
        g2d.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g2d.setColor(Color.RED);
        g2d.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw bricks
        g2d.setColor(Color.WHITE);
        for (Rectangle brick : bricks) {
            g2d.fillRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
        }
    }

    public static void main(String[] args) {
        new BrickBreakerGame();
    }
}


