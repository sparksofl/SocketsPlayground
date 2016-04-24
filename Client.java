import java.net.*;
import java.io.*;

public class Client extends Helper {
  public static void main(String[] ar) {
    int serverPort = 6666; // здесь обязательно нужно указать порт к которому привязывается сервер.
    String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа.
                                  // Здесь указан адрес того самого компьютера где будет исполняться и клиент.

    try {
        InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
        System.out.println("Searching for a socket with IP address " + address + " and port " + serverPort + "...");
        Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
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
            System.out.println("Waiting for the server response...");
            System.out.println();

            if (line.startsWith("send")) {
              String fileName = line.split(" ")[1];
              FileInputStream fis = new FileInputStream("/home/mary/en.yml");
              File f = new File("/home/mary/en.yml");
              byte[] buffer = new byte[4096];
              out.writeUTF("sending " + f.length() + " " + fileName); // отсылаем введенную строку текста серверу.
              int i = 0;
              while (fis.read(buffer) > 0) {
                  out.write(buffer);
                  System.out.println("Sending: " + (i * 4096.0 / f.length()) * 100.0);
                  i++;
              }
              System.out.println("Sending finished");
              //out.close();
              fis.close();
            } else {
              out.writeUTF(filterString(line)); // отсылаем введенную строку текста серверу.
            }
            out.flush(); // заставляем поток закончить передачу данных.
            line = in.readUTF(); // ждем пока сервер отошлет строку текста.
            System.out.println("\u001B[1m Server: \u001B[0m " + line);
            System.out.print("\u001B[1m You: \u001B[0m ");
        }
    } catch (Exception x) {
        x.printStackTrace();
    }
  }
}
