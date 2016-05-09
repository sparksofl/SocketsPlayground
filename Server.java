import java.net.*;
import java.io.*;

public class Server extends Helper {
  public static void main(String[] ar)    {
    try {
      ServerSocket ss = new ServerSocket(SOCKET_PORT); // создаем сокет сервера и привязываем его к вышеуказанному порту
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
      while(true) {
        line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
        System.out.println("\u001B[1m Client: \u001B[0m " + line);
        System.out.print("\u001B[1m You: \u001B[0m ");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        line = reader.readLine();

        if (line.startsWith("send")) {
          String fileName = line.split(" ")[1];
          sendFile(out, fileName);
        } else {
          out.writeUTF(filterString(line)); // отсылаем введенную строку текста серверу.
        }

        out.flush(); // заставляем поток закончить передачу данных.
        System.out.println("Waiting for the client response...");
        System.out.println();
      }
    } catch(Exception x) { x.printStackTrace(); }
  }
}
