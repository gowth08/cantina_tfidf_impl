/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

import java.io.*;

/**
 *
 * @author Gowtham
 */
public class Start_tfidf{
    public static String[]query;
    public static String[][]terms;//to store the first 5 terms for each doc
    //public static float[][]weights;//to store the first 5 weight for terms for each doc
    public  static void main(String[]args)throws Exception
    {
        java.lang.StringIndexOutOfBoundsException sb;
        System.out.println("\t\t\t\tTF IDF Implementation\n");
        new StopWord();//test for StopWord
        System.out.println("whencesoever is in Hahtable :" +
                StopWord.isStopWord("whencesoever"));
        /*File 1*/
        /*
        File f = new File("C:\\Documents and Settings\\Gowtham\\Desktop\\" +
                "tfidf\\lisp.htm");
        FileInputStream fis = new FileInputStream(f);
        byte[]buf = new byte[(int)fis.getChannel().size()];
        fis.read(buf);
        String s = new String(buf, 0, buf.length);
        String processed_s = file_process.file_remove_tags(s);*/
        /*File 2*/

        File f1 = new File(args[0]);
        FileInputStream fis1 = new FileInputStream(f1);
        byte[]buf1 = new byte[(int)fis1.getChannel().size()];
        fis1.read(buf1);
        String s1 = new String(buf1, 0, buf1.length);
        String processed_s1 = file_process.process(s1);
        /*File 3*/
        /*
        File f2 = new File("C:\\Documents and Settings\\Gowtham\\Desktop" +
                "\\tfidf\\ca.htm");
        FileInputStream fis2 = new FileInputStream(f2);
        byte[]buf2 = new byte[(int)fis2.getChannel().size()];
        fis2.read(buf2);
        String s2 = new String(buf2, 0, buf2.length);
        String processed_s2 = file_process.file_remove_tags(s2);
        System.out.println("\n\nProcessed Doc:\n\n"+processed_s+"\n\n\n");*/
        System.out.println("\n\nProcessed Doc:\n\n"+processed_s1+"\n\n\n");
        //System.out.println("\n\nProcessed Doc:\n\n"+processed_s2+"\n\n\n");
        String[] docs=new String[] {
                                           /*processed_s,
                                           processed_s2,*/
                                           processed_s1
			};
        terms = new String[docs.length][5];
        query = new String[docs.length];
        for(int i =0; i < docs.length; i++)
            query[i] = new String("");
        //weights = new float[docs.length][5];
        /*Consider only the first 5 terms*/
        Tokeniser_ t =new Tokeniser_();//test for Tokeniser_
        String [][]arr = new String[docs.length][];
        for(int i= 0; i < arr.length; i++)
                arr[i] = t.partition(docs[i]);
        System.out.println("\nTokeniser Class");
        for(int i = 0; i < arr.length;i++)
        {
            System.out.println("Doc"+ (i+1) + " :");
            for(int j = 0; j < arr[i].length; j++)
                System.out.print("  "+arr[i][j]);
            System.out.println();
        }
        /*Test for NGram Generator*/
        float similarity = Ngrams.GetQuadGramSimilarity("text", "test");
        System.out.println("\nSimilarity b/n \"text\" and \"test\" is :"+
                similarity);
        System.out.println();
        tf_idf.browser.main_(max_5terms(docs));
    }
    public static String max_5terms(String[]docs)
    {
        /*Test for TFIDF_Measure*/
        TFIDF_Measure tfidf = new TFIDF_Measure(docs);
        String result = "";
        System.out.println("\nTERMS WITH MAX_TFIDF SCORE IN A GIVEN DOC:");
        for(int i = 0; i < docs.length; i++)
        {
            terms[i] = tfidf.doc_max_terms(i);
        }

        for(int i = 0; i < docs.length; i++)
        {
            System.out.println("\n\nQuery for Document :"+(i+1));
            for(int j = 0; j < terms[i].length-1; j++)
            {
                    query[i]+=terms[i][j]+"+";
                System.out.println(terms[i][j]);
            }
            System.out.println(terms[i][terms[i].length-1]);
                query[i]+=terms[i][terms[i].length-1];
            result += query[i];
        }
        System.out.println("\n\nTotal Query String is :"+result);
        return result;
    }
}