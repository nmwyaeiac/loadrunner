package com.loderunner.GAME;

import com.loderunner.INPUT.inputhandler;
import com.loderunner.INPUT.inputhandler.Action;
import com.loderunner.MAP.map;
import com.loderunner.MAP.map_loader;
import com.loderunner.ENTITY.player;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * game
 * un thread pour lire les entrer sortie
 * on va utiliser une blocking list
 */
public class game {
  private static final long TICK_MS = 100;

  private map gameMap;
  private player gamePlayer;
  private physics physics;
  private renderer renderer;

  private int score = 0;
  private int level = 1;
  private boolean running = false;
  private boolean gameOver = false;
  private boolean victory = false;

  private final LinkedBlockingQueue<Action> inputQueue = new LinkedBlockingQueue<>();

  public game() {
  }

  private void loadlevel(int levelNumber) throws IOException {
    String p = "levels/level" + levelNumber + ".txt";
    gameMap = map_loader.LoadFromFile(p);
    gamePlayer = gameMap.getPlayer();
    if (gamePlayer == null)
      throw new IOException("pas de joueur dans le niveau");
    physics = new physics(gameMap);
    gameOver = false;
    victory = false;

  }

  public void update() {
    if (!running || gameOver || victory)
      return;
    physics.applyGravity(gamePlayer);
    if (physics.playerCollectsGold(gamePlayer))
      score += 100;
    if (physics.playerColidEnemy(gamePlayer)) {
      gamePlayer.LoseLife();
      if (!gamePlayer.isAlive()) {
        gameOver = true;
        running = false;
        return;
      }
      respawnPlayer();
    }
    gameMap.update();
    if (gameMap.isLevelCompleted()) {
      level++;
      try {
        loadlevel(level);
      } catch (IOException e) {
        victory = true;
        running = false;
      }
    }
  }

  public void start() throws IOException {
    loadlevel(level);
    running = true;
  }

  public void actionMoveUp() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  public void actionMoveDown() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  public void actionMoveLeft() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  public void actionMoveRight() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  public void actionDigLeft() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  public void actionDigRight() {
    if (running)
      inputQueue.offer(Action.MOVE_UP);
  }

  private void processInput() {
    Action action = inputQueue.poll();
    if (action == null)
      return;
    switch (action) {
      case MOVE_LEFT:
        physics.moveLeft(gamePlayer);
        break;
      case MOVE_RIGHT:
        physics.moveRight(gamePlayer);
        break;
      case MOVE_UP:
        physics.moveUP(gamePlayer);
        break;
      case MOVE_DOWN:
        physics.moveDown(gamePlayer);
        break;
      case DIG_LEFT:
        physics.digLeft(gamePlayer);
        break;
      case DIG_RIGHT:
        physics.digRight(gamePlayer);
        break;
      case QUIT:
        running = false;
        break;
    }
  }

  private void respawnPlayer() {
    if (!gamePlayer.isAlive())
      return;
    try {
      int lives = gamePlayer.getlife();
      loadlevel(level);
      gamePlayer.setLife(lives);
    } catch (IOException e) {
      running = false;
    }
  }

  // getter
  public map getGameMap() {
    return gameMap;
  }

  public player getGamePlayer() {
    return gamePlayer;
  }

  public int getScore() {
    return score;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isVictory() {
    return gameOver;
  }
}
