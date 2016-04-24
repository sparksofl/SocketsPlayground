import java.net.*;
import java.io.*;

public class Helper {
  public static String filterString(String s) {
    return s.replaceAll("[^a-zA-Z0-9]","*");
  }
}
