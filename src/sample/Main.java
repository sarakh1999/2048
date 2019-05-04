package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main extends Application {

    enum State {
        start, running, won, over
    }

    static int highestValue;
    static int score;
    private Random rand = new Random();
    private Tile[][] tiles;
    Scanner scanner = new Scanner(System.in);
    private int n = scanner.nextInt();
    private State gameState = State.start;
    private boolean checkingAvailableMoves;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Group rootMenu = new Group();
        Scene sceneMenu = new Scene(rootMenu, 800, 600);
        Rectangle rectangle1 = new Rectangle(200, 100);
        rectangle1.relocate(310, 120);
        rectangle1.setFill(Color.GOLD);
        ArrayList<Rectangle> rects = new ArrayList<>();
        Label label = new Label("2048");
        label.relocate(310, 120);
        label.setFont(Font.font(80));
        Button button1 = new Button();
        button1.relocate(400, 300);
        button1.setText("Quit");
        button1.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                exit(0);
            }
        });
        primaryStage.setScene(sceneMenu);
        primaryStage.show();
        Button button = new Button();
        button.relocate(400, 250);
        button.setText("Play");
        rootMenu.getChildren().addAll(button, rectangle1, button1, label);
        button.setOnMouseClicked(event -> {
            Group root = new Group();
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();


            if (event.getButton() == MouseButton.PRIMARY) {
                if (gameState != State.running) {
                    score = 0;
                    highestValue = 0;
                    gameState = State.running;
                    tiles = new Tile[n][n];
                    addRandomTile();
                    addRandomTile();
                }
                if (gameState == State.running) {
                    scene.setOnKeyPressed(event1 -> {
                                switch (event1.getCode()) {
                                    case UP:
                                        moveUp();
                                        break;
                                    case DOWN:
                                        moveDown();
                                        break;
                                    case LEFT:
                                        moveLeft();
                                        break;
                                    case RIGHT:
                                        moveRight();
                                        break;
                                }
                                System.out.println(gameState);
                                rects.clear();
                                root.getChildren().clear();
                                rectMake(root, rects);
                                if (gameState == State.over) {
                                    message("Game Over!", root);
                                }
                                if (gameState == State.won) {
                                    message("You Won!", root);
                                }
                            }
                    );

                } else {
                    Rectangle rectangle = new Rectangle(400, 400);
                    rectangle.relocate(200, 100);
                    rectangle.setFill(Color.BLACK);
                    root.getChildren().add(rectangle);
                    Label ll = new Label("2048");
                    ll.relocate(310, 270);
                    ll.setFont(Font.font(100));
                    root.getChildren().add(ll);
                }
            }
        });


    }

    private void message(String s, Group root) {
        Label l = new Label(s);
        Label l1 = new Label("Score:" + score);
        root.getChildren().clear();
        l.relocate(400, 300);
        l1.relocate(400, 350);
        l.setFont(Font.font(34));
        l1.setFont(Font.font(34));
        root.getChildren().addAll(l, l1);
    }

    private void rectMake(Group root, ArrayList<Rectangle> rects) {
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (tiles[r][c] == null) {
                    Rectangle rectangle = new Rectangle(100, 100);
                    rectangle.relocate(200 + c * 121, 100 + r * 121);
                    rectangle.setFill(Color.BEIGE);
                    root.getChildren().add(rectangle);
                    rects.add(rectangle);
                } else {
                    int value = tiles[r][c].getValue();
                    Rectangle rectangle = new Rectangle(100, 100);
                    rectangle.relocate(200 + c * 121, 100 + r * 121);
                    if (value == 2) {
                        rectangle.setFill(Color.rgb(112, 23, 16));
                    }
                    if (value == 4) {
                        rectangle.setFill(Color.rgb(255, 228, 195));
                    }
                    if (value == 8) {
                        rectangle.setFill(Color.rgb(231, 176, 142));
                    }
                    if (value == 16) {
                        rectangle.setFill(Color.rgb(255, 196, 195));
                    }
                    if (value == 32) {
                        rectangle.setFill(Color.rgb(231, 148, 142));
                    }
                    if (value == 64) {
                        rectangle.setFill(Color.rgb(190, 126, 86));
                    }
                    if (value == 128) {
                        rectangle.setFill(Color.rgb(190, 94, 86));
                    }
                    if (value == 256) {
                        rectangle.setFill(Color.rgb(156, 57, 49));
                    }
                    if (value == 512) {
                        rectangle.setFill(Color.rgb(231, 150, 160));
                    }
                    if (value == 1024) {
                        rectangle.setFill(Color.rgb(200, 196, 195));
                    }
                    if (value == 2048) {
                        rectangle.setFill(Color.rgb(221, 230, 160));
                    }
                    root.getChildren().add(rectangle);
                    rects.add(rectangle);

                    int x = 200 + c * 121 + (106 - 33) / 2 + 12;
                    int y = 100 + r * 121 + (45 + (106 - (85 + 10)) / 2) - 12;
                    Label l = new Label(Integer.toString(value));
                    l.relocate(x, y);
                    l.setFont(Font.font(28));
                    root.getChildren().add(l);
                    Label ll = new Label("Score : " + score);
                    ll.relocate(10, 50);
                    ll.setFont(Font.font(40));
                    root.getChildren().add(ll);

                }
            }
        }
    }


    void addRandomTile() {
        int position = rand.nextInt(n * n);
        int row, col;
        do {
            position = (position + 1) % (n * n);
            row = position / n;
            col = position % n;
        } while (tiles[row][col] != null);

        int val = rand.nextInt(10) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }

    private boolean moveTile(int countDownFrom, int yArc, int xArc) {
        boolean moved = false;

        for (int i = 0; i < n * n; i++) {
            int j = Math.abs(countDownFrom - i);

            int r = j / n;
            int c = j % n;

            if (tiles[r][c] == null)
                continue;

            int nextR = r + yArc;
            int nextC = c + xArc;

            while (nextR >= 0 && nextR < n && nextC >= 0 && nextC < n) {

                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];

                if (next == null) {
                    if (checkingAvailableMoves)
                        return true;
                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yArc;
                    nextC += xArc;
                    moved = true;

                } else if (next.canMergeWith(curr)) {

                    if (checkingAvailableMoves)
                        return true;

                    int value = next.mergeWith(curr);
                    if (value > highestValue)
                        highestValue = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }

        if (moved) {
            if (highestValue < 2048) {
                clearMerged();
                addRandomTile();
                if (!movesAvailable()) {
                    gameState = State.over;
                }
            } else if (highestValue == 2048)
                gameState = State.won;
        }

        return moved;
    }

    void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }

    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }

    boolean moveUp() {
        return moveTile(0, -1, 0);
    }

    boolean moveDown() {
        return moveTile(n * n - 1, 1, 0);
    }

    boolean moveLeft() {
        return moveTile(0, 0, -1);
    }

    boolean moveRight() {
        return moveTile(n * n - 1, 0, 1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Tile {
    private boolean merged;
    private int value;

    Tile(int number) {
        value = number;
    }

    int getValue() {
        return value;
    }

    void setMerged(boolean m) {
        merged = m;
    }

    boolean canMergeWith(Tile other) {
        if (!merged && other != null) {
            if (this.value == other.value) {
                return true;
            }
        }
        return false;
    }

    int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            value *= 2;
            merged = true;
            return value;
        }
        return -1;
    }
}