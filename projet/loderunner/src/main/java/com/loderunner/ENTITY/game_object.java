package com.loderunner.ENTITY;

abstract public class game_object extends entity {
  protected boolean isSolid;
  protected boolean isCollectable;

  public game_object(int x, int y) {
    super(x, y);
  }

  public void setIsSolid(boolean b) {
    this.isSolid = b;
  }

  public void setIsCollectable(boolean b) {
    this.isCollectable = b;
  }

  public boolean getIsSolid() {
    return this.isSolid;
  }

  public boolean getisCollectable() {
    return this.isCollectable;
  }
}
