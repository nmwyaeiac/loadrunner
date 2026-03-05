package com.loderunner.ENTITY.WALL;

import com.loderunner.ENTITY.game_object;

/**
 * c'est une classe abstraite qui herite de game object
 * et qui sert a construire des blocs destructible et non destrctible
 */
abstract class wall extends game_object {

  wall(int x, int y) {
    super(x, y);
  }

  public abstract boolean canDigg();

}
