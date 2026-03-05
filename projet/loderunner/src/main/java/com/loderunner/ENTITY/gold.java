package com.loderunner.ENTITY;

public class gold extends game_object {
  private int value;
  private boolean collected = false;

  gold(int x, int y, int value) {
    super(x, y);
    this.setIsCollectable(true);
    this.setIsSolid(false);

    setValue(value);
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public void collected() {
    this.collected = true;
  }

  boolean isCollected() {
    return collected;
  }

  @Override
  public String toString() {
    return "g";
  }
}
