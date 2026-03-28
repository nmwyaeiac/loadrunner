package com.loderunner;

import com.loderunner.GAME.game;
import com.loderunner.UI.GameWindow;
import com.loderunner.UI.ScreenMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    ScreenMenu menu = new ScreenMenu(primaryStage);
    menu.show(
        () -> StartSolo(primaryStage),
        () -> StartPanelMulti(primaryStage),
        () -> Platform.exit());
  }

  private void StartSolo(Stage stage) {
    game gameInstance = new game();
    try {
      gameInstance.loadlevel(1);
    } catch (IOException e) {
      System.err.println("bug de chargement gros :" + e.getMessage());
      return;
    }
    new GameWindow(gameInstance).show(stage);
  }

  private void StartPanelMulti(Stage stage) {
    // todo creer un menu pour mettre si on heberge la game ou si on veut rejoindre
    // une game
    // on entre l ip dans un champs et sa lance
  }

  public static void main(String[] args) {
    launch(args);
  }
}
