package com.loderunner.GAME;

import com.loderunner.MAP.map;
import com.loderunner.ENTITY.player;

/**
 * renderer
 */
public class renderer {
  // seq ansi pour posiiton le curseur en (0,0), sans effacer
  private static final String CURSOR_HOME = "\033[H";
  // seq ansi pour tout effacer
  private static final String CLEAR_SCREEN = "\033[2J";
  private map GameMap;
  private boolean firstFrame = true;

  public renderer(map GameMap) {
    this.setGameMap(GameMap);
  }

  public void setGameMap(map GameMap) {
    this.GameMap = GameMap;
  }

  public void render(player p, int score, int level) {
    StringBuilder frame = new StringBuilder();
    if (firstFrame) {
      frame.append(CLEAR_SCREEN);
      firstFrame = false;
    }
    frame.append(CURSOR_HOME);

    frame.append(String.format(
        " Niveau: %-3d Vies: %-3d Or: %-5d Score: %d%n",
        level, p.getlife(), p.getButin(), score));
    frame.append(" zqsd pour deplacement a pour creuser a gauche e pour creuser a droite x pour quitter \n");
    frame.append(" ");
    for (int x = 0; x <= GameMap.getWidth(); x++)
      frame.append('-');
    frame.append('\n');
    for (int y = 0; y < GameMap.getHeight(); y++) {
      frame.append(" ");
      for (int x = 0; x < GameMap.getWidth(); x++) {
        frame.append(GameMap.getDisplayChar(x, y));
      }
      frame.append('\n');
    }
    frame.append(" ");
    for (int x = 0; x <= GameMap.getWidth(); x++)
      frame.append('-');

    System.out.println(frame);
  }

  public void renderGameOver(int score) {
    System.out.println("\n\n Game over");
    System.out.println("score final " + score);
    System.out.println("appuywe sur une touche pour quitter");
  }

  public void renderVictory(int score, int level) {
    System.out.println("\n\n niveau " + level + "completer");
    System.out.println("score " + score);
    System.out.println("passage au niveau superieur");
  }
}
