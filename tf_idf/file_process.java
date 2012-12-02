/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

/**
 *
 * @author Gowtham
 */
public class file_process
{
    public static String process(String file_raw)
    {
        int j = 0;
        char[]buf = new char[file_raw.length()];
        for(int i = 0; i < file_raw.length() && j < buf.length; /*no inc*/)
        {
            /*Takes Care of SCRIPT tag*/
            if(((Character)file_raw.charAt(i)).equals('<') &&
                    i+1 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+1)).toString().equalsIgnoreCase("s")
                    && i+2 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+2)).toString().equalsIgnoreCase("c")
                    && i+3 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+3)).toString().equalsIgnoreCase("r")
                    && i+4 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+4)).toString().equalsIgnoreCase("i")
                    && i+5 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+5)).toString().equalsIgnoreCase("p")
                    && i+6 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+6)).toString().equalsIgnoreCase("t"))
            {
                i+=7;
                while(!(((Character)file_raw.charAt(i)).equals('/')
                    && i+1 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+1)).toString().equalsIgnoreCase("s")
                    && i+2 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+2)).toString().equalsIgnoreCase("c")
                    && i+3 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+3)).toString().equalsIgnoreCase("r")
                    && i+4 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+4)).toString().equalsIgnoreCase("i")
                    && i+5 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+5)).toString().equalsIgnoreCase("p")
                    && i+6 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+6)).toString().equalsIgnoreCase("t")
                    && i+7 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+7)).equals('>')))
                    i++;
                i+=8;
                buf[j++] = ' ';
            }
            /*takes care of style tag*/
            if(((Character)file_raw.charAt(i)).equals('<') &&
                    i+1 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+1)).toString().equalsIgnoreCase("s")
                    && i+2 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+2)).toString().equalsIgnoreCase("t")
                    && i+3 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+3)).toString().equalsIgnoreCase("y")
                    && i+4 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+4)).toString().equalsIgnoreCase("l")
                    && i+5 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+5)).toString().equalsIgnoreCase("e")
                    )
            {
                i+=6;
                while(!(((Character)file_raw.charAt(i)).equals('/')
                    && i+1 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+1)).toString().equalsIgnoreCase("s")
                    && i+2 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+2)).toString().equalsIgnoreCase("t")
                    && i+3 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+3)).toString().equalsIgnoreCase("y")
                    && i+4 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+4)).toString().equalsIgnoreCase("l")
                    && i+5 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+5)).toString().equalsIgnoreCase("e")
                    && i+6 < file_raw.length() &&
                    ((Character)file_raw.charAt(i+6)).equals('>')))
                    i++;
                i+=7;
                buf[j++] = ' ';
            }
            /*takes care of comments & scripts in html*/
            if(((Character)file_raw.charAt(i)).equals('<') &&
                    i+1 < file_raw.length() &&
               ((Character)file_raw.charAt(i+1)).equals('!') &&
                    i+2 < file_raw.length() &&
               ((Character)file_raw.charAt(i+2)).equals('-') &&
                    i+3 < file_raw.length() &&
               ((Character)file_raw.charAt(i+3)).equals('-'))
            {
                i+=4;
                while(!((((Character)file_raw.charAt(i)).equals('-'))
                        && i+1 < file_raw.length()
                     && ((Character)file_raw.charAt(i+1)).equals('-')
                        && i+2 < file_raw.length()
                     && ((Character)file_raw.charAt(i+2)).equals('>')))
                    i++;
                i+=3;
                buf[j++] = ' ';
            }
            /*takes care of nested tags*/
            else if(((Character)file_raw.charAt(i)).equals('<'))
            {
                i++;
                int count = 1;
                while(!((Integer)count).equals(0) && i < file_raw.length())
                {
                    if(((Character)file_raw.charAt(i)).equals('<'))
                        count++;
                    if(((Character)file_raw.charAt(i)).equals('>'))
                        count--;
                    i++;
                }
                buf[j++] = ' ';
            }
            /*copies text which'l b displayed in page*/
            else
            {
                buf[j++] = file_raw.charAt(i);
                i++;
            }
        }
        buf[j] = '\0';
        int buf_length = 0;
        for(int i = 0; !((Character)buf[i]).equals('\0'); i++)
            buf_length++;
        String fil = new String(buf, 0, buf_length);
        return fil;
    }
}
/*
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.MutableAttributeSet;

public class file_process {
  private file_process() {}

  public static List<String> extractText(Reader reader) throws IOException {
    final ArrayList<String> list = new ArrayList<String>();

    ParserDelegator parserDelegator = new ParserDelegator();
    ParserCallback parserCallback = new ParserCallback() {
      public void handleText(final char[] data, final int pos) {
        list.add(new String(data));
      }
      public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) { }
      public void handleEndTag(Tag t, final int pos) {  }
      public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) { }
      public void handleComment(final char[] data, final int pos) { }
      public void handleError(final java.lang.String errMsg, final int pos) { }
    };
    parserDelegator.parse(reader, parserCallback, true);
    return list;
  }

  public static String process(String args) throws Exception{
    FileReader reader = new FileReader(args);
    List<String> lines = file_process.extractText(reader);
    String retStr = "";
    for (String line : lines) {
      System.out.println(line);
      retStr += line;
    }
    return retStr;
  }
}*/
