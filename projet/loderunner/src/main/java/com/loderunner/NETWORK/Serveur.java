package com.loderunner.NETWORK;

import com.loderunner.GAME.game;
import java.io.*;
import java.net.*;

public class Serveur {
  private static final int port = 8990;
  private static final int udp = 9000;
  private static game gameInstance;

  public static void main(String[] args) {
    ScoreUDPServer scoreServer = new ScoreUDPServer(udp);
    Thread scoreThread = new Thread(scoreServer, "score");
    scoreThread.start();
    gameInstance = new game();
    gameInstance.setMulti(true);
    try {
      gameInstance.loadlevel(1);
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Serveur heberge sur le port " + port + ". Attente des joueurs...");

      // connexion j1
      Socket client1 = serverSocket.accept();
      System.out.println("J1 connecte (L'hote) !");
      ObjectOutputStream out1 = new ObjectOutputStream(client1.getOutputStream());
      BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));

      // connexion j2
      System.out.println("En attente du J2 sur le reseau...");
      Socket client2 = serverSocket.accept();
      System.out.println("J2 connecte ! Lancement de la partie.");
      ObjectOutputStream out2 = new ObjectOutputStream(client2.getOutputStream());
      BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));

      // ecoute clavier
      new Thread(() -> ecouterClient(in1, true)).start(); // Thread J1
      new Thread(() -> ecouterClient(in2, false)).start(); // Thread J2
      boolean scoreSent = false;
      // jeu (10fps)
      while (true) {
        gameInstance.update();
        // envoie du score une fois en din de partie
        if (!scoreSent && (gameInstance.isGameOver() || gameInstance.isVictory())) {
          scoreSent = true;
          final int finalScore = gameInstance.getScore();
          final int finalLevel = gameInstance.getLevel();
          // final String name = gameInstance.getPlayerName();// a def dcp
          new Thread(() -> {
            try (ScoreUDPClient client = new ScoreUDPClient("127.0.0.1", udp)) {
              client.submit("name", finalScore, finalLevel);
            } catch (Exception e) {
            }
          }).start();

        }
        // envoie map a j1
        out1.reset();
        out1.writeObject(gameInstance.getGameMap());
        out1.flush();

        // envoie map a J2
        out2.reset();
        out2.writeObject(gameInstance.getGameMap());
        out2.flush();

        Thread.sleep(100);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // touches clavier
  private static void ecouterClient(BufferedReader in, boolean isJ1) {
    try {
      String touche;
      while ((touche = in.readLine()) != null) {
        if (isJ1) {
          // touches J1
          switch (touche) {
            case "Z":
              gameInstance.actionMoveUp();
              break;
            case "S":
              gameInstance.actionMoveDown();
              break;
            case "Q":
              gameInstance.actionMoveLeft();
              break;
            case "D":
              gameInstance.actionMoveRight();
              break;
            case "A":
              gameInstance.actionDigLeft();
              break;
            case "E":
              gameInstance.actionDigRight();
              break;
          }
        } else {
          // touches J2
          gameInstance.actionJ2(touche);
        }
      }
    } catch (IOException e) {
      System.out.println(isJ1 ? "J1 a quitte." : "J2 a quitte.");
    }
  }
}
