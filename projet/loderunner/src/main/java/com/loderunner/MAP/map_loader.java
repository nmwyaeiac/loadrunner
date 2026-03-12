package com.loderunner.MAP;

import com.loderunner.ENTITY.*;
import com.loderunner.ENTITY.WALL.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * map_loader
 */
public class map_loader {
  public static map LoadFromFile(String fPath) throws IOException {
    List<String> line = Files.readAllLines(Paths.get(fPath));
    if (line.isEmpty())
      throw new IOException("Fichier de niveau vide :" + fPath);
    // a faire

  }

}
