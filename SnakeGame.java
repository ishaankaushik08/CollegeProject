import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private int score = 0;
    private final int TILE_SIZE = 25; 
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int INIT_SNAKE_LENGTH = 5;
    
    private LinkedList<Point> snake;  // Snake body represented as a list of points
    private Point apple;  // Position of the apple
    private char direction = 'R';  // Snake's direction (R, L, U, D)
    private boolean gameOver = false;
    private Timer timer;  // Timer to control game speed
    
    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        
        // Initialize the snake
        snake = new LinkedList<>();
        for (int i = 0; i < INIT_SNAKE_LENGTH; i++) {
            snake.add(new Point(200 - i * TILE_SIZE, 300));  // Initial snake position
        }

        // Generate an apple at a random position
        spawnApple();

        // Set up key listener for snake movement
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && direction != 'D') {
                    direction = 'U';
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != 'U') {
                    direction = 'D';
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != 'R') {
                    direction = 'L';
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 'L') {
                    direction = 'R';
                }
            }
        });
        
        // Timer for game loop (30 FPS)
        timer = new Timer(200, this);
        timer.start();
    }

    // Generate a new apple at a random position
    private void spawnApple() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        int y = rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
        apple = new Point(x, y);
    }

    // Update the game state
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollisions();
            repaint();
        }
    }

    // Move the snake based on the current direction
    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = null;

        // Determine the new head position based on direction
        switch (direction) {
            case 'U': newHead = new Point(head.x, head.y - TILE_SIZE); break;
            case 'D': newHead = new Point(head.x, head.y + TILE_SIZE); break;
            case 'L': newHead = new Point(head.x - TILE_SIZE, head.y); break;
            case 'R': newHead = new Point(head.x + TILE_SIZE, head.y); break;
        }

        // Add the new head to the snake
        snake.addFirst(newHead);

        // Check if snake eats the apple
        if (newHead.equals(apple)) {
            score++;  // Increase score when apple is eaten
            spawnApple();  // Spawn a new apple
        } else {
            snake.removeLast();  // Remove the tail if no apple is eaten
        }
    }

    // Check for collisions
    private void checkCollisions() {
        Point head = snake.getFirst();

        // Check if the snake collides with walls
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true;
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    // Draw the game screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", WIDTH / 2 - 100, HEIGHT / 2 - 20);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, WIDTH / 2 - 40, HEIGHT / 2 + 20);
            return;
        }

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
        }

        // Draw the apple
        g.setColor(Color.RED);
        g.fillRect(apple.x, apple.y, TILE_SIZE, TILE_SIZE);

        // Draw current score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}