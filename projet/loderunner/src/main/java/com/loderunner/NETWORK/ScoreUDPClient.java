package com.loderunner.NETWORK;

import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

//utilable dans un try with ressource
public class ScoreUDPClient implements AutoCloseable {
  private static final int TIMEOUT_MS = 500;
  private static final int MAX_TRY = 3;
  private DatagramSocket socket;
  private InetAddress addr;
  private int port;
  private final AtomicInteger seq = new AtomicInteger(1);

  public ScoreUDPClient(String ip, int port) {
    try {
      this.socket = new DatagramSocket();
      this.socket.setSoTimeout(TIMEOUT_MS);
      this.addr = InetAddress.getByName(ip);
      this.port = port;
    } catch (Exception e) {
      // TODO: handle exception
      System.err.println("[score] error init: " + e.getMessage());
    }
  }

  public boolean submit(String player, int score, int level) {
    if (socket == null || socket.isClosed()) {
      System.err.println("[score] socket fermer,impossible d envoyer le score: ");
      return false;
    }
    int id = seq.getAndIncrement();
    ScorePacket pkt = ScorePacket.submit(id, player, score, level);
    for (int at = 1; at <= MAX_TRY; at++) {
      try {
        send(pkt);
        ScorePacket resp = receive();
        if (resp != null && resp.type == ScorePacket.ACK && resp.seqId == id) {
          System.out.println("[score] ack recu mgl");
          return true;
        }
        if (resp != null && resp.type == ScorePacket.ERROR) {
          System.out.println("[score] serv error, retransmission");
        }
      } catch (SocketTimeoutException e) {
        System.out.println("[score] Timeout " + at + "/" + MAX_TRY);
      } catch (Exception e) {
        System.out.println("[score] erreur reseau :" + e.getMessage());
        return false;
      }
    }
    System.err.println("[score] echec apres " + MAX_TRY + "tentative");
    return false;
  }

  public void disconect() {
    if (socket != null && !socket.isClosed()) {
      socket.close();
      System.out.println("[score] client deco");
    }
  }

  @Override
  public void close() {
    disconect();
  }

  private void send(ScorePacket pkt) throws Exception {
    byte[] data = pkt.toBytes();
    socket.send(new DatagramPacket(data, data.length, addr, port));
  }

  private ScorePacket receive() throws Exception {
    byte[] buf = new byte[ScorePacket.MAX_SIZE];
    DatagramPacket dp = new DatagramPacket(buf, buf.length);
    socket.receive(dp);
    try {
      return ScorePacket.fromBytes(dp.getData(), dp.getLength());
    } catch (IllegalArgumentException e) {
      System.err.println("[score] paquet corrompu ignorer");
      return null;
    }
  }
}
