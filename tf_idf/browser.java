/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

/**
 *
 * @author Gowtham
 */
import javax.swing.*;
import java.io.*;
import java.awt.*;

public class browser {

  public static void main_(String args) {

    // get the first URL
    String initialPage = "http://www.google.co.in/";
    //if (args.length > 0) initialPage = args[0];

    // set up the editor pane
    JEditorPane jep = new JEditorPane();
    jep.setEditable(false);

    try {
      jep.setPage(initialPage);
    }
    catch (IOException ex) {
      System.err.println("\nNo NET Connection...\n");
      System.err.println(ex);
      System.exit(-1);
    }

    // set up the window
    JScrollPane scrollPane = new JScrollPane(jep);
    JFrame f = new JFrame("Simple Web Browser");
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.setContentPane(scrollPane);
    f.setSize(512, 342);
    EventQueue.invokeLater(new FrameShower(f));

  }

  // Helps avoid a really obscure deadlock condition.
  // See http://java.sun.com/developer/JDCTechTips/2003/tt1208.html#1
  private static class FrameShower implements Runnable {

    private final Frame frame;

    FrameShower(Frame frame) {
      this.frame = frame;
    }

    public void run() {
     frame.setVisible(true);
    }
  }
}


