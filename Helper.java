import java.net.*;
import java.io.*;

public class Helper {
  public final static String ADDRESS = "127.0.0.1";
  public final static int SOCKET_PORT = 8080; // порт, к которому привязывается сервер
  public final static String FILE_PATH = System.getProperty("user.dir") + "/";
  public final static int FILE_SIZE = 6022386;

  public static void startChat(DataInputStream in, DataOutputStream out) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String line = null;

    while (true) {
      while (reader.ready()) {
        System.out.print("> ");
        line = reader.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.

        if (line.startsWith("send")) {
          String fileName = line.split(" ")[1];
          sendFile(out, fileName);
        } else {
          out.writeUTF(filterString(line)); // отсылаем введенную строку текста серверу.
        }
      }

      while (in.available() != 0 && line != "") {
        line = in.readUTF();
        System.out.println("\u001B[1mResponse: \u001B[0m " + line);
        System.out.print("> ");
      }
    }
  }

  public static void sendFile(DataOutputStream out, String fileName) {
    try {
      FileInputStream fis = new FileInputStream(FILE_PATH + fileName);
      File f = new File(FILE_PATH + fileName);
      byte[] buffer = new byte[FILE_SIZE];
      out.writeUTF("sending " + f.length() + " " + fileName);
      int i = 0;
      while (fis.read(buffer) >= 0) {
        System.out.println("\033[3mSending: " + (i * (float) FILE_SIZE / f.length()) * 100.0 + " %");
        i++;
      }
      System.out.println("\u001B[32mSending is finished\u001B[0m\033[0m");
      fis.close();
    } catch(IOException e) {
      System.out.println("\u001B[31m\033[3mSending failed: " + e.getMessage() + "\u001B[0m\033[0m");
    }
    System.out.print("> ");
  }

  public static String filterString(String s) {
    return s.replaceAll("[^a-zA-Z0-9 ,\'\"?!\\.:;()]","*");
  }
}
