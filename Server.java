import java.net.*;
import java.io.*;

public class Server extends Helper {
  public static void main(String[] ar) throws IOException {
    ServerSocket ss = null;
    try {
      ss = new ServerSocket(SOCKET_PORT); // создаем сокет сервера и привязываем его к вышеуказанному порту
      System.out.println("Waiting for a client...");
      Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
      System.out.println("Got a client!");
      System.out.println();

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      String line = null;

      System.out.print("> ");
      startChat(in, out);
    } catch(Exception x) {
      x.printStackTrace();
    } finally {
      if (ss != null) {
        ss.close();
      }
    }
  }
}
