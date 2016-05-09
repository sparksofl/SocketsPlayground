import java.net.*;
import java.io.*;

public class Client extends Helper {
  public static void main(String[] ar) {
    String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа.
                                  // Здесь указан адрес того самого компьютера где будет исполняться и клиент.

    try {
      InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
      System.out.println("Searching for a socket with IP address " + address + " and port " + SOCKET_PORT + "...");
      Socket socket = new Socket(ipAddress, SOCKET_PORT); // создаем сокет используя IP-адрес и порт сервера.
      System.out.println("Connection established");

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      // Создаем поток для чтения с клавиатуры.
      BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
      String line = null;
      System.out.print("\u001B[1m You: \u001B[0m ");

      while (true) {
        line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.

        if (line.startsWith("send")) {
          String fileName = line.split(" ")[1];
          sendFile(out, fileName);
        } else {
          out.writeUTF(filterString(line)); // отсылаем введенную строку текста серверу.
        }

        out.flush(); // заставляем поток закончить передачу данных.

        System.out.println("Waiting for the server response...");
        System.out.println();

        line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        System.out.println("\u001B[1m Server: \u001B[0m " + line);
        System.out.print("\u001B[1m You: \u001B[0m ");
      }
    } catch (Exception x) {
        x.printStackTrace();
    }
  }
}
