/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tf_idf;

/**
 *
 * @author Gowtham
 */
public class TFIDF_Measure
{
    private java.util.Dictionary words_index = new java.util.Hashtable();
    private String[]docs;
    /*to store documents*/
    private String[][]ngram_doc;
    /*to store Tokens obtained from Tokeniser_ */
    private int num_docs=0,num_terms=0;/*count for terms and docs*/
    private java.util.ArrayList terms_alist;
    /*Arraylist to store terms obtained after StopWord and Tokeniser_
     Step*/
    private int[][]term_freq;
    /*int [num_terms][num_docs]*/
    private float[][]term_weight;
    /*same as term_freq but used to calculate weight*/
    private int[]max_term_freq,doc_freq;
    /*int[num_docs]-->max_term_freq, int[num_terms]-->doc_freq*/
    public TFIDF_Measure(String[]docs)
    {
        this.docs = docs;
        this.num_docs = docs.length;
        MyConst();
    }
    public void print()
    {
        System.out.println("\t\t\tStats\n");
        System.out.println("TermS with their weight(tfidf)are :\n");
        for(int i =0; i < num_terms; i++)
        {
            System.out.println("\nTerm "+(i+1)+": "+terms_alist.get(i)
                    +"\nTerm Weight in doc ");
            for(int j = 0; j < num_docs; j++)
                System.out.print(j+": "+term_weight[i][j]+"\n");
        }
        /*System.out.println("\nNGRAM for Each doc:\n");
        for(int i = 0; i < num_docs; i++)
        {
            System.out.println("Doc :"+ (i+1));
            for(int j = 0; j < ngram_doc[i].length; j++)
                System.out.println(ngram_doc[i][j]);
            System.out.println();
        }*/
    }
    private void Gen_NGram_Text()
    {
        String[][]temp = new String[num_docs][];
        for(int i = 0; i < num_docs; i++)
        {
            temp[i] = Ngrams.GenNGrams(docs[i], 4);
            java.util.ArrayList alist = new java.util.ArrayList();
            StringBuffer sb = new StringBuffer(" ");
            for(int j = 0; j < temp[i].length; j++)
            {
                temp[i][j] = temp[i][j].trim();
                /*remove leading and trailing spaces*/
                if(temp[i][j].length() > 0 && !temp[i][j].contains(sb)
                && !StopWord.isStopWord(temp[i][j]) && !alist.contains(temp[i][j]))
                       alist.add(temp[i][j]);
                /*Check and add to alist*/
            }
            Object[]a = new Object[alist.size()];
            alist.toArray(a);
            ngram_doc[i] = Tokeniser_.AL2A(a);
        }
    }
    private java.util.ArrayList Gen_Terms(String[]docs)
    {
        java.util.ArrayList uniques_alist = new java.util.ArrayList();
        ngram_doc = new String[num_docs][];//for each document
        for(int i = 0; i < docs.length; i++)
        {
            Tokeniser_ tk = new Tokeniser_();
            String[]tokens = tk.partition(docs[i]);
            /*tokens contain Tokenised Words Based on pattern and matchCount*/
            for(int j = 0; j < tokens.length; j++)
                if(!uniques_alist.contains(tokens[j]))
                    uniques_alist.add(tokens[j]);
            /*if Tokens[j] for a docs[i] is not in list then add it to ArrayList*/
        }
        return uniques_alist;
    }
    private Object add(java.util.Dictionary dict, Object key, Object value)
    {
        Object a = dict.put(key, value);
        return a;
    }
    private void MyConst()
    {
        terms_alist = Gen_Terms(docs);//store Tokenised Terms in terms_alist
        num_terms = terms_alist.size();
        Object a[] = new Object[num_terms];
        terms_alist.toArray(a);
        String a1[] = Tokeniser_.AL2A(a);
        /*Initialization */
        max_term_freq = new int[this.num_docs];
        /*for each document find the term which has max_freq*/
        doc_freq = new int[this.num_terms];
        /*for each term calc the freq of occurence of term in each doc and
         store it*/
        term_freq = new int[num_terms][];
        /*each term's freq(tf) in each doc*/
        term_weight = new float[num_terms][];
        /*each terms Computed Weight*/
        for(int i = 0; i < num_terms; i++)
        {
            term_weight[i] = new float[num_docs];
            term_freq[i] = new int[num_docs];
            add(words_index, a1[i], i);//add all terms to words_index
        }
        Gen_Term_Freq();
        Gen_Term_Weight();
        Gen_NGram_Text();
        print();
    }
    private void Gen_Term_Freq()
    {
        for(int i = 0; i < num_docs; i++)
        {
            String cur_doc = docs[i];
            /*Get the Freq of each word in cur_doc*/
            java.util.Dictionary dict = Get_Word_Freq(cur_doc);
            java.util.Enumeration enum_key = dict.keys();
            //enumeration to string distinct_word
            max_term_freq[i] = Integer.MIN_VALUE;//initialize to update later
            while(enum_key.hasMoreElements())
            {
                String key = enum_key.nextElement().toString();
                int value = ((Integer)dict.get(key)).intValue();
                int term_index = ((Integer)Get_Term_Index(key)).intValue();
                term_freq[term_index][i] = ((Integer)value).intValue();
                doc_freq[term_index]++;
                if(value > max_term_freq[i])
                    max_term_freq[i] = value;//update
            }
        }
    }
    private void Gen_Term_Weight()
    {
        for(int i = 0; i < num_terms; i++)
            for(int j = 0; j < num_docs; j++)
                term_weight[i][j] = Compute_Term_Weight(i, j);
    }
    private float Compute_Term_Weight(int term, int doc)
    {
        float tf = Get_Term_Freq(term, doc);
        float idf = Get_Inv_Doc_Freq(term);
        return tf * idf;
    }
    private float Get_Term_Freq(int term, int doc)
    {
        int freq = term_freq[term][doc];
        int maxfreq = max_term_freq[doc];
        return ((float)freq/(float)maxfreq);
    }
    private float Get_Inv_Doc_Freq(int term)
    {
        int docfreq = doc_freq[term];
        if(num_docs != 1)
            return log_((float)num_docs/(float)docfreq);
        return (float)num_docs/(float)docfreq;
    }
    private float log_(float inp)
    {
        return (float)Math.log(inp);
    }
    private Object Get_Term_Index(String key)
    {
        Object indx = words_index.get(key);
        if(indx == null)
            return -1;
        return indx;
    }
    private String[] string_sort(String[]a)
    {
        /*bubble sorting*/
        for(int i = 0; i < a.length; i++)
			for(int j = i+1; j < a.length; j++)
				if(a[i].compareToIgnoreCase(a[j]) >= 1)
						{
							String temp=a[i];
							a[i]=a[j];
							a[j]=temp;
						}
        return a;
    }
    private java.util.Dictionary Get_Word_Freq(String inp)
    {
        String small_inp = inp.toLowerCase();
        Tokeniser_ tk = new Tokeniser_();
        String[]tk_words = tk.partition(small_inp);
        tk_words = string_sort(tk_words);//sort tk_words
        /*alternative for sorting-->java.util.Arrays.sort(tk_words);*/
        String[] distinct_words = Get_Distinct_Words(tk_words);
        java.util.Dictionary res = new java.util.Hashtable();
        for(int i = 0; i < distinct_words.length; i++)
        {
            Object temp;
            temp = Count_Words(distinct_words[i], tk_words);
            res.put(distinct_words[i], temp);
            /*key-->distinct_words[i],Value-->Count*/
        }
        return res;
        /*returns a dictionary with key and value as mentioned above*/
    }
    private String[] Get_Distinct_Words(String[] tk_words)
    {
        if(tk_words == null)
            return new String[0];
        else
        {
            java.util.ArrayList alist =new java.util.ArrayList();
            /*Add Distinct Words to ArrayList*/
            for(int i = 0; i < tk_words.length; i++)
                if(!alist.contains(tk_words[i]))
                    alist.add(tk_words[i]);
            Object[]a = new Object[alist.size()];
            alist.toArray(a);
            return Tokeniser_.AL2A(a);//return array
        }
    }
    private Object Count_Words(String distinct_word, String[]tk_words)
    {
        int count = 0;
        int indx = java.util.Arrays.binarySearch(tk_words, distinct_word);
        /*find the indx of distinct_word in tk_words using BinSearch algo
         * works only if tk_words is already sorted*/
        if(indx > 0)
            while(indx > 0 && tk_words[indx].equals(distinct_word))
                indx--;//goes to lhs matching end of distinct_word
        while(indx < tk_words.length && indx >= 0)
        {
            if(tk_words[indx].equals(distinct_word))
                count++;//inc count since a match is found
            indx++;
            if(indx < tk_words.length && !tk_words[indx].equals(distinct_word))
                break;//if a non-match is found break as array is sorted
        }
        return count;
    }
    //Cosine Similarity
    public float Get_Sim(int doc_i, int doc_j)
    {
        float[]vector1 = Get_Term_Vector(doc_i);
        float[]vector2 = Get_Term_Vector(doc_j);
        return TermVector.Compute_Cosine_Similarity(vector1, vector2);
    }
    private float[] Get_Term_Vector(int doc)
    {
        float[]wt = new float[num_terms];
        for(int i = 0; i < num_terms; i++)
            wt[i] = term_weight[i][doc];
        return wt;
    }
    public String[] doc_max_terms(int doc_no)
    {
        String[] max_terms = new String[5];
        for(int k = 0; k < 5; k++)
        {
            float max = Float.MIN_VALUE;
            int max_index = 0;
            for(int i = 0; i < num_terms; i++)
            {
                int comp = ((Float)term_weight[i][doc_no])
                        .compareTo(((Float)max).floatValue());
                if(comp > 0)
                {
                    max = term_weight[i][doc_no];
                    max_index = i;
                }
            }
            max_terms[k] = terms_alist.get(max_index).toString();
            term_weight[max_index][doc_no] = Float.MIN_VALUE;
        }
        return max_terms;
    }
}
class TermVector
    {
        public static float Compute_Cosine_Similarity(float[]vector1, float[]vector2)
        {
            try
            {
            if(vector1.length != vector2.length)
                throw new Exception("DIFF LENGTH VECTORS");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return Float.MIN_VALUE;
            }
            float denom = (Vector_Len(vector1) * Vector_Len(vector2));
            if(denom == 0f)
                return 0f;
            return (Inner_Product(vector1, vector2)/denom);            
        }
        public static float Inner_Product(float[]vector1, float[]vector2)
        {
            try
            {
            if(vector1.length != vector2.length)
                throw new Exception("DIFF LENGTH VECTORS");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return Float.MIN_VALUE;
            }
            float res = 0f;
            for(int i = 0; i < vector1.length; i++)
                res += vector1[i] * vector2[i];
            return res;
        }
        public static float Vector_Len(float[]vec)
        {
            float sum = 0.0f;
            for(int i = 0; i < vec.length; i++)
                sum += vec[i] * vec[i];
            return (float)Math.sqrt(sum);
        }
    }
