import java.net.*;
import java.io.*;

public class Helper {
  public final static int SOCKET_PORT = 8080; // порт, к которому привязывается сервер
  public final static String FILE_PATH = System.getProperty("user.dir") + "/";
  public final static int FILE_SIZE = 6022386;

  public static void sendFile(DataOutputStream out, String fileName) throws IOException {
    FileInputStream fis = new FileInputStream(FILE_PATH + fileName);
    File f = new File(FILE_PATH + fileName);
    byte[] buffer = new byte[FILE_SIZE];
    out.writeUTF("sending " + f.length() + " " + fileName); // отсылаем введенную строку текста серверу.
    int i = 0;
    while (fis.read(buffer) > 0) {
        out.write(buffer);
        System.out.println("Sending: " + (i * (float) FILE_SIZE / f.length()) * 100.0 + " %");
        i++;
    }
    System.out.println("Sending is finished");
    fis.close();
  }

  public static String filterString(String s) {
    return s.replaceAll("[^a-zA-Z0-9 ,\\.:;()]","*");
  }
}
