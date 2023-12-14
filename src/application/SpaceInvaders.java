package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SpaceInvaders extends Application {

    static final Random rand = new Random();
    private static final int width = 800;
    private static  final int height = 600;
    private static final int player_size = 70;
    static final Image[] player_png = {
            new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\player2.png"),
            new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\player3.png")
    };
    int index = 0;

    final Image deathstar_png = new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\deathstar2.png");
    final Image lose_png = new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\lose.png");
    static final Image[] enemies_png = {
            new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\enemy.png"),
            new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\enemy2.png"),
    };
    static final int explosion_width = 128;
    static final int explosion_widthD = 128;
    static final int explosion_row = 4;
    static final int explosion_col = 2;

    static final int explosion_rowD = 4;
    static final int explosion_colD = 4;
    static final int explosion_height = 256;
    static final int explosion_heightD = 128;
    public static final int explosion_frames = 9;
    public static final int explosionDeath_frames = 16;

    public static int getHeight() {
        return height;
    }
    public static int getWidth() { return width; }
    final int max_enemies = 10,  max_shots = max_enemies * 2;
    boolean gameOver = false;
    public static GraphicsContext gc;
    static int ok = 0;
    Player player;

    DeathStar deathStar;
    List<Shot> shots;
    List<Universe> univ;
    List<Enemy> Enemies;

    private double init_pos = (double) width / 2;
    static int score, final_score;
    static Canvas canvas;

    //start
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(width, height);
        //Scene scene = new Scene(width, height);
        gc = canvas.getGraphicsContext2D();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        //canvas.setCursor(Cursor.MOVE);
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::keyPressed);

        setup();
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setTitle("Star Wars: Rebels Ressurect");
        stage.show();

    }

    public void keyPressed(KeyEvent evt) {
        KeyCode key = evt.getCode();
        System.out.println("Key Pressed: " + key);

        if (ok == 0) {
            if (key == KeyCode.D) {
                //init_pos= init_pos+ 10;
                ok = 3;
            }
            if (key == KeyCode.A) {
                //init_pos= init_pos- 10;
                ok = 3;
            }
            if (key == KeyCode.J) {
                ok = 3;
            }
            if (key == KeyCode.ENTER) {
                ok = 1;
            }
        } else  if (ok == 3){
            if (key == KeyCode.D) {
                init_pos = init_pos + 10;
            }
            if (key == KeyCode.A) {
                init_pos= init_pos- 10;
            }
            if (key == KeyCode.J) {
                if (shots.size() < max_shots) shots.add(player.shoot());
                if (gameOver) {
                    gameOver = false;
                    setup();
                }
            }
            if (key == KeyCode.ESCAPE)
                ok = 0;
        }

        if (ok == 1) {
            if (key == KeyCode.DIGIT1) {
                index = 0;
                ok = 2;
            } else if (key == KeyCode.DIGIT2) {
                index = 1;
                ok = 2;
            }
        }

        if (ok == 4) {
            if (key == KeyCode.ESCAPE) {
                ok = 0;
                gameOver = false;
                setup();
            }
        }

    }

    private void setup() {
        univ = new ArrayList<>();
        canvas.setOnKeyPressed(this::keyPressed);
        player = new Player(width / 2, height - player_size, player_size, player_png[index]);
        deathStar = new DeathStar(600, 45, 200, deathstar_png);
        shots = new ArrayList<>();
        Enemies = new ArrayList<>();
        init_pos = (double) width / 2;

        score = 0;
        IntStream.range(0, max_enemies).mapToObj(i -> this.newEnemy()).forEach(Enemies::add);
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, width, height);
        gc.setTextAlign(TextAlignment.CENTER);

        if (ok == 0) {
            Image logo = new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\test.png");
            gc.drawImage(logo, 150,  100);
            canvas.setOnKeyPressed(this::keyPressed);
        } else if (ok == 1) {
            gc.setFont(Font.font("SansSerif", 30));
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            gc.fillText("Choose Player", (double) width / 2, 500);

            Image icon1_png = new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\icon1.png");
            Image icon2_png = new Image("file:C:\\Users\\Paul\\Desktop\\SpaceInvaders\\sprites\\icon2.png");

            gc.drawImage(icon1_png, (double) width / 2 - 350, (double) height / 2 - 100);
            gc.drawImage(icon2_png, (double) width / 2 + 150, (double) height / 2 - 100);

            canvas.setOnKeyPressed(this::keyPressed);
            setup();
        } else if (ok == 2) {
            ok = 3;
            setup();
        }
        else if (ok == 3) {
            gc.setFont(Font.font("SansSerif", 20));
            gc.setFill(Color.WHITE);
            gc.fillText("Score: " + final_score, 60, 20);

            if (gameOver) {
                ok = 4;
            } else final_score = score;
            univ.forEach(Universe::draw);

            player.update();
            player.draw();
            deathStar.update();
            deathStar.draw();

            canvas.setOnKeyPressed(this::keyPressed);
            //canvas.setOnKeyTyped(e -> keyPressed(e));

            player.posX = (int) init_pos;

            Enemies.stream().peek(Player::update).peek(Player::draw).forEach(e -> {
                if (player.collision(e) && !player.exploding) {
                    player.explode();
                    deathStar.explode();
                }
            });

            for (int i = shots.size() - 1; i >= 0; i--) {
                Shot shot = shots.get(i);
                if (shot.posY < 0 || shot.toRemove) {
                    shots.remove(i);
                    continue;
                }
                shot.update();
                shot.draw();

                for (Enemy enemy : Enemies) {
                    if (shot.collide(enemy) && !enemy.exploding) {
                        score++;
                        enemy.explode();
                        shot.toRemove = true;
                    }
                }
            }

            for (int i = Enemies.size() - 1; i >= 0; i--) {
                if (Enemies.get(i).destroyed) {
                    Enemies.set(i, newEnemy());
                }
            }

            gameOver = player.destroyed;
            if (rand.nextInt(10) > 2)
                univ.add(new Universe());

            for (int i = 0; i < univ.size(); i++) {
                if (univ.get(i).posY > height)
                    univ.remove(i);
            }
        }
        else if (ok == 4) {
            gc.drawImage(lose_png, 0, 0);
            gc.setFont(Font.font(35));
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            gc.fillText("GAME OVER", (double) width / 2, 100);
            gc.setFont(Font.font(25));
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            gc.fillText("Your Score is: " + final_score + "\n Try Again? (ESCAPE)", (double) width / 2, 500);
            canvas.setOnKeyPressed(this::keyPressed);
        }
    }

    Enemy newEnemy() {
        return new Enemy(150 + rand.nextInt(width - 100), 50 + rand.nextInt(100),
                player_size, enemies_png[rand.nextInt(enemies_png.length)]);
    }

    DeathStar newDeathStar() {
        return new DeathStar(600, 45, 300, deathstar_png);
    }

    static int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }


    public static void main(String[] args) {
        launch();
    }
}
