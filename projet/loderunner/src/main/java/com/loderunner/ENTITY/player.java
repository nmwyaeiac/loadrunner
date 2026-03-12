package com.loderunner.ENTITY;

public class player extends character {
  private int life;

  public player(int x, int y, int capaciter, int life) {
    super(x, y, capaciter);
    this.setLife(life);
  }

  public player(int x, int y) {
    this(x, y, 99, 3);
  }

  public void setLife(int life) {
    this.life = life;
  }

  public int getlife() {
    return this.life;
  }

  public void LoseLife() {
    this.setLife(this.getlife() - 1);
  }

  public boolean isAlive() {
    return this.life > 0;
  }

  @Override
  public String toString() {
    return "@";
  }

}
