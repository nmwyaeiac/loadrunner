package com.loderunner.ENTITY;

import com.loderunner.ENTITY.WALL.*;

/**
 * enemy implementer l ia pour controler les enemies pour qu il tqrget le joueur
 * il ne peuve prendre qu un seul
 * gold et pour le recuperer il doit etre dans un enseveli
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
      if (b.getIsSolid() == false) {
        this.setIsUnderGround(true);
        return true;
      }
    }
    return false;
  }

}
