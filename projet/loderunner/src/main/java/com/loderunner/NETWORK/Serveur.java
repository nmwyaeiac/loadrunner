package com.loderunner.NETWORK;

import com.loderunner.GAME.game;
import java.io.*;
import java.net.*;

public class Serveur {
    private static final int port = 8990;
    private static final int udp = 9000;
    private static game gameInstance;
    public static volatile boolean serverRunning = true; // BOUTON D'ARRET

    public static void main(String[] args) {
        serverRunning = true;
        // lecture du mode de jeu et de la map
        boolean modeCoop = args.length > 0 && args[0].equals("COOP");
        boolean useGen = true;
        if (args.length > 1) {
            useGen = args[1].equals("GEN");
        }

        ScoreUDPServer scoreServer = new ScoreUDPServer(udp);
        new Thread(scoreServer, "score").start();

        gameInstance = new game();
        gameInstance.setMulti(true);
        gameInstance.setCoop(modeCoop);
        gameInstance.setUseGeneration(useGen); // Application de la regle de map

        try {
            gameInstance.loadlevel(1);
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serveur heberge sur le port " + port);

            // connexion j1
            Socket client1 = serverSocket.accept();
            ObjectOutputStream out1 = new ObjectOutputStream(client1.getOutputStream());
            BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));

            // connexion j2
            Socket client2 = serverSocket.accept();
            ObjectOutputStream out2 = new ObjectOutputStream(client2.getOutputStream());
            BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));

            // ecoute clavier
            new Thread(() -> ecouterClient(in1, true)).start();
            new Thread(() -> ecouterClient(in2, false)).start();

            boolean scoreSent = false;

            // jeu (10fps)
            while (serverRunning) {
                gameInstance.update();
                // envoie du score une fois en din de partie
                if (!scoreSent && (gameInstance.isGameOver() || gameInstance.isVictory())) {
                    scoreSent = true;
                    final int finalScore = gameInstance.getScore();
                    final int finalLevel = gameInstance.getLevel();
                    new Thread(() -> {
                        try (ScoreUDPClient client = new ScoreUDPClient("127.0.0.1", udp)) {
                            client.submit("Hote", finalScore, finalLevel);
                        } catch (Exception e) {}
                    }).start();
                }

                try {
                    out1.reset(); out1.writeObject(gameInstance.getGameMap()); out1.flush();
                    out2.reset(); out2.writeObject(gameInstance.getGameMap()); out2.flush();
                } catch(Exception e) {
                    serverRunning = false; // stop si on arrive plus a envoyer (joueur quiite etc)
                }

                Thread.sleep(100);
            }

            // fin du serveur
            serverSocket.close();
            scoreServer.stop();
            System.out.println("Partie réseau terminée. Port libéré.");

        } catch (Exception e) {
            System.out.println("Erreur serveur : " + e.getMessage());
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
                        case "Z": gameInstance.actionMoveUp(); break;
                        case "S": gameInstance.actionMoveDown(); break;
                        case "Q": gameInstance.actionMoveLeft(); break;
                        case "D": gameInstance.actionMoveRight(); break;
                        case "A": gameInstance.actionDigLeft(); break;
                        case "E": gameInstance.actionDigRight(); break;
                    }
                } else {
                    // touches J1
                    gameInstance.actionJ2(touche);
                }
            }
        } catch (IOException e) {
            System.out.println(isJ1 ? "J1 a quitté." : "J2 a quitté.");
            serverRunning = false; // stop si rq
        }
    }
}