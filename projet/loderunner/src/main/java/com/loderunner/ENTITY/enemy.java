package com.loderunner.ENTITY;

import com.loderunner.ENTITY.WALL.*;

/*
 * enemie - piloter par une ia,capa li;iter a 1 gold
 */
public class enemy extends character {
  private boolean isUnderGround;

  public enemy(int x, int y) {
    super(x, y);
    this.setCapaciter(1);
    this.setIsUnderGround(false);

  }

  public void setIsUnderGround(boolean b) {
    this.isUnderGround = b;
  }

  public boolean getIsUnderGround() {
    return this.isUnderGround;
  }

  public boolean isinbrokebreak(game_object b) {
    if (b instanceof brick) {
      if (!b.getIsSolid()) {
        this.setIsUnderGround(true);
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "E";
  }
}
