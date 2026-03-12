package com.loderunner.INPUT;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * InputHandler
 */
public class inputhandler implements Runnable {
  public enum Action {
    MOVE_LEFT,
    MOVE_RIGHT,
    MOVE_UP,
    MOVE_DOWN,
    DIG_LEFT,
    DIG_RIGHT,
    QUIT,
    UNKMOW
  }

  private LinkedBlockingDeque<Action> queue;
  private volatile boolean running = true;

  public inputhandler(LinkedBlockingDeque<Action> queue) {
    this.setQueue(queue);
  }

  public void setQueue(LinkedBlockingDeque<Action> queue) {
    this.queue = queue;
  }

  public void stop() {
    this.running = false;
  }

  @Override
  public void run() {
    while (running) {
    }
  }

  private Action mapKeytoAction(int key) throws IOException {
    // 27= esc -> pour fleche
    if (key == 27) {
      int next = System.in.read();
      if (next == '[') {
        int arrow = System.in.read();
        switch (arrow) {
          case 'A':
            return Action.MOVE_UP;
          case 'B':
            return Action.MOVE_DOWN;
          case 'C':
            return Action.MOVE_RIGHT;
          case 'D':
            return Action.MOVE_LEFT;
        }
      }
      return Action.QUIT;

    }
    switch (Character.toLowerCase((char) key)) {
      // zqsd (azerty)
      case 'z':
        return Action.MOVE_UP;
      case 's':
        return Action.MOVE_DOWN;
      case 'q':
        return Action.MOVE_LEFT;
      case 'd':
        return Action.MOVE_RIGHT;
      // creuser
      case 'a':
        return Action.DIG_LEFT;
      case 'e':
        return Action.DIG_RIGHT;
      // a changer pour esc ou ouvrir un menu qui gere sa pour quitter le jeux
      case 'x':
        return Action.QUIT;
      default:
        return Action.UNKMOW;
    }
  }
  // a voir pour gerer le raw mod en java
}
