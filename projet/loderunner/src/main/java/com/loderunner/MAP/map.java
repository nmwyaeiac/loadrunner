package com.loderunner.MAP;

import com.loderunner.ENTITY.*;
import com.loderunner.ENTITY.WALL.*;

import java.util.ArrayList;
import java.util.List;

/**
 * map
 */
public class map {
  private tiletype[][] grid;
  private int width, height;

  private player player;
  private List<enemy> enemies;
  private List<gold> golds;
  private List<ladder> ladders;
  private List<monkey_bars> monkeys_bars;
  private List<brick> bricks;
  private List<bedrock> bedrocks;

  public map(int width, int height) {
    this.setWidth(width);
    this.setHeight(height);
    this.grid = new tiletype[width][height];
    setMapEmpty(width, height);

    this.enemies = new ArrayList<>();
    this.golds = new ArrayList<>();
    this.monkeys_bars = new ArrayList<>();
    this.bricks = new ArrayList<>();
    this.ladders = new ArrayList<>();
    this.bedrocks = new ArrayList<>();

  }

  public void setPlayer(player p) {
    this.player = p;
  }

  public player getPlayer() {
    return this.player;
  }

  public void addenemies(enemy e) {
    enemies.add(e);
  }

  public List<enemy> getEnemy() {
    return enemies;
  }

  public brick getBrickAt(int x, int y) {
    for (brick b : bricks) {
      if (b.getX() == x && b.getY() == y) {
        return b;
      }
    }
    return null;
  }

  public void addGold(gold g) {
    golds.add(g);
    grid[g.getX()][g.getY()] = tiletype.GOLD;
  }

  public void removeGold(gold g) {
    golds.remove(g);
  }

  public gold getGoldAt(int x, int y) {
    for (gold g : golds)
      if (g.getX() == x && g.getY() == y && g.getisCollectable())
        return g;
    return null;

  }

  public void addMonkeysBars(monkey_bars m) {
    monkeys_bars.add(m);
    grid[m.getX()][m.getY()] = tiletype.MONKEY_BAR;
  }

  public void addLadders(ladder l) {
    ladders.add(l);
    grid[l.getX()][l.getY()] = tiletype.LADDER;
  }

  public void addBrick(brick b) {
    bricks.add(b);
    grid[b.getX()][b.getY()] = tiletype.DESTRUCTIBLE_WALL;
  }

  public void addBedrock(bedrock b) {
    bedrocks.add(b);
    grid[b.getX()][b.getY()] = tiletype.INDESTRUCTIBLE_WALL;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setMapEmpty(int width, int height) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        grid[x][y] = tiletype.EMPTY;
      }
    }
  }

  public void setTile(int x, int y, tiletype t) {
    if (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight()) {
      grid[x][y] = t;
    }
  }

  public tiletype getTile(int x, int y) {
    if (x >= 0 && x <= this.getWidth() && y >= 0 && y <= this.getHeight()) {
      return grid[x][y];
    } else
      return tiletype.INDESTRUCTIBLE_WALL;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public boolean isSolid(int x, int y) {
    for (brick b : bricks)
      if (b.getX() == x && b.getY() == y && b.getIsSolid())
        return true;
    for (bedrock bd : bedrocks)
      if (bd.getX() == x && bd.getY() == y)
        return true;
    return false;
  }

  public boolean isLadder(int x, int y) {
    for (ladder l : ladders)
      if (l.getX() == x && l.getY() == y)
        return true;
    return false;
  }

  public boolean isMonkeyBars(int x, int y) {
    for (monkey_bars m : monkeys_bars)
      if (m.getX() == x && m.getY() == y)
        return true;
    return false;
  }

  public String getDisplayChar(int x, int y) {
    if (player != null && player.getX() == x && player.getY() == y) {
      return player.toString();
    }
    gold g = getGoldAt(x, y);
    if (g != null) {
      return g.toString();
    }
    for (enemy e : enemies)
      if (e.getX() == x && e.getY() == y)
        return e.toString();
    for (ladder l : ladders)
      if (l.getX() == x && l.getY() == y)
        return l.toString();
    for (monkey_bars m : monkeys_bars)
      if (m.getX() == x && m.getY() == y)
        return m.toString();
    for (brick b : bricks)
      if (b.getX() == x && b.getY() == y)
        return b.toString();
    for (bedrock b : bedrocks)
      if (b.getX() == x && b.getY() == y)
        return b.toString();
    return " ";

  }

  public void update() {
    for (brick b : bricks)
      b.update();
    golds.removeIf(gold::shouldBeRemoved);
  }

  public boolean isLevelCompleted() {
    return golds.isEmpty();
  }
}
