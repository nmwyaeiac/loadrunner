package com.loderunner.NETWORK;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import com.loderunner.ENTITY.character;

/*
 * protocole custom:
 * 4b :magic on l appel "lrsc"
 * 1b: pour le type 
 * 4: seq id 
 * 4b : crc32 
 * et le payload en utf_8 (json)
 * 13 b
 *
 * type : 
 * submit 
 * ack 
 * error
 */
public class ScorePacket {
  public static final int MAGIC = 0x4c525343;// "lrsc"
  public static final int HEADER_SIZE = 13;
  public static final int MAX_SIZE = 512;
  public static final byte SUBMIT = 0x01;
  public static final byte ACK = 0x02;
  public static final byte ERROR = 0x03;
  public final byte type;
  public final int seqId;
  public final String payload;

  private ScorePacket(byte type, int seqId, String payload) {
    this.type = type;
    this.seqId = seqId;
    this.payload = payload;
  }

  public static ScorePacket submit(int seq, String player, int score, int level) {
    String p = String.format("{\"player\":\"%s\",\"score\":%d,\"level\":%d}", player.replace("\"", ""), score, level);
    // exempple de payload {"player":"nomDuJoueur","score":1234,"level":5}
    return new ScorePacket(SUBMIT, seq, p);
  }

  public static ScorePacket ack(int seq) {
    return new ScorePacket(ACK, seq, "");
  }

  public static ScorePacket error(int seq) {
    return new ScorePacket(ERROR, seq, "");
  }

  public byte[] toBytes() {
    byte[] payloadbytes = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);// suuport utf 8 character
    ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE + payloadbytes.length);
    buf.putInt(MAGIC);
    buf.put(type);
    buf.putInt(seqId);
    buf.putInt(crc(payload));
    buf.put(payloadbytes);
    return buf.array();
  }

  public static ScorePacket fromBytes(byte[] data, int len) {
    if (len < HEADER_SIZE)
      return null;
    ByteBuffer buf = ByteBuffer.wrap(data, 0, len);
    if (buf.getInt() != MAGIC)
      return null;
    byte type = buf.get();
    int seqId = buf.getInt();
    int received = buf.getInt();

    byte[] pb = new byte[len - HEADER_SIZE];
    buf.get(pb);
    String payload = new String(pb, java.nio.charset.StandardCharsets.UTF_8);
    if (received != crc(payload))
      throw new IllegalArgumentException("CRC invalide");
    return new ScorePacket(type, seqId, payload);
  }

  public static int crc(String s) {
    CRC32 c = new CRC32();
    c.update(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    return (int) c.getValue();
  }

  public String getString(String key) {
    String search = "\"" + key + "\":\"";
    int s = payload.indexOf(search);
    if (s == -1)
      throw new IllegalArgumentException("manque la clef gros: " + key);
    s += search.length();
    return payload.substring(s, payload.indexOf('"', s));
  }

  public int getInt(String key) {
    String search = "\"" + key + "\":\"";
    int s = payload.indexOf(search);
    if (s == -1)
      throw new IllegalArgumentException("manque la clef gros: " + key);
    s += search.length();
    int e = s;
    while (e < payload.length() && Character.isDigit(payload.charAt(e)))
      e++;
    return Integer.parseInt(payload.substring(s, e));
  }
}
