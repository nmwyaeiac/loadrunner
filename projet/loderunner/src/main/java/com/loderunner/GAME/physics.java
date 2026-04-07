package com.loderunner.GAME;

import com.loderunner.ENTITY.*;
import com.loderunner.ENTITY.WALL.brick;
import com.loderunner.MAP.map;

public class physics {
    private map GameMap;

    public physics(map GameMap) {
        this.setGameMap(GameMap);
    }

    public void setGameMap(map GameMap) {
        this.GameMap = GameMap;
    }

    public void applyGravity(character c) {
        int x = c.getX();
        int y = c.getY();
        int below = y + 1;

        if (GameMap.isLadder(x, y)) return;
        if (GameMap.isMonkeyBars(x, y)) return;

        if (below < GameMap.getHeight()) {
            if (!GameMap.isSolid(x, below) && !GameMap.isLadder(x, below)) {
                c.down();
            }
        }
    }

    public boolean moveLeft(character c) {
        int nx = c.getX() - 1;
        if (nx < 0 || GameMap.isSolid(nx, c.getY())) return false;
        c.left();
        return true;
    }

    public boolean moveRight(character c) {
        int nx = c.getX() + 1;
        if (nx >= GameMap.getWidth() || GameMap.isSolid(nx, c.getY())) return false;
        c.right();
        return true;
    }

    public boolean moveUP(character c) {
        if (!GameMap.isLadder(c.getX(), c.getY())) return false;
        int ny = c.getY() - 1;
        if (ny < 0 || GameMap.isSolid(c.getX(), ny)) return false;
        c.up();
        return true;
    }

    public boolean moveDown(character c) {
        int ny = c.getY() + 1; // FIX : c'est +1 pour descendre, pas -1 !
        if (ny >= GameMap.getHeight() || GameMap.isSolid(c.getX(), ny)) return false;

        if (GameMap.isLadder(c.getX(), c.getY()) || GameMap.isLadder(c.getX(), ny)) {
            c.down();
            return true;
        }
        return false;
    }

    public boolean digLeft(character c) { return digAt(c, c.getX() - 1, c.getY() + 1); }
    public boolean digRight(character c) { return digAt(c, c.getX() + 1, c.getY() + 1); }

    public boolean digAt(character c, int tx, int ty) {
        if (tx < 0 || tx >= GameMap.getWidth() || ty < 0 || ty >= GameMap.getHeight()) return false;
        brick target = GameMap.getBrickAt(tx, ty);
        if (target != null && target.canDigg()) {
            c.dig(target);
            return true;
        }
        return false;
    }

    public boolean playerColidEnemy(player p) {
        for (enemy e : GameMap.getEnemy()) {
            if (p.getX() == e.getX() && p.getY() == e.getY()) return true;
        }
        return false;
    }

    public boolean playerCollectsGold(player p) {
        gold g = GameMap.getGoldAt(p.getX(), p.getY());
        if (g != null) {
            p.takeGold(g);
            GameMap.removeGold(g);
            return true;
        }
        return false;
    }
}