package com.john.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WinRegistry {

  private static final String REGQUERY_UTIL = "REG";

  private static final String REGSTR_TOKEN = "REG_BINARY";

  public static byte[] getReg(String path, String name) {
    String result = "";
    try {
      String[] cmd = { REGQUERY_UTIL, "QUERY", path, "/v", name };
      System.out.println(String.join(" ", cmd));
      Process process = Runtime.getRuntime().exec(cmd);
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();
      result = reader.getResult();
      int p = result.indexOf(REGSTR_TOKEN);

      if (p == -1)
        return null;

      result = result.substring(p + REGSTR_TOKEN.length()).trim();
      return hexStringToByteArray(result);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public static void main(String[] args) {
    System.out.print(hexStringToByteArray("390FFC122947ECA986DEF4D20D51E1A0A9545D3BD2922C64CA4B8DDA0CA1C7AE"));
  }

  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  static class StreamReader extends Thread {
    private InputStream is;
    private StringWriter sw;

    StreamReader(InputStream is) {
      this.is = is;
      sw = new StringWriter();
    }

    public void run() {
      try {
        int c;
        while ((c = is.read()) != -1)
          sw.write(c);
      } catch (IOException e) {
        ;
      }
    }

    String getResult() {
      return sw.toString();
    }
  }
}
