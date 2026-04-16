package com.loderunner.UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * 3 opt (et un bouton pour choisir le mode de map)
 * chaque btn fait des callback donner par lmain
 * comme sa il gere pas game ni gameWindow
 */
public class ScreenMenu {
    private final Stage stage;
    private boolean useGeneration = true; // par defaut, on utilise l'algo de map

    public ScreenMenu(Stage stage) {
        this.stage = stage;
    }

    // On utilise Consumer<Boolean> pour envoyer le choix de la map a Main
    public void show(Consumer<Boolean> Solo, Consumer<Boolean> Multi, Runnable Quit) {
        Text title = new Text("Lode Runner");
        // a changer
        title.setFill(Color.YELLOW);
        title.setFont(Font.font("Monospace", 36));

        Button btnSolo = makeButton("Jouer en solo");
        Button btnMulti = makeButton("Multijoueur");

        // NOUVEAU : Bouton pour changer de type de Map
        Button btnToggleMap = makeButton("Map : GÉNÉRATIVE");
        btnToggleMap.setStyle(btnToggleMap.getStyle() + "-fx-text-fill: #00ff00;");

        btnToggleMap.setOnAction(e -> {
            useGeneration = !useGeneration;
            if (useGeneration) {
                btnToggleMap.setText("Map : GÉNÉRATIVE");
                btnToggleMap.setStyle(btnToggleMap.getStyle() + "-fx-text-fill: #00ff00;");
            } else {
                btnToggleMap.setText("Map : FICHIER (.txt)");
                btnToggleMap.setStyle(btnToggleMap.getStyle() + "-fx-text-fill: #ffaa00;");
            }
        });

        Button btnQuit = makeButton("Quitter");

        btnSolo.setOnAction(e -> Solo.accept(useGeneration));
        btnMulti.setOnAction(e -> Multi.accept(useGeneration));
        btnQuit.setOnAction(e -> Quit.run());

        VBox layout = new VBox(20, title, btnSolo, btnMulti, btnToggleMap, btnQuit);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#000000;");
        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("Lode Runner L2");
        stage.setScene(scene);
        stage.show();
    }

    private Button makeButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Monospace", 36));
        btn.setStyle(
                "-fx-background-color:#222222;" +
                        "-fx-text-fill:#ffffff;" +
                        "-fx-border-color:#ffffff;" +
                        "-fx-border-width:1px;" +
                        "-fx-padding:10 40 10 40;");
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color:#444444;" +
                        "-fx-text-fill:#ffff00;" +
                        "-fx-border-color:#fff00;" +
                        "-fx-border-width:1px;" +
                        "-fx-padding:10 40 10 40;"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color:#222222;" +
                        "-fx-text-fill:#ffffff;" +
                        "-fx-border-color:#fffff;" +
                        "-fx-border-width:1px;" +
                        "-fx-padding:10 40 10 40;"));
        return btn;
    }
}