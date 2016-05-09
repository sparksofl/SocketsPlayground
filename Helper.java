import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Helper {
  public final static String ADDRESS = "127.0.0.1";
  public final static int SOCKET_PORT = 8080; // порт, к которому привязывается сервер
  public final static String FILE_PATH = System.getProperty("user.dir") + "/";
  public final static int FILE_SIZE = 3750;
  public static String line;

  public static void main(String[] args) throws IOException {}

  public static synchronized void startChat(DataInputStream in, DataOutputStream out) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    line = null;

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

      while (in.available() != 0 && !"".equals(line = in.readUTF())) {
        if (line.startsWith("sending")) {
          System.out.println(line);
          saveFile(Integer.parseInt(line.split(" ")[1]), line.split(" ")[2], in);
        } else if (line != "" || line != null) {
          System.out.println("\u001B[1mResponse: \u001B[0m " + line);
          System.out.print("> ");
        }
      }
    }
  }

  public static synchronized void sendFile(DataOutputStream out, String fileName) {
    try {
      FileInputStream fis = new FileInputStream(FILE_PATH + fileName);
      File f = new File(FILE_PATH + fileName);
      byte[] buffer = new byte[FILE_SIZE];
      out.writeUTF("sending " + f.length() + " " + fileName);
      int i = 0;
      while (fis.read(buffer) > 0) {
        System.out.println("\033[3mSending: " + (i * (float) FILE_SIZE / f.length()) * 100.0 + " %");
        out.write(buffer);
        i++;
      }
      System.out.println("\u001B[32mSending is finished\u001B[0m\033[0m");
      fis.close();
    } catch(IOException e) {
      System.out.println("\u001B[31m\033[3mSending failed: " + e.getMessage() + "\u001B[0m\033[0m");
    }
    System.out.print("> ");
  }

  private static synchronized void saveFile(int size, String name, DataInputStream in) throws IOException {
    String fileName = generateFilename(name.split("\\.")[0], name.split("\\.")[1]);

    try {
      FileOutputStream fos = new FileOutputStream(FILE_PATH + fileName);
      byte[] buffer = new byte[FILE_SIZE];

      int filesize = size; // Send file size in separate msg
      int read = 0;
      int totalRead = 0;
      int remaining = filesize;
      while ((read = in.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
        totalRead += read;
        remaining -= read;
        System.out.println("\033[3mread " + totalRead + " bytes.\033[0m");
        fos.write(buffer, 0, read);
      }
      System.out.println("\u001B[32m\033[mFile " + fileName
          + " downloaded (" + filesize + " bytes read)\u001B[0m\033[0m");
      fos.close();
    } catch(IOException e) {
      System.out.println("\u001B[31m\033[3mAn error occurred while downloading the file: " + e.getMessage() + "\u001B[0m\033[0m");
    }
    System.out.print("> ");
  }

  public static String filterString(String s) {
    return s.replaceAll("[^a-zA-Z0-9 ,\'\"?!\\.:;()]","*");
  }

  public static String generateFilename(String name, String format) {
    String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    return name + timestamp + "." + format;
  }
}
