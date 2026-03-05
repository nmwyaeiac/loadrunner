package com.loderunner.ENTITY;

abstract public class entity {
  protected int x, y;

  entity(int x, int y) {
    setX(x);
    setX(y);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public boolean Colide(entity other) {
    return this.getX() == other.getX() && this.getY() == other.getY();
  }
}
