package com.john.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class WinRegistry {

  private static final String REGQUERY_UTIL = "REG QUERY";

  private static final String REGSTR_TOKEN = "REG_BINARY";

  public static byte[] getReg(String path, String name) {
    String result = "";

    String line = "";
    ArrayList<String> result2 = new ArrayList<String>();

    try {
      String[] cmd = { REGQUERY_UTIL, path, "/v", name };
      System.out.println(String.join(" ", cmd));
      Process process = Runtime.getRuntime().exec(cmd);
      StreamReader reader = new StreamReader(process.getInputStream());

      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      while ((line = input.readLine()) != null) {
        result2.add(new String(line));
      }
      BufferedReader input2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      while ((line = input2.readLine()) != null) {
        result2.add(new String(line));
      }
      int exitvalue = process.waitFor();

      reader.start();
      process.waitFor();
      reader.join();

      result = reader.getResult();
      int p = result.indexOf(REGSTR_TOKEN);

      if (p == -1)
        return null;

      result = result.substring(p + REGSTR_TOKEN.length()).trim();
      // 390FFC122947ECA986DEF4D20D51E1A0A9545D3BD2922C64CA4B8DDA0CA1C7AE
      return hexStringToByteArray(result);
    } catch (Exception e) {
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
