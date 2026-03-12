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

  private final LinkedBlockingQueue<Action> inputQueue = new LinkedBlockingQueue<>();
  private inputhandler inputhandler;
  private Thread inputThread;

  public game() {
  }

  private void loadlevel(int levelNumber) throws IOException {
    String p = "levels/level" + levelNumber + ".txt";
    gameMap = map_loader.LoadFromFile(p);
    gamePlayer = gameMap.getPlayer();
    if (gamePlayer == null)
      throw new IOException("pas de joueur dans le niveau");
    physics = new physics(gameMap);
    renderer = new renderer(gameMap);
  }

  public void start() throws IOException {
    loadlevel(level);
    startInputThread();
    running = true;
    loop();
  }

private void  loop(){
  while(running){
    long tickStart= System.currentTimeMillis();
    processInput();
    physics.applyGravity(gamePlayer);
    if(physics.playerCollectsGold(gamePlayer))score+=100;
    if(physics.playerTouchEnemy(gamePlayer)){
      gamePlayer.LoseLife();
      respawnPlayer();
    }
    gameMap.update();
    if(gameMap.isLevelCompleted()){
      rederer.rendererVictory(score,level);
      level++;
      try{
        thread.sleep(2000);
        loadLevel(level);
      }catch(IOException e){System.out.println("plus de niveau dispo. fin de jeux");
      running=false;
      }catch(InterruptedException e){
        Thread.currentThread().interrupt();
      }
    }
    if(!gamePlayer.isAlive()){
      renderer.renderGameOver(score);
      running =false;
    }
    renderer.render(gamePlayer,score,level);
    //maitenir 10 fps
    long e =System.currentTimeMillis()-tickStart;
    long sleep = TICK_MS-e;
    if(sleep>0){try{
      Thread.sleep(sleep);

    catch(InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }
}
  stopInputThread();
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
        physics.moveUp(gamePlayer);
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
      default:
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

  private void startInputThread() {
    inputhandler = new inputhandler(inputQueue);
    inputThread = new Thread(inputhandler, "input-thread");
    inputThread.start();
  }

  private void stopInputThread() {
    if (inputhandler != null)
      inputhandler.stop();
    if (inputThread != null)
      inputThread.interrupt();
  }
}
