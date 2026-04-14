package com.loderunner.GAME;

import com.loderunner.INPUT.inputhandler;
import com.loderunner.INPUT.inputhandler.Action;
import com.loderunner.MAP.map;
import com.loderunner.MAP.map_loader;
import com.loderunner.MAP.map_generator;
import com.loderunner.ENTITY.player;
import com.loderunner.ENTITY.enemy;
import com.loderunner.ENTITY.character;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * game
 * un thread pour lire les entrer sortie
 * on va utiliser une blocking list
 */
public class game {
    private map gameMap;
    private player gamePlayer;
    private enemy j2Enemy; // <-- LE PERSO DU JOUEUR 2
    private physics physics;
    private boolean isMulti = false;
    private boolean isCoop = false;
    private map_generator generator = new map_generator();
    private PathFinder pf = new PathFinder();
    private int tickIa = 0;

    private int score = 0;
    private int level = 1;
    private boolean running = false;
    private boolean gameOver = false;
    private boolean victory = false;

    // 2 files d'attente pour eviter concurrence clavier
    private final LinkedBlockingQueue<Action> inputQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> j2InputQueue = new LinkedBlockingQueue<>();

    public game() {}

    public void setMulti(boolean m) {
        this.isMulti = m;
    }

    public void setGameMap(map m) {
        this.gameMap = m;
        this.gamePlayer = m.getPlayer();
    }

    public void setCoop(boolean m) { this.isCoop = m; }

    public void loadlevel(int levelNumber) throws IOException {
        this.gameMap = generator.generate(levelNumber);

        this.level = levelNumber;
        gamePlayer = gameMap.getPlayer();
        if (gamePlayer == null) throw new IOException("pas de joueur dans le niveau");

        // zssigner j2 (coop/multi)
        if (isMulti) {
            if (isCoop) {
                // coop : j2 a cote de j1
                j2Enemy = new enemy(gamePlayer.getX()+1, gamePlayer.getY());
                j2Enemy.setMate(true);
                gameMap.addenemies(j2Enemy);
            } else if (!gameMap.getEnemy().isEmpty()) {
                // 1v1 : prend place enemy
                j2Enemy = gameMap.getEnemy().get(0);
            } else {
                j2Enemy = null;
            }
        }

        physics = new physics(gameMap);
        gameOver = false;
        victory = false;
        running = true;
    }

    public void update() {
        if (!running || gameOver || victory) return;

        processInput();   // Touches J1
        processJ2Input(); // Touches J2

        physics.applyGravity(gamePlayer);
        for (enemy e : gameMap.getEnemy()) physics.applyGravity(e);

        if (physics.playerCollectsGold(gamePlayer)) score += 100;

        // J2 ramasse aussi l'or en Coop !
        if (isMulti && isCoop && j2Enemy != null) {
            com.loderunner.ENTITY.gold g = gameMap.getGoldAt(j2Enemy.getX(), j2Enemy.getY());
            if (g != null) {
                gameMap.removeGold(g);
                score += 100;
            }
        }

        // Collisions (Modifie pour le mode Coop)
        boolean dead = false;
        for (enemy e : gameMap.getEnemy()) {
            if (isCoop && e == j2Enemy) continue; // EN COOP : J1 et J2 se traversent sans mourir !

            if (gamePlayer.getX() == e.getX() && gamePlayer.getY() == e.getY()) {
                dead = true;
                break;
            }
        }

        if (dead) {
            gamePlayer.LoseLife();
            if (!gamePlayer.isAlive()) {
                gameOver = true; running = false; return;
            }
            respawnPlayer();
        }

        gameMap.update();

        // IA
        tickIa++;
        if (tickIa >= 4) {
            for (enemy e : gameMap.getEnemy()) {
                if (e == j2Enemy) continue; // Le J2 n'a pas d'IA
                if (!e.getIsUnderGround()) {
                    character.Direction next = pf.getNextStep(e, gamePlayer, gameMap);
                    if (next != null) {
                        switch (next) {
                            case LEFT: physics.moveLeft(e); break;
                            case RIGHT: physics.moveRight(e); break;
                            case UP: physics.moveUP(e); break;
                            case DOWN: physics.moveDown(e); break;
                        }
                    } else {
                        if (e.getX() < gamePlayer.getX()) physics.moveRight(e);
                        else if (e.getX() > gamePlayer.getX()) physics.moveLeft(e);
                    }
                }
            }
            tickIa = 0;
        }

        if (gameMap.isLevelCompleted()) {
            level++;
            try { loadlevel(level); } catch (IOException e) { victory = true; running = false; }
        }
    }

    private void processJ2Input() {
        String touche = j2InputQueue.poll();
        if (touche == null || j2Enemy == null) return;

        switch (touche) {
            case "Z": physics.moveUP(j2Enemy); break;
            case "S": physics.moveDown(j2Enemy); break;
            case "Q": physics.moveLeft(j2Enemy); break;
            case "D": physics.moveRight(j2Enemy); break;
            // En Coop, le J2 a le droit de creuser !
            case "A": if(isCoop) physics.digLeft(j2Enemy); break;
            case "E": if(isCoop) physics.digRight(j2Enemy); break;
        }
    }

    public void start() throws IOException {
        loadlevel(level);
        running = true;
    }

    // inputs J1
    public void actionMoveUp() { if (running) inputQueue.offer(Action.MOVE_UP); }
    public void actionMoveDown() { if (running) inputQueue.offer(Action.MOVE_DOWN); }
    public void actionMoveLeft() { if (running) inputQueue.offer(Action.MOVE_LEFT); }
    public void actionMoveRight() { if (running) inputQueue.offer(Action.MOVE_RIGHT); }
    public void actionDigLeft() { if (running) inputQueue.offer(Action.DIG_LEFT); }
    public void actionDigRight() { if (running) inputQueue.offer(Action.DIG_RIGHT); }

    private void processInput() {
        Action action = inputQueue.poll();
        if (action == null) return;
        switch (action) {
            case MOVE_LEFT: physics.moveLeft(gamePlayer); break;
            case MOVE_RIGHT: physics.moveRight(gamePlayer); break;
            case MOVE_UP: physics.moveUP(gamePlayer); break;
            case MOVE_DOWN: physics.moveDown(gamePlayer); break;
            case DIG_LEFT: physics.digLeft(gamePlayer); break;
            case DIG_RIGHT: physics.digRight(gamePlayer); break;
            case QUIT: running = false; break;
        }
    }

    // Ibputs j2
    public void actionJ2(String touche) {
        if (running) j2InputQueue.offer(touche);
    }

    private void respawnPlayer() {
        if (!gamePlayer.isAlive()) return;
        try {
            int lives = gamePlayer.getlife();
            loadlevel(level);
            gamePlayer.setLife(lives);
        } catch (IOException e) { running = false; }
    }

  // getter
  public map getGameMap() {
    return gameMap;
  }

  public player getGamePlayer() {
    return gamePlayer;
  }

  public int getScore() {
    return score;
  }

  public int getLevel() {
    return level;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isVictory() {
    return victory;
  }
}
