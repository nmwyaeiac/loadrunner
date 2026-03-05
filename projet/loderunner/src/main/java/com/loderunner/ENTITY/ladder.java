package com.loderunner.ENTITY;

/**
 * ladder
 */
public class ladder extends game_object {
  ladder(int x, int y) {
    super(x, y);

    this.setIsSolid(false);
    this.setIsCollectable(false);
  }

  @Override
  public String toString() {
    return "H";
  }
}
