package com.loderunner.NETWORK;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class ScoreUDPServer implements Runnable {
  private static class Entry {
    String player;
    int score, level;

    Entry(String p, int s, int l) {
      this.player = p;
      this.score = s;
      this.level = l;
    }
  }

  private static final int TOP = 10;
  private static final String FILE_PATH = "src/com/loderunner/RESSOURCES/score/leaderboard.dat";
  private final int port;
  private final List<Entry> board = new ArrayList<>();
  private volatile boolean running = true;

  public ScoreUDPServer(int port) {
    this.port = port;
    load();
  }

  @Override
  public void run() {
    try (DatagramSocket socket = new DatagramSocket(port)) {
      System.out.println("[ScoreServ] actif sur le port" + port);
      byte[] buf = new byte[ScorePacket.MAX_SIZE];
      while (running) {
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        try {
          socket.receive(dp);
        } catch (SocketException e) {
          System.out.println(e.getMessage());
          break;
        }
        byte[] data = Arrays.copyOf(dp.getData(), dp.getLength());
        InetAddress clientAddr = dp.getAddress();
        int clientPort = dp.getPort();
        new Thread(() -> handle(data, clientAddr, clientPort, socket)).start();
      }
    } catch (Exception e) {
      System.err.println("[ScoreServer] erreur " + e.getMessage());
    }
  }

  public void stop() {
    running = false;
  }

  private void handle(byte[] data, InetAddress addr, int port, DatagramSocket socket) {
    try {
      ScorePacket pkt = ScorePacket.fromBytes(data, data.length);
      if (pkt == null || pkt.type != ScorePacket.SUBMIT)
        return;
      addScore(pkt.getString("player"), pkt.getInt("score"), pkt.getInt("level"));
      reply(socket, ScorePacket.ack(pkt.seqId), addr, port);
    } catch (Exception e) {
      reply(socket, ScorePacket.error(0), addr, port);
    }
  }

  private synchronized void addScore(String player, int score, int level) {
    boolean f = false;
    for (Entry e : board) {
      if (e.player.equals(player)) {
        if (score > e.score) {
          e.score = score;
          e.level = level;
        }
        f = true;
        break;
      }
    }
    if (!f)
      board.add(new Entry(player, score, level));
    board.sort((a, b) -> Integer.compare(b.score, a.score));
    if (board.size() > TOP)
      board.subList(TOP, board.size()).clear();
    save();
  }

  private void save() {
    File file = new File(System.getProperty("user.dir"), FILE_PATH);
    try (BufferedWriter bw = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
      for (Entry e : board) {
        bw.write(e.player + ";" + e.score + ";" + e.level);
        bw.newLine();
      }
      bw.flush();
      System.out.println("[Score serveur] sauvegarde effectuer dans " + file.getAbsolutePath());
    } catch (IOException e) {
      System.err.println("[Score server] erreur, le fichier na pas pu etre sauvegarde " + e.getMessage());
    }
  }

  private void load() {
    File file = new File(System.getProperty("user.dir"), FILE_PATH);
    if (!file.exists()) {
      System.out.println("[ScoreServer] aucun score existant (fichier attendu :" + file.getAbsolutePath() + " )");
      return;
    }
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
      String line;
      board.clear();
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty())
          continue;
        String parts[] = line.split(";");
        if (parts.length == 3) {
          board.add(new Entry(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }
      }
      System.out.println("[ScoreServer]" + board.size() + "scores charges");
    } catch (Exception e) {
      System.err.println("[ScoreServer] erreur de chargement");
    }
  }

  private void reply(DatagramSocket sckt, ScorePacket pkt, InetAddress addr, int port) {
    try {
      byte[] data = pkt.toBytes();
      sckt.send(new DatagramPacket(data, data.length, addr, port));
    } catch (IOException ioe) {
    }
  }
}
