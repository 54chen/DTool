package com.john;

import java.io.IOException;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import com.github.fzakaria.ascii85.Ascii85;
import com.john.common.FileUtil;

/**
 * DTool APP v0.1
 *
 */
public class App {

    final static String EXT = ".gg";
    final static String INSTRUCT = "InStrucT1onss.txt";
    final static String ORIGINALPATH = "C:\\Windows\\Temp\\OriginalPath.txt";
    final static String ENCPATH = "C:\\Windows\\Temp\\EncPath.txt";

    public static void main(String[] args) throws IOException {
        TextIO textIO = TextIoFactory.getTextIO();

        showMsg(textIO, "DTool APP v0.1");

        List<String> from = FileUtil.readFileByLines(ENCPATH);
        List<String> to = FileUtil.readFileByLines(ORIGINALPATH);

        int i = 0;
        for (String string : from) {
            showMsg(textIO, string);
            showMsg(textIO, to.get(i));
            if (!string.startsWith(to.get(i).substring(0, to.get(i).length() - 4))) {
                showMsg(textIO, string + " (from) file is not match (to) " + to.get(i));
                i++;
                continue;
            }
            
            byte[] content = FileUtil.readFile(string);
            if (content == null) {
                showMsg(textIO, string + " (from) is not exsited!");
                i++;
                continue;
            }
            byte[] origContent = Ascii85.decode(new String(content));
            FileUtil.deleteFeile(to.get(i));
            FileUtil.saveFile(to.get(i), origContent);

            i++;
        }

        showMsg(textIO, "Bye!");
    }

    private static void showMsg(TextIO textIO, String msg) {
        textIO.getTextTerminal().println("[log]" + msg);
    }

}
