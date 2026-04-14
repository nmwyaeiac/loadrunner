package com.loderunner.UI;

import com.loderunner.MAP.map;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkWindow {
    private static final int TILE_SIZE = 40;
    private static final int WIDTH = 20 * TILE_SIZE;
    private static final int HEIGHT = 15 * TILE_SIZE;

    private GameRenderer renderer;
    private PrintWriter out;
    private String ip;
    private Runnable onMenu;
    private Socket socket; // VARIABLE GLOBALE POUR LE COUPER

    public NetworkWindow(String ip, Runnable onMenu) {
        this.ip = ip;
        this.onMenu = onMenu;
    }

    public void show(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        SpriteLoader sprites = new SpriteLoader();
        // nv gamerenderer
        renderer = new GameRenderer(gc, WIDTH, HEIGHT, sprites);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        // envoie touches au serv
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                try {
                    if (socket != null) socket.close(); // ON COUPE LE CABLE
                } catch (Exception e) {}
                if (onMenu != null) onMenu.run(); // RETOUR MENU
                return;
            }
            if (out != null) {
                out.println(event.getCode().toString());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lode Runner - Mode Réseau");
        primaryStage.show();
        // Connexion back
        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        try {
            socket = new Socket(ip, 8990);
            out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                // recoit map
                map currentMap = (map) in.readObject();
                Platform.runLater(() -> {
                    // map to afficheur
                    renderer.renderGame(currentMap, currentMap.getPlayer(), 0, 1, false, false);
                });
            }
        } catch (Exception e) {
            System.out.println("Déconnecté du serveur.");
        }
    }
}