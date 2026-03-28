package com.loderunner;

import com.loderunner.GAME.game;
import com.loderunner.UI.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    game gameInstance = new game();
    try {
      gameInstance.loadlevel(1);
    } catch (IOException e) {
      System.err.println("bug de chargement gros :" + e.getMessage());
      return;
    }
    new GameWindow(gameInstance).show(primaryStage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
