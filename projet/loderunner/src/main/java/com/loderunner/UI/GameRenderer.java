package com.loderunner.UI;

import com.loderunner.ENTITY.enemy;
import com.loderunner.ENTITY.player;
import com.loderunner.ENTITY.WALL.brick;
import com.loderunner.MAP.map;
import com.loderunner.MAP.tiletype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * GameWindow
 */
public class GameRenderer {
  // taille d un block en pixels (a pas toucher sinon l affichage pete)
  private static final int TILE_SIZE = 40;
  private final GraphicsContext gc;
  // 20 colonnes et 15 lignes max pr ce test
  private final int WIDTH;
  private final int HEIGHT;

  public GameRenderer(GraphicsContext gc, int width, int height) {
    this.gc = gc;
    this.WIDTH = width;
    this.HEIGHT = height;
  }

  public void renderGame(map m, player p, int score, int level, boolean isGameOver, boolean isVictory) {
    // on clear avec un bg noir a chaque frame
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, WIDTH, HEIGHT);
    if (m == null)
      return;
    // edit de l ancienne version
    // on decoupe les responsabiliter a d autre fonction
    drawTiles(m);
    drawGold(m);
    drawEnemies(m);
    drawPlayer(p);
    drawHUD(p, score, level);
    // les ecrans de fin de game
    if (isGameOver) {
      gc.setFill(Color.RED);
      gc.fillText("GAME OVER", WIDTH / 2 - 40, HEIGHT / 2);
    } else if (isVictory) {
      gc.setFill(Color.GREEN);
      gc.fillText("GG NIVEAU FINI", WIDTH / 2 - 40, HEIGHT / 2);
    }
  }

  private void drawTiles(map m) {
    for (int x = 0; x < m.getWidth(); x++) {
      for (int y = 0; y < m.getHeight(); y++) {
        tiletype t = m.getTile(x, y);
        int px = x * TILE_SIZE;
        int py = y * TILE_SIZE;

        if (t == tiletype.INDESTRUCTIBLE_WALL) {
          gc.setFill(Color.DARKGRAY);
          gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        } else if (t == tiletype.DESTRUCTIBLE_WALL) {
          // verif si la brique est detruite ou pas
          brick b = m.getBrickAt(x, y);
          if (b != null && !b.getIsBroken()) {
            gc.setFill(Color.BROWN);
            gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
          }
        } else if (t == tiletype.LADDER) {
          gc.setFill(Color.LIGHTGRAY);
          // petite echelle fine pr le style
          gc.fillRect(px + 10, py, TILE_SIZE - 20, TILE_SIZE);
        } else if (t == tiletype.MONKEY_BAR) {
          gc.setFill(Color.WHITE);
          gc.fillRect(px, py + 10, TILE_SIZE, 10);
        }
      }
    }

  }

  private void drawGold(map m) {
    for (int x = 0; x < m.getWidth(); x++) {
      for (int y = 0; y < m.getHeight(); y++) {
        if (m.getGoldAt(x, y) != null) {
          gc.setFill(Color.YELLOW);
          gc.fillOval(x * TILE_SIZE + 10, y * TILE_SIZE + 10, 20, 20);
        }
      }
    }
  }

  private void drawEnemies(map m) {
    gc.setFill(Color.RED);
    for (enemy e : m.getEnemy()) {
      gc.fillRect(e.getX() * TILE_SIZE, e.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }

  private void drawPlayer(player p) {
    if (p != null) {
      gc.setFill(Color.BLUE);
      gc.fillRect(p.getX() * TILE_SIZE, p.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }

  private void drawHUD(player p, int score, int level) {
    if (p != null)
      return;
    gc.setFill(Color.WHITE);
    gc.fillText("Score: " + score + " | Vies: " + p.getlife(), 10, 20);
  }

}
