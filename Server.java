import java.net.*;
import java.io.*;

public class Server extends Helper {
  public static void main(String[] ar)    {
    int port = 6666; // случайный порт (может быть любое число от 1025 до 65535)
      try {
        ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
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
            FileInputStream fis = new FileInputStream(Server.class.getProtectionDomain().getCodeSource().getLocation()+"/"+fileName);
            File f = new File(Server.class.getProtectionDomain().getCodeSource().getLocation()+"/"+fileName);
            byte[] buffer = new byte[4096];
            out.writeUTF("sending " + f.length() + " " + fileName); // отсылаем введенную строку текста серверу.
            int i = 0;
            while (fis.read(buffer) > 0) {
                out.write(buffer);
                System.out.println("Sending: " + (i * 4096.0 / f.length()) * 100.0 + " %");
                i++;
            }
            System.out.println("Sending finished");
            //out.close();
            fis.close();
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
