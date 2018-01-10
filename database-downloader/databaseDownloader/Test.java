package databaseDownloader;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class Test {
  public static void main(String s[]) {
    Desktop desktop = null;
    // on Windows, retrieve the path of the "Program Files" folder
    File file = new File(System.getenv("programfiles"));
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
    }
    try {
      desktop.open(file);
    }
    catch (IOException e){  }
  }
}