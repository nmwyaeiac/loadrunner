package com.loderunner.ENTITY;
import java.io.Serializable;

abstract public class entity implements Serializable {
  protected int x, y;

  entity(int x, int y) {
    setX(x);
    setY(y);
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
