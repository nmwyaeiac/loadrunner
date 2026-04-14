package com.loderunner.UI;

import com.loderunner.GAME.game;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow {
    private static final int TILE_SIZE = 40;

    private final game gameInstance;
    private GameRenderer renderer;
    private Runnable onMenu; // Pour le retour menu

    public GameWindow(game gameInstance, Runnable onMenu) {
        this.gameInstance = gameInstance;
        this.onMenu = onMenu;
    }

    public void show(Stage primaryStage) {
        int WIDTH = gameInstance.getGameMap().getWidth() * TILE_SIZE;
        int HEIGHT = gameInstance.getGameMap().getHeight() * TILE_SIZE;

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        SpriteLoader sprites = new SpriteLoader();
        renderer = new GameRenderer(gc, WIDTH, HEIGHT, sprites);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) {
                    gameInstance.update();
                    renderer.renderGame(gameInstance.getGameMap(),
                            gameInstance.getGamePlayer(),
                            gameInstance.getScore(),
                            gameInstance.getLevel(),
                            gameInstance.isGameOver(), gameInstance.isVictory());
                    lastUpdate = now;
                }
            }
        };

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Z:
                    gameInstance.actionMoveUp();
                    break;
                case S:
                    gameInstance.actionMoveDown();
                    break;
                case Q:
                    gameInstance.actionMoveLeft();
                    break;
                case D:
                    gameInstance.actionMoveRight();
                    break;
                case A:
                    gameInstance.actionDigLeft();
                    break;
                case E:
                    gameInstance.actionDigRight();
                    break;
                case ESCAPE:
                    gameLoop.stop();
                    if (onMenu != null) onMenu.run();
                    break;
                default:
                    break;
            }
        });

        gameLoop.start();

        primaryStage.setTitle("Lode Runner L2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}