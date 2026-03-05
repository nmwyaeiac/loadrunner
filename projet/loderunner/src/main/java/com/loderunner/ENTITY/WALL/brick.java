package com.loderunner.ENTITY.WALL;

/**
 * brique cassable
 */
public class brick extends wall {

  brick(int x, int y) {
    super(x, y);
  }

  @Override
  public boolean canDigg() {
    return true;
  }

  public void startDigg() {
    // a faire
  }

  @Override
  public String toString() {
    return "#";
  }

}
