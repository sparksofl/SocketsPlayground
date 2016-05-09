import java.net.*;
import java.io.*;

public class Client extends Helper {
  public static void main(String[] ar) {
    try {
      InetAddress ipAddress = InetAddress.getByName(ADDRESS); // создаем объект который отображает вышеописанный IP-адрес.
      System.out.println("Searching for a socket with IP address " + ADDRESS + " and port " + SOCKET_PORT + "...");
      Socket socket = new Socket(ipAddress, SOCKET_PORT); // создаем сокет используя IP-адрес и порт сервера.
      System.out.println("Connection established");
      System.out.println();

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      // Создаем поток для чтения с клавиатуры.
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = null;

      System.out.print("> ");
      startChat(in, out);
    } catch (Exception x) { x.printStackTrace(); }
  }
}
