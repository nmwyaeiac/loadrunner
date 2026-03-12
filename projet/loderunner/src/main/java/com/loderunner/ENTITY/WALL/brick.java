package com.loderunner.ENTITY.WALL;

/**
 * brique cassable
 */
public class brick extends wall {
  private int durability;
  private boolean isBroken = false;
  private long breakTime = 0;
  final long REGEN_DELAY = 5000;

  brick(int x, int y, int durability) {
    super(x, y);
    this.setDurability(durability);
    this.setIsSolid(true);
    this.setIsCollectable(false);

  }

  brick(int x, int y) {
    this(x, y, 3);
  }

  public long getBreakTime() {
    return this.breakTime;
  }

  public void setIsBroken(boolean isBroken) {
    this.isBroken = isBroken;
  }

  public boolean getIsBroken() {
    return this.isBroken;
  }

  public void setDurability(int durability) {
    this.durability = durability;
  }

  public int getDurability() {
    return durability;
  }

  @Override
  public boolean canDigg() {
    return !isBroken;
  }

  public void hit() {
    if (!isBroken) {
      this.setDurability(this.getDurability() - 1);
    }
    if (this.getDurability() <= 0) {
      breakBrick();
    }
  }

  private void breakBrick() {
    this.setIsBroken(true);
    this.setIsSolid(false);
    breakTime = System.currentTimeMillis();
    System.out.println("brique casser , reapparait dans 30 secondes");
  }

  public void update() {
    if (this.getIsBroken() && (System.currentTimeMillis() - this.getBreakTime()) >= REGEN_DELAY) {
      regenerate();
    }
  }

  public long getTimeUntilRegen() {
    if (!isBroken)
      return 0;
    long e = System.currentTimeMillis() - breakTime;
    return Math.max(0, REGEN_DELAY - e);
  }

  private void regenerate() {
    this.setIsBroken(false);
    this.setDurability(3);
    this.setIsSolid(true);
    System.out.println("brique casser reaparait");
  }

  public String toString() {
    if (this.getIsBroken()) {
      long remaining = getTimeUntilRegen() / 1000;
      return remaining > 0 ? " " : "#";
    }
    return "#";
  }
}
