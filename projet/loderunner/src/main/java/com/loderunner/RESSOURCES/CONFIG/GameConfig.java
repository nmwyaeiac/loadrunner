package com.loderunner.RESSOURCES.CONFIG;

public class GameConfig {
  private static GameConfig instance;

  public static GameConfig get() {
    if (instance == null)
      instance = new GameConfig();
    return instance;
  }

  private GameConfig() {
  }

  // affichage
  public int TILE_SIZE = 40;
  public int MAP_WIDTH = 20;
  public int MAP_HEIGHT = 15;

  // joueur
  public int PLAYER_LIVES = 3;
  public int PLAYER_CAPACITY = 99;
  public int GOLD_SCORE_VALUE = 100;

  // brique
  public int BRICK_DURANILITY = 3;
  public long BRICK_REGEN_MS = 5000;

  // ia / physic
  public int AI_TICK_INTERVAL = 4;
  public long GAME_TICK_MS = 100;

  // generation procedurale

  public int GEN_MIN_WIDTH = 20;
  public int GEN_MAX_WIDTH = 30;
  public int GEN_MIN_HEIGHT = 14;
  public int GEN_MAX_HEIGHT = 20;

  public int GEN_FLOOR_GAP_MIN = 3;
  public int GEN_FLOOR_GAP_MAX = 5;
  public int GEN_PLATEFORM_MIN_LEN = 3;
  public int GEN_PLATEFORM_MAX_LEN = 8;

  public int GEN_GOLD_PER_LEVEL = 5;
  public int GEN_ENEMY_PER_LEVEL = 1;

  public int GEN_ENEMY_SCALE = 1;
  public int GEN_ENEMY_SCALE_STEP = 3;

  public double GEN_BRICK_DENSITY = 0.0; // brick aleatoire hors plateform

  // reseau

  public int NETWORK_PORT_TCP = 8990;
  public int NETWORK_PORT_UDP = 9000;
  public long NETWORK_TICK_MS = 100;

  // helper
  public int windowWidth() {
    return MAP_WIDTH * TILE_SIZE;
  }

  public int windowHeight() {
    return MAP_HEIGHT * TILE_SIZE;
  }

  public int enemyCountForLevel(int level) {
    return GEN_ENEMY_PER_LEVEL + (level / GEN_ENEMY_SCALE_STEP) * GEN_ENEMY_SCALE;
  }

  public int goldCountForLevel(int level) {
    return GEN_GOLD_PER_LEVEL + level;
  }
}
