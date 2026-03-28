package com.loderunner.UI;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * SpriteLoader
 */
public class SpriteLoader {
  private final Map<String, Image> sprites = new HashMap<>();
  private static final String[] SPRITE_NAMES = { "player", "enemy", "gold", "brick", "bederock", "ladder",
      "monkey_bar" };

  public SpriteLoader() {
    load();
  }

  public Image get(String name) {
    return sprites.get(name);
  }

  public boolean has(String name) {
    return sprites.containsKey(name) && sprites.get(name) != null;
  }

  private void load() {
    for (String name : SPRITE_NAMES) {
      String path = "/sprites/" + name + ".png";
      try {
        Image img = new Image(getClass().getResourceAsStream(path));
        if (!img.isError()) {
          sprites.put(name, img);
          System.out.println("sprite charger: " + name + " [ OK ]");
        } else {
          System.out.println("sprites introuvable (fallback couleur) :" + name);
        }
      } catch (Exception e) {
        System.out.println("sprite introuvable (fallback couleur) : " + name);
      }
    }
  }

}
