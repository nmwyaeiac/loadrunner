package com.loderunner.ENTITY.WALL;

/**
 * brique incassable
 *
 */
public class bedrock extends wall {
  bedrock(int x, int y) {
    super(x, y);

    this.setIsSolid(true);
  }

  @Override
  public boolean canDigg() {
    return false;
  }

  @Override
  public String toString() {
    return "+";
  }
}
