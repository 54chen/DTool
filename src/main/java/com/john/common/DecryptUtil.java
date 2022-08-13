package com.john.common;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DecryptUtil {
    public static String Encode(byte[] bytes) throws Exception {
        if (bytes == null)
            throw new Exception("bytes");

        String enc1 = "";

        StringBuilder sb = new StringBuilder(bytes.length * 5 / 4);
        int count = 0;
        int value = 0;
        for (byte b : bytes) {
            value |= ((int) b) << (24 - (count * 8));
            count++;

            if (count == 4) {
                if (value == 0)
                    sb.append('z');
                else
                    EncodeValue(sb, value, 0);
                count = 0;
                value = 0;
            }
        }
        if (count > 0)
            EncodeValue(sb, value, 4 - count);

        enc1 = sb.toString().trim();
        return enc1;
    }

    // Writes the Ascii85 characters for a 32-bit value to a StringBuilder.
    private static void EncodeValue(StringBuilder sb, int value, int paddingBytes) {
        char[] encoded = new char[5];

        for (int index = 4; index >= 0; index--) {
            encoded[index] = (char) ((value % 85) + c_firstCharacter);
            value /= 85;
        }

        if (paddingBytes != 0)
            encoded = Arrays.copyOf(encoded, 5 - paddingBytes);

        sb.append(encoded);
    }

    public static byte[] DecodeStr(String str) {
        int m = 4;
        StringBuilder sb = new StringBuilder(str.length() * 4 / 5);
        int value = 0;
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);
            if (m == 4 && c == 'z') {
                sb.append(" ");
                value = 0;
                m=4;
                continue;
            }
            int cInt = c;
            int cFInt = c_firstCharacter;
            value += (cInt - cFInt) * Math.pow(85, m); // ??!
            m--;
            if (m < 0 || i == str.length() - 1) {
                Decode(value, sb);
                value = 0;
                m = 4;
            }

        }
        return sb.toString().getBytes();
    }

    private static void Decode(int value, StringBuilder sb) {
        //TODO z
        byte[] a = ByteBuffer.allocate(4).putInt(value).array();
        for (byte bt : a) {
            sb.append((char)bt);
        }
    }

    // the first and last characters used in the Ascii85 encoding character set
    static char c_firstCharacter = '!';

    public static void main(String[] args) throws Exception {
        String str = "this is a string for encode z!";
        String result = Encode(str.getBytes());
        System.out.println(result);

        byte[] s = FileUtil.readFile("/Users/chenzhen/Documents/workspace/DTool/src/main/java/com/john/common/test.txt");
        byte[] cr = DecodeStr(new String(s));
        String org = new String(cr);
        System.out.println(org);

        // int value = 0;
        // byte[] b = {124,100,20,20};

        // for (int i = 0; i < 4; i++) {
        //     System.out.println(Integer.toBinaryString(b[i]));

        //     value |= ((int) b[i]) << (24 - (i * 8));
        //     System.out.println(Integer.toBinaryString(value));
        // }
        // System.out.println(Integer.toBinaryString(value));

        // for (int i = 0; i < b.length; i++) {
        //     byte xx = (byte) value;
        //     System.out.println(xx);
        //     value >> i*8;
        // }
 

        // char a = '!';
        // int x = a;
        // System.out.println(x);

        // int value = 99;
        // char[] encoded = new char[5];
        // System.out.println(value);

        // for (int index = 4; index >= 0; index--) {
        // encoded[index] = (char) ((value % 85) + c_firstCharacter);
        // value /= 85;
        // int y = encoded[index];
        // System.out.println(y - c_firstCharacter);

        // }

    }
}
