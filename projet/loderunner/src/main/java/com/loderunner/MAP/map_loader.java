package com.loderunner.MAP;

import com.loderunner.ENTITY.*;
import com.loderunner.ENTITY.WALL.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class map_loader {
  public static map LoadFromFile(String fPath) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(fPath));
    if (lines.isEmpty())
      throw new IOException("fichier vide gros " + fPath);

    int height = lines.size();
    // a changer j ai pas trouver de meilleur solution pour sa merci stackoverflow
    int width = lines.stream().mapToInt(String::length).max().orElse(0);

    map m = new map(width, height);

    for (int y = 0; y < height; y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        switch (c) {
          case '#':
            m.addBrick(new brick(x, y));
            break;
          case '+':
            m.addBedrock(new bedrock(x, y));
            break;
          case 'H':
            m.addLadders(new ladder(x, y));
            break;
          case '-':
            m.addMonkeysBars(new monkey_bars(x, y));
            break;
          case 'g':
            m.addGold(new gold(x, y, 1));
            break;
          case '@':
            m.setPlayer(new player(x, y));
            break;
          case 'E':
            m.addenemies(new enemy(x, y));
            break;
          default:
            break; // espace = vide
        }
      }
    }

    return m;
  }
}
