package com.loderunner.MAP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.loderunner.ENTITY.WALL.bedrock;
import com.loderunner.ENTITY.WALL.brick;
import com.loderunner.ENTITY.enemy;
import com.loderunner.ENTITY.gold;
import com.loderunner.ENTITY.ladder;
import com.loderunner.ENTITY.player;
import com.loderunner.RESSOURCES.CONFIG.GameConfig;

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

    public map generate(int level) {
        int width = 30; 
        int height = 20; 
        map m = new map(width, height);
        placeBorder(m, width, height);
        List<int[]> platforms = generateStructure(m, width, height);
        for (int i = platforms.size() - 1; i > 0; i--) {
            int[] current = platforms.get(i);
            int[] below = platforms.get(i - 1);
            int ladderX = randBetween(current[0], current[1]);
            drawLadderBetween(m, ladderX, current[2], below[2]);
        }

        placeGold(m, platforms, level);
        placeEnemies(m, platforms, level);
        placePlayer(m, platforms);

        return m;
    }

    private void placeBorder(map m, int width, int height) {
        for (int x = 0; x < width; x++) {
            m.addBedrock(new bedrock(x, 0));
            m.addBedrock(new bedrock(x, height - 1));
        }
        for (int y = 1; y < height - 1; y++) {
            m.addBedrock(new bedrock(0, y));
            m.addBedrock(new bedrock(width - 1, y));
        }
    }

    private List<int[]> generateStructure(map m, int width, int height) {
    List<int[]> platforms = new ArrayList<>();
    platforms.add(new int[]{1, width - 2, height - 1});
    int y = height - 2;
    while (y > 4) {
        y -= 3;
        int len = randBetween(6, 12);
        int cx = randBetween(2 + len / 2, width - 3 - len / 2);
        int[] bounds = placePlatform(m, cx, y, len);
        platforms.add(new int[]{bounds[0], bounds[1], y});
    }
    return platforms;
}
    private int[] placePlatform(map m, int cx, int y, int len) {
        int left = cx - (len / 2);
        int right = cx + (len / 2);
        for (int x = left; x <= right; x++) {
            if (x > 0 && x < m.getWidth() - 1) {
                m.addBrick(new brick(x, y));
            }
        }
        return new int[]{left, right};
    }

   private void drawLadderBetween(map m, int x, int yUpper, int yLower) {
    if (m.getBrickAt(x, yUpper) != null) {
        m.removeBrick(x, yUpper);
        m.addLadders(new ladder(x, yUpper));
    }
    int y = yUpper + 1;
    while (y < m.getHeight() - 1) {
        boolean solidBelow = m.isSolid(x, y + 1) || m.getBrickAt(x, y + 1) != null;
        if (m.getBrickAt(x, y) != null) {
            m.removeBrick(x, y);
            m.addLadders(new ladder(x, y));
            break;
        }
        if (m.isSolid(x, y)) {
            break;
        }
        m.addLadders(new ladder(x, y));
        if (solidBelow) {
            break;
        }
        y++;
    }
}

    private void placeGold(map m, List<int[]> platforms, int level) {
        int count = cfg.goldCountForLevel(level);
        List<int[]> spots = getFreeSpots(m, platforms);
        for (int i = 0; i < count && !spots.isEmpty(); i++) {
            int[] spot = spots.remove(rng.nextInt(spots.size()));
            m.addGold(new gold(spot[0], spot[1], 1));
        }
    }

    private void placeEnemies(map m, List<int[]> platforms, int level) {
        int count = cfg.enemyCountForLevel(level);
        List<int[]> spots = getFreeSpots(m, platforms);
        for (int i = 0; i < count && !spots.isEmpty(); i++) {
            int[] spot = spots.remove(rng.nextInt(spots.size()));
            m.addenemies(new enemy(spot[0], spot[1]));
        }
    }

    private void placePlayer(map m, List<int[]> platforms) {
        int[] top = platforms.get(platforms.size() - 1);
        m.setPlayer(new player(top[0], top[2] - 1));
    }

    private List<int[]> getFreeSpots(map m, List<int[]> platforms) {
    List<int[]> spots = new ArrayList<>();
    for (int[] p : platforms) {
        for (int x = p[0]; x <= p[1]; x++) {
            int above = p[2] - 1;
            if (above > 0 
                && !m.isSolid(x, above) 
                && !m.isLadder(x, above)
                && m.isSolid(x, p[2])) {  // vérif qu'il y a bien un sol dessous
                spots.add(new int[]{x, above});
            }
        }
    }
    return spots;
}
    private int randBetween(int min, int max) {
        if (min >= max) return min;
        return min + rng.nextInt(max - min + 1);
    }
}
