package com.loderunner;

import com.loderunner.GAME.game;
import com.loderunner.UI.GameWindow;
import com.loderunner.UI.ScreenMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;
import com.loderunner.UI.NetworkWindow;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ScreenMenu menu = new ScreenMenu(primaryStage);
        menu.show(
                (useGen) -> StartSolo(primaryStage, useGen),
                (useGen) -> StartPanelMulti(primaryStage, useGen),
                () -> Platform.exit());
    }

    private Runnable returnToMenu(Stage stage) {
        return () -> {
            try { start(stage); } catch (Exception e) {}
        };
    }

    private void StartSolo(Stage stage, boolean useGen) {
        game gameInstance = new game();
        gameInstance.setUseGeneration(useGen); // envoi de l'option map
        try {
            gameInstance.loadlevel(1);
        } catch (IOException e) {
            System.err.println("bug de chargement gros :" + e.getMessage());
            return;
        }
        new GameWindow(gameInstance, returnToMenu(stage)).show(stage);
    }

    private void StartPanelMulti(Stage stage, boolean useGen) {
        javafx.scene.layout.VBox menu = new javafx.scene.layout.VBox(20);
        menu.setAlignment(javafx.geometry.Pos.CENTER);
        menu.setStyle("-fx-background-color: #000000;");

        javafx.scene.text.Text title = new javafx.scene.text.Text("MODE MULTIJOUEUR");
        title.setFill(javafx.scene.paint.Color.YELLOW);
        title.setFont(javafx.scene.text.Font.font("Monospace", 36));

        javafx.scene.control.Button btnHostCoop = new javafx.scene.control.Button("1. Héberger (Mode Coop)");
        btnHostCoop.setFont(javafx.scene.text.Font.font("Monospace", 20));

        javafx.scene.control.Button btnHostCombat = new javafx.scene.control.Button("2. Héberger (Mode Combat)");
        btnHostCombat.setFont(javafx.scene.text.Font.font("Monospace", 20));

        javafx.scene.control.Label ipLabel = new javafx.scene.control.Label("Adresse IP de l'hôte :");
        ipLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        javafx.scene.control.TextField ipField = new javafx.scene.control.TextField("127.0.0.1");
        ipField.setMaxWidth(150);

        javafx.scene.control.Button btnJoin = new javafx.scene.control.Button("3. Rejoindre la partie");
        btnJoin.setFont(javafx.scene.text.Font.font("Monospace", 20));

        String modeMap = useGen ? "GEN" : "FILE"; // On passe le choix au Serveur

        btnHostCoop.setOnAction(e -> {
            new Thread(() -> com.loderunner.NETWORK.Serveur.main(new String[]{"COOP", modeMap})).start();
            new NetworkWindow("127.0.0.1", returnToMenu(stage)).show(stage);
        });

        btnHostCombat.setOnAction(e -> {
            new Thread(() -> com.loderunner.NETWORK.Serveur.main(new String[]{"COMBAT", modeMap})).start();
            new NetworkWindow("127.0.0.1", returnToMenu(stage)).show(stage);
        });

        btnJoin.setOnAction(e -> {
            new NetworkWindow(ipField.getText(), returnToMenu(stage)).show(stage);
        });

        menu.getChildren().addAll(title, btnHostCoop, btnHostCombat, ipLabel, ipField, btnJoin);
        stage.setScene(new javafx.scene.Scene(menu, 800, 600));
    }

    public static void main(String[] args) {
        launch(args);
    }
}