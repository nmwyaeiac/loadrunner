package com.loderunner.GAME;

import com.loderunner.ENTITY.*;
import com.loderunner.MAP.map;
import java.util.Arrays;

public class pathfinding {
    
    public character.Direction getNextStep(enemy e, player p, map gameMap) {
        int w = gameMap.getWidth();
        int h = gameMap.getHeight();
        
        //grille distances
        int[][] dist = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                dist[i][j] = 999;
            }
        }

        dist[p.getX()][p.getY()] = 0;//joueur

        for (int k = 0; k < w * h; k++) {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    if (dist[x][y] != 999) {
                        // 4 voisins
                        updateTile(x + 1, y, dist[x][y] + 1, dist, gameMap, x, y);
                        updateTile(x - 1, y, dist[x][y] + 1, dist, gameMap, x, y);
                        updateTile(x, y + 1, dist[x][y] + 1, dist, gameMap, x, y);
                        updateTile(x, y - 1, dist[x][y] + 1, dist, gameMap, x, y);
                    }
                }
            }
        }

        return findBestDirection(e, dist, gameMap);
    }

    private void updateTile(int nx, int ny, int newDist, int[][] dist, map m, int x, int y) {
        if (nx >= 0 && nx < m.getWidth() && ny >= 0 && ny < m.getHeight()) {
            if (!m.isSolid(nx, ny)) {
                boolean isVertical = (nx == x);
                boolean canPass = true;
                
                if (isVertical) {
                    canPass = m.isLadder(nx, ny) || m.isLadder(x, y);//descente si echelle uniquement
                }

                if (canPass && newDist < dist[nx][ny]) {
                    dist[nx][ny] = newDist;
                }
            }
        }
    }

    private character.Direction findBestDirection(enemy e, int[][] dist, map m) {
        int x = e.getX();
        int y = e.getY();
        int min = 999;
        character.Direction best = null;
        
        if (x + 1 < m.getWidth() && dist[x+1][y] < min) {
            min = dist[x+1][y];
            best = character.Direction.RIGHT;
        }
        
        if (x - 1 >= 0 && dist[x-1][y] < min) {
            min = dist[x-1][y];
            best = character.Direction.LEFT;
        }
        
        if (y + 1 < m.getHeight() && dist[x][y+1] < min) {
            if (m.isLadder(x, y) || m.isLadder(x, y+1) || !m.isSolid(x, y+1)) {
                min = dist[x][y+1];
                best = character.Direction.DOWN;
            }
        }
        
        if (y - 1 >= 0 && dist[x][y-1] < min) {
            if (m.isLadder(x, y)) {
                min = dist[x][y-1];
                best = character.Direction.UP;
            }
        }

        return best;
    }
}