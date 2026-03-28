package com.loderunner;

import com.loderunner.GAME.game;
import com.loderunner.MAP.map;
import com.loderunner.MAP.tiletype;
import com.loderunner.ENTITY.player;
import com.loderunner.ENTITY.enemy;
import com.loderunner.ENTITY.gold;
import com.loderunner.ENTITY.WALL.brick;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // taille d un block en pixels (a pas toucher sinon l affichage pete)
    private static final int TILE_SIZE = 40;
    // 20 colonnes et 15 lignes max pr ce test
    private static final int WIDTH = 20 * TILE_SIZE;
    private static final int HEIGHT = 15 * TILE_SIZE;

    // l instance globale du jeu
    private game gameInstance;

    @Override
    public void start(Stage primaryStage) {
        // on init le jeu et on charge le niveau depuis le txt
        gameInstance = new game();
        try {
            gameInstance.loadlevel(1);
        } catch (IOException e) {
            System.out.println("bug de chargement gros : " + e.getMessage());
            return; // on stop tt si le niveau crash
        }

        // le canvas cest notre zone de dessin
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        // on ecoute les touches du clavier
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Z: gameInstance.actionMoveUp(); break;
                case S: gameInstance.actionMoveDown(); break;
                case Q: gameInstance.actionMoveLeft(); break;
                case D: gameInstance.actionMoveRight(); break;
                case A: gameInstance.actionDigLeft(); break;
                case E: gameInstance.actionDigRight(); break;
                default: break;
            }
        });

        // le moteur du jeu (tourne en boucle auto)
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // on lock a 10 fps pr eviter que le perso aille a 200 a l heure
                if (now - lastUpdate >= 100_000_000) {

                    // 1. on calcule la physique et l ia
                    gameInstance.update();

                    // 2. on dessine tout
                    renderGame(gc);

                    lastUpdate = now;
                }
            }
        };

        gameLoop.start();

        // on affiche la fenetre avec un titre
        primaryStage.setTitle("Lode Runner L2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // fct pr dessiner chaque element sur la map
    private void renderGame(GraphicsContext gc) {
        // on clear avec un bg noir a chaque frame
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        map m = gameInstance.getGameMap();
        if (m == null) return;

        // on parcours la grille pr dessiner les blocs statiques
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                tiletype t = m.getTile(x, y);
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;

                if (t == tiletype.INDESTRUCTIBLE_WALL) {
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                }
                else if (t == tiletype.DESTRUCTIBLE_WALL) {
                    // verif si la brique est detruite ou pas
                    brick b = m.getBrickAt(x, y);
                    if (b != null && !b.getIsBroken()) {
                        gc.setFill(Color.BROWN);
                        gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                    }
                }
                else if (t == tiletype.LADDER) {
                    gc.setFill(Color.LIGHTGRAY);
                    // petite echelle fine pr le style
                    gc.fillRect(px + 10, py, TILE_SIZE - 20, TILE_SIZE);
                }
                else if (t == tiletype.MONKEY_BAR) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(px, py + 10, TILE_SIZE, 10);
                }
            }
        }

        // on dessine l or
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                if(m.getGoldAt(x, y) != null) {
                    gc.setFill(Color.YELLOW);
                    gc.fillOval(x * TILE_SIZE + 10, y * TILE_SIZE + 10, 20, 20);
                }
            }
        }

        // on dessine les bots
        gc.setFill(Color.RED);
        for (enemy e : m.getEnemy()) {
            gc.fillRect(e.getX() * TILE_SIZE, e.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // on dessine notre perso
        player p = gameInstance.getGamePlayer();
        if (p != null) {
            gc.setFill(Color.BLUE);
            gc.fillRect(p.getX() * TILE_SIZE, p.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // l interface en haut a gauche
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + gameInstance.getScore() + " | Vies: " + p.getlife(), 10, 20);

        // les ecrans de fin de game
        if (gameInstance.isGameOver()) {
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", WIDTH / 2 - 40, HEIGHT / 2);
        } else if (gameInstance.isVictory()) {
            gc.setFill(Color.GREEN);
            gc.fillText("GG NIVEAU FINI", WIDTH / 2 - 40, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}