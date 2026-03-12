package com.loderunner.ENTITY;

import com.loderunner.ENTITY.WALL.brick;

/**
 * entity_mobile
 */
public abstract class character extends entity {
  protected int capaciter;
  protected int butin;
  protected Direction direction;

  public enum Direction {
    LEFT, RIGHT
  }

  public character(int x, int y, int capaciter) {
    super(x, y);
    this.setCapaciter(capaciter);
    this.setButin(0);
  }

  character(int x, int y) {
    this(x, y, 1);
  }

  public void setCapaciter(int capaciter) {
    this.capaciter = capaciter;
  }

  public int getCapaciter() {
    return this.capaciter;
  }

  public void setButin(int butin) {
    this.butin = butin;
  }

  public int getButin() {
    return this.butin;
  }

  public void setDirection(Direction d) {
    this.direction = d;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public boolean canMove(game_object other) {
    return (!(this.Colide(other) && other.getIsSolid()));
  }

  public void right() {
    this.setX(this.getX() + 1);
    this.setDirection(Direction.RIGHT);
  }

  public void left() {
    this.setX(this.getX() - 1);
    this.setDirection(Direction.LEFT);
  }

  public void up() {
    this.setY(this.getY() - 1);
  }

  public void down() {
    this.setY(this.getY() + 1);
  }

  public void takeGold(gold g) {
    if (this.getButin() < this.getCapaciter()) {
      this.setButin(this.getButin() + g.getValue());
      g.collected();
    }
  }

  public void dropGold() {
    if (this.getButin() > 0) {
      this.setButin(this.getButin() - 1);
    }
  }

  public boolean dig(game_object g) {
    if (g instanceof brick) {
      brick b = (brick) g;
      if (b.canDigg()) {
        b.hit();
        return true;
      }
    }
    return false;
  }

  public boolean canMoveTo(game_object target) {
    if (target == null)
      return true;
    if (target instanceof brick) {
      brick b = (brick) target;
      return !b.getIsSolid();
    }
    return !target.getIsSolid();
  }
}
