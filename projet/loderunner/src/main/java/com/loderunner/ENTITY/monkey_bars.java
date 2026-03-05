package com.loderunner.ENTITY;

/**
 * monkey_bars
 */
public class monkey_bars extends game_object {
  monkey_bars(int x, int y) {
    super(x, y);
    this.setIsCollectable(false);
    this.setIsSolid(false);
  }

  public boolean canMoveHorizontaly() {
    return true;
  }

  public boolean canFall() {
    return false;
  }

  @Override
  public String toString() {
    return "-";
  }
}
