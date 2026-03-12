package com.loderunner.ENTITY;

/**
 * ladder
 */
public class ladder extends game_object {
  public ladder(int x, int y) {
    super(x, y);

    this.setIsSolid(false);
    this.setIsCollectable(false);
  }

  public boolean canClimbUp(character c) {
    return true;
  }

  public boolean canClimbdown(character c) {
    return true;
  }

  @Override
  public String toString() {
    return "H";
  }
}
