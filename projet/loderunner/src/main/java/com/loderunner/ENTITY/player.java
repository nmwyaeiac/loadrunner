package com.loderunner.ENTITY;

public class player extends character {
  private int life;
  private int butin;

  player(int x, int y, int capaciter, int life) {
    super(x, y, capaciter);
    this.setLife(life);
  }

  public void setLife(int life) {
    this.life = life;
  }

  public int getlife() {
    return this.life;
  }

}
