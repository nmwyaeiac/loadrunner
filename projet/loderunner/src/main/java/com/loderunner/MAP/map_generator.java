package com.loderunner.MAP;

import com.loderunner.RESSOURCES.CONFIG.*;
import com.loderunner.ENTITY.*;
import com.loderunner.ENTITY.WALL.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class map_generator {
  private final Random rng;
  private final GameConfig cfg;

  public map_generator() {
    this.rng = new Random();
    this.cfg = GameConfig.get();
  }

  public map_generator(long seed) {
    this.rng = new Random(seed);
    this.cfg = GameConfig.get();
  }

  // a changer surement c qu une idee : map s agrandis par nv et + d enemie et or
  // pour plus de difficulter
  public map generate(int level) {
    int width = randBetween(cfg.GEN_MIN_WIDTH, cfg.GEN_MAX_WIDTH);
    int height = randBetween(cfg.GEN_MIN_HEIGHT, cfg.GEN_MAX_HEIGHT);
    map m = new map(width, height);
    placeBorder(m, width, height);
    List<int[]> platforms = generatePlateforms(m, width, height, level);
    placeGold(m, platforms, level);
    List<int[]> enemySpots = placeEnemies(m, platforms, level);
    placePlayer(m, platforms);
    return m;
  }

  private void placeBorder(map m, int width, int height) {
    for (int x = 0; x < width; x++) {
      m.addBedrock(new bedrock(x, height - 1));
    }
    for (int y = 0; y < height; y++) {
      m.addBedrock(new bedrock(0, y));
      m.addBedrock(new bedrock(width - 1, y));
    }
  }

  private List<int[]> generatePlateforms(map m, int width, int height, int level) {
    List<int[]> plateform = new ArrayList<>();

    int[] solPlatform = { 1, width - 2, height - 1 };
    plateform.add(solPlatform);
    int y = height - 1;
    int[] prevPlatform = solPlatform;
    while (y > 2) {
      int gap = randBetween(cfg.GEN_FLOOR_GAP_MIN, cfg.GEN_FLOOR_GAP_MAX);
      y -= gap;
      if (y <= 1)
        break;// jsp comment faire sans pour l instant a adapter plus tard
      int maxLen = Math.max(cfg.GEN_FLOOR_GAP_MIN, cfg.GEN_FLOOR_GAP_MAX);
      int len = randBetween(cfg.GEN_PLATEFORM_MIN_LEN, maxLen);
      int cx = randBetween(1 + len / 2, width - 2 - len / 2);
      int[] bounds = placePlatform(m, cx, y, len);
      plateform.add(new int[] { bounds[0], bounds[1], y });
      int ladderX = randBetween(bounds[0], bounds[1]);
      placeLadder(m, ladderX, y, prevPlatform[2]);
      prevPlatform = new int[] { bounds[0], bounds[1], y };
    }
    int exitX = randBetween(1, width - 2);
    placeLadder(m, exitX, 0, prevPlatform[2]);
    return plateform;
  }

  private int[] placePlatform(map m, int cx, int y, int len) {
    int left = cx;
    int right = cx;
    m.addBrick(new brick(cx, y));
    for (int i = 1; i <= len; i++) {
      if ((i % 2) == 0) {
        if (left - 1 > 0) {
          left--;
          m.addBrick(new brick(left, y));
        } else {
          if (right + 1 < m.getWidth() - 1) {
            right++;
            m.addBrick(new brick(right, y));
          }
        }
      }
    }
    return new int[] { left, right };
  }

  private void placeLadder(map m, int x, int y, int yBottom) {
    for (int ly = y; ly < yBottom; ly++) {
      if (!m.isSolid(x, ly)) {
        m.addLadders(new ladder(x, ly));
      }
    }
  }

  private void placeGold(map m, List<int[]> platforms, int level) {
    int count = cfg.goldCountForLevel(level);
    List<int[]> candidates = getFreeSpots(m, platforms);
    List<int[]> placed = new ArrayList<>();
    for (int i = 0; i < count && !candidates.isEmpty(); i++) {
      int idx = rng.nextInt(candidates.size());
      int[] spot = candidates.remove(idx);
      m.addGold(new gold(spot[0], spot[1], 1));
    }
  }

  private List<int[]> placeEnemies(map m, List<int[]> platforms, int level) {
    int count = cfg.enemyCountForLevel(level);
    List<int[]> candidates = getFreeSpots(m, platforms);
    List<int[]> placed = new ArrayList<>();

    for (int i = 0; i < count && !candidates.isEmpty(); i++) {
      int idx = rng.nextInt(candidates.size());
      int[] spot = candidates.remove(idx);
      m.addenemies(new enemy(spot[0], spot[1]));
      placed.add(spot);
    }
    return placed;
  }

  private void placePlayer(map m, List<int[]> platforms) {
    int[] topPlatform = null;
    int minY = Integer.MAX_VALUE;
    for (int i = 1; i < platforms.size(); i++) {
      int[] p = platforms.get(i);
      if (p[2] < minY) {
        minY = p[2];
        topPlatform = p;
      }
    }
    if (topPlatform == null)
      topPlatform = platforms.get(0);
    int px = topPlatform[0] + 1;
    int py = topPlatform[2] - 1;
    m.setPlayer(new player(px, py));

  }

  private List<int[]> getFreeSpots(map m, List<int[]> platforms) {
    List<int[]> spots = new ArrayList<>();
    for (int[] p : platforms) {
      int xL = p[0], xR = p[1], y = p[2];
      for (int x = xL; x <= xR; x++) {
        int above = y - 1;
        if (above >= 0 && !m.isSolid(x, above) && !m.isLadder(x, above)) {
          spots.add(new int[] { x, above });
        }
      }
    }
    return spots;
  }

  private int randBetween(int min, int max) {
    if (min >= max)
      return min;
    return min + rng.nextInt(max - min + 1);
  }
}
