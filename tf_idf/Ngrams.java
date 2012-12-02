/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

/**
 *
 * @author Gowtham
 */
public class Ngrams
{
    public Ngrams()
    {
        //add code
    }
    public static String[] GenNGrams(String txt, int gramlen)
    {
        if( txt == null || txt.length() == 0)
            return null;//null return if no txt
        java.util.ArrayList grams_alist = new java.util.ArrayList();
        if(txt.length() < gramlen)
        {
            String gram;
            for(int i = 0; i <= txt.length(); i++)
            {
                gram = txt.substring(0, i);
                if(grams_alist.indexOf(gram) == -1)
                    grams_alist.add(gram);
            }
            gram = txt.substring(txt.length()-1,txt.length());
            if(grams_alist.indexOf(gram) == -1)
                    grams_alist.add(gram);
        }
        else
        {
            for(int i = 1; i <= gramlen-1; i++)
            {
                String gram = txt.substring(0, i);
                if(grams_alist.indexOf(gram) == -1)
                    grams_alist.add(gram);
            }
            for(int i = 0; i < txt.length()-gramlen + 1; i++)
            {
                String gram = txt.substring(i, i+gramlen);
                if(grams_alist.indexOf(gram) == -1)
                    grams_alist.add(gram);
            }
            for(int i = txt.length()-gramlen + 1; i < txt.length(); i++)
            {
                String gram = txt.substring(i);
                if(grams_alist.indexOf(gram) == -1)
                    grams_alist.add(gram);
            }
        }
        Object a[] = new Object[grams_alist.size()];
        grams_alist.toArray(a);
        return Tokeniser_.AL2A(a);
    }
    public static float GetNGramSimilarity(String txt1, String txt2, int gramlen)
    {
        if((Object)txt1 == null || (Object)txt2 == null || txt1.length() == 0 ||
                txt2.length() == 0)
            return 0.0f;
        String []grams1 = GenNGrams(txt1, gramlen);
        String []grams2 = GenNGrams(txt2, gramlen);
        //print(grams1, grams2);
        int count = 0;
        for(int i =0; i < grams1.length; i++)
            for(int j = 0; j < grams2.length; j++)
            {
                if(!grams1[i].equals(grams2[j]))//if not equal continue
                    continue;
                count++;//similarity count
                break;//proceed to next j
            }
        float sim=(2.0f * (float) count) / (float) (grams1.length + grams2.length);
        return sim;
    }
        public static float GetBigramSimilarity(String text1, String text2)
		{
			return GetNGramSimilarity(text1, text2, 2);
		}
		public static float GetTrigramSimilarity(String text1, String text2)
		{
			return GetNGramSimilarity(text1, text2, 3);
		}
		public static float GetQuadGramSimilarity(String text1, String text2)
		{
			return GetNGramSimilarity(text1, text2, 4);
		}
    public static void print(String[] a, String[] a1)
    {
        System.out.println("\nNGRAMS Check"+"\nText 1");
        for(int i = 0; i < a.length; i++)
            System.out.print("  "+a[i]);
        System.out.println("\nNGRAMS Check"+"\nText 2");
        for(int i = 0; i < a1.length; i++)
            System.out.print("  "+a1[i]);
        System.out.println();
    }
}
