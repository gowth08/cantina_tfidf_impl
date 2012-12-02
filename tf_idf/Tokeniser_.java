/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

import java.util.regex.*;

/**
 *
 * @author Gowtham
 */
public class Tokeniser_
{
    public Tokeniser_()
    {
        //add code
    }
    public static String[] AL2A(Object[] a)
    {
        String[] arr = new String[a.length];
        for(int i = 0;i < arr.length;i++)
            arr[i] = (String) a[i];
        return arr;
    }
    public String[] partition(String inp)
    {
        Pattern pat = Pattern.compile("([ \\t{}():;.&== ,\n-])");
        inp = inp.toLowerCase();
        String []tokens = pat.split(inp);
        java.util.ArrayList alist = new java.util.ArrayList();
        for(int i = 0;i < tokens.length;i++)
        {
            Matcher mat = pat.matcher(tokens[i]);
            if(mat.groupCount() >= 0 && tokens[i].trim().length() > 0 &&
              !StopWord.isStopWord(tokens[i]) && tokens[i].length() >=3
              && tokens[i].length() < 15)
                    alist.add(tokens[i]);
        }
        Object []a = new Object[alist.size()];
        alist.toArray(a);
        return AL2A(a);
    }
}
