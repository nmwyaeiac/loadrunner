package com.loderunner.UI;

import com.loderunner.GAME.game;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * GameWindow
 */
public class GameWindow {
  // taille d un block en pixels (a pas toucher sinon l affichage pete)
  private static final int TILE_SIZE = 40;
  // 20 colonnes et 15 lignes max pr ce test

  private final game gameInstance;
  private GameRenderer renderer;

  public GameWindow(game gameInstance) {
    this.gameInstance = gameInstance;
  }

  public void show(Stage primaryStage) {
    int WIDTH = gameInstance.getGameMap().getWidth() * TILE_SIZE;
    int HEIGHT = gameInstance.getGameMap().getHeight() * TILE_SIZE;
    // le canvas cest notre zone de dessin
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    SpriteLoader sprites = new SpriteLoader();
    renderer = new GameRenderer(gc, WIDTH, HEIGHT, sprites);
    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

    // on ecoute les touches du clavier
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
        default:
          break;
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
          renderer.renderGame(gameInstance.getGameMap(),
              gameInstance.getGamePlayer(),
              gameInstance.getScore(),
              gameInstance.getLevel(),
              gameInstance.isGameOver(), gameInstance.isVictory());
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
}
