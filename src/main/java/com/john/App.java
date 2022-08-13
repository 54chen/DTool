package com.john;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import com.github.fzakaria.ascii85.Ascii85;
import com.john.common.DecryptUtil;
import com.john.common.FileUtil;
import com.john.common.WinRegistry;
 
/**
 * DTool APP v0.1
 *
 */
public class App {

    final static String EXT = ".gg";
    final static String INSTRUCT = "InStrucT1onss.txt";
    final static String ORIGINALPATH = "C:\\Windows\\Temp\\OriginalPath.txt";
    final static String ENCPATH = "C:\\Windows\\Temp\\EncPath.txt";
    final static String REG_PATH = "HKEY_CURRENT_USER\\Software\\Microsoft\\Services";
    final static String KEY_NAME = "KeyValue";
    final static String IV_NAME = "IVvalue";
    final static String ALGORITHM = "AES/CBC/NoPadding"; //ZeroBytePadding
  

    public static void main(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, GeneralSecurityException {
        TextIO textIO = TextIoFactory.getTextIO();

        showMsg(textIO, "DTool APP v0.1");
 
        // byte[] AESKey = WinRegistry.getReg(REG_PATH, KEY_NAME);
        // showMsg(textIO,"Windows Distribution = " + AESKey);

        // byte[] IV = WinRegistry.getReg(REG_PATH, IV_NAME);
        // showMsg(textIO,"Windows Distribution = " + IV);

        List<String> from = FileUtil.readFileByLines(ENCPATH);
        List<String> to = FileUtil.readFileByLines(ORIGINALPATH);

        int i = 0;
        for (String string : from) {
            showMsg(textIO, string);
            showMsg(textIO, to.get(i));
            if (!string.startsWith(to.get(i).substring(0, to.get(i).length()-4))) {
                showMsg(textIO, string+" (from) file is not match (to) "+ to.get(i));
                i++;
                continue;
            }
            i++;
            byte[] content = FileUtil.readFile(string);
            if (content == null) {
                showMsg(textIO, string+" (from) is not exsited!");
                continue;
            }
            // decrypt(AESKey, IV, reverse(content));
            //byte[] orig = DecryptUtil.DecodeStr(new String(content));
            byte[] origContent = Ascii85.decode(new String(content));
            FileUtil.deleteFeile(to.get(i));
            FileUtil.saveFile(to.get(i), origContent);
        }

        showMsg(textIO, "Bye!");
    }
 
    private static void showMsg(TextIO textIO, String msg) {
        textIO.getTextTerminal().println("[log]" + msg);
    }

    private static String decrypt(byte KEY[], byte IV[], byte ciphertextBytes[])
            throws GeneralSecurityException {
        /*
         * if (key.length != 32 || key.length != 48 || key.length != 64) { throw
         * new IllegalArgumentException("Invalid key size."); }
         */

        IvParameterSpec iv = new IvParameterSpec(IV, 0, 16);

        SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(ciphertextBytes);

        // Remove zero bytes at the end.
        int lastLength = original.length;
        for (int i = original.length - 1; i > original.length - 16; i--) {
            if (original[i] == (byte) 0) {
                lastLength--;
            } else {
                break;
            }
        }

        return new String(original, 0, lastLength);

    }
}
