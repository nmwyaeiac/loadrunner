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

  @Override
  public String toString() {
    return "-";
  }
}
