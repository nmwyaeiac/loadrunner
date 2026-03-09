package com.loderunner.ENTITY;

import com.loderunner.ENTITY.WALL.brick;

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

  public boolean canMoveTo(game_object target) {
    if (target == null)
      return true;
    if (target instanceof brick) {
      brick b = (brick) target;
      return b.getIsSolid();
    }
    return !target.getIsSolid();
  }
}
