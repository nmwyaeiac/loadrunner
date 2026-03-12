package com.loderunner;

import com.loderunner.GAME.game;
import java.io.IOException;;

public class loderunner {
  public static void main(String[] args) {
    game g = new game();
    try {
      g.start();
    } catch (IOException e) {
      System.err.println("Erreur: " + e.getMessage());
      System.err.println("Verifier que le dossier levels/ exist avc levelx.txt");
      System.err.println("x= le numero du lvl");
    }
  }
}
