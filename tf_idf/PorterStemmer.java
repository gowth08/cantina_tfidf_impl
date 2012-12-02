package tf_idf;

	interface StemmerInterface 
	{
		String stemTerm( String s );
	}


	public class PorterStemmer implements StemmerInterface
	{

		private char _b[];
		private int i,     // offset into b 
			i_end, // offset to end of stemmed word 
			j, k;
		private static int INC = 200;
		// unit of size whereby b is increased 
		
		public PorterStemmer() 
		{
			_b = new char[INC];
			i = 0;
			i_end = 0;
		}
		
		public String stemTerm( String s )
		{
			setTerm( s );
			stem();
			return getTerm();
		}

		void setTerm( String s)
		{
			i = s.length();
			char new_b[] = new char[i];
			for (int c = 0; c < i; c++)
				{
				new_b[c] = s.charAt(c);
                _b[c]  = new_b[c];}
		}

		public String getTerm()
		{
			return new String(_b, 0, i_end);//find out
		}


		public void add(char ch) 
		{
			String s1=new String(_b);
			if (i == s1.length()) 
			{
				char new_b[] = new char[i+INC];
				for (int c = 0; c < i; c++)
					{new_b[c] = _b[c];
				_b[c] = new_b[c];}
			}
			_b[i++] = ch;
		}

		public void add(char[] w, int wLen) 
		{
			String s1=new String(_b);
			if (i+wLen >= s1.length()) 
			{
				char[] new_b = new char[i+wLen+INC];
				for (int c = 0; c < i; c++)
					{
                    new_b[c] = _b[c];
                    _b[c] = new_b[c];
                }
			}
			for (int c = 0; c < wLen; c++)
				_b[i++] = w[c];
		}

        @Override
		public  String toString() 
		{
			return new String(_b,0,i_end);//find out
		}

		public int getResultLength() 
		{
			return i_end;
		}

		public char[] getResultBuffer() //find out
		{
			return _b;
		}


		private boolean cons(int i) 
		{
			switch (_b[i]) 
			{
				case 'a': case 'e': case 'i': case 'o': case 'u': return false;
				case 'y': return (i==0) ? true : !cons(i-1);
				default: return true;
			}
		}

		private int m() 
		{
			int n = 0;
			int i_m = 0;
			while(true) 
			{
				if (i_m > j) return n;
				if (! cons(i_m)) break; i_m++;
			}
			i_m++;
			while(true) 
			{
				while(true) 
				{
					if (i_m > j) return n;
					if (cons(i_m)) break;
					i_m++;
				}
				i_m++;
				n++;
				while(true) 
				{
					if (i_m > j) return n;
					if (! cons(i_m)) break;
					i_m++;
				}
				i_m++;
			}
		}


		private boolean vowelinstem() 
		{
			int i_vowelinstem;
			for (i_vowelinstem = 0; i_vowelinstem <= j; i_vowelinstem++)
				if (! cons(i_vowelinstem))
					return true;
			return false;
		}


		private boolean doublec(int j) 
		{
			if (j < 1)
				return false;
			if (_b[j] != _b[j-1])
				return false;
			return cons(j);
		}

		private boolean cvc(int i) 
		{
			if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2))
				return false;
			int ch = _b[i];
			if (ch == 'w' || ch == 'x' || ch == 'y')
				return false;
			return true;
		}

		private boolean ends(String s) 
		{
			int l = s.length();
			int o = k-l+1;
			if (o < 0)
				return false;
			char[] sc = s.toCharArray();
			for (int i = 0; i < l; i++)
				if (_b[o+i] != sc[i])
					return false;
			j = k-l;
			return true;
		}

		/// setto(s) sets (j+1),...k to the characters in the string s, readjusting
		//
		private void setto(String s) 
		{
			int l = s.length();
			int o = j+1;
			char[] sc = s.toCharArray();
			for (int i = 0; i < l; i++)
				_b[o+i] = sc[i];
			k = j+l;
		}


		private void r(String s) 
		{
			if (m() > 0)
				setto(s);
		}

		private void step1() 
		{
			if (_b[k] == 's') 
			{
				if (ends("sses"))
					k -= 2;
				else if (ends("ies"))
					setto("i");
				else if (_b[k-1] != 's')
					k--;
			}
			if (ends("eed")) 
			{
				if (m() > 0)
					k--;
			} 
			else if ((ends("ed") || ends("ing")) && vowelinstem()) 
			{
				k = j;
				if (ends("at"))
					setto("ate");
				else if (ends("bl"))
					setto("ble");
				else if (ends("iz"))
					setto("ize");
				else if (doublec(k)) 
				{
					k--;
					int ch = _b[k];
					if (ch == 'l' || ch == 's' || ch == 'z')
						k++;
				}
				else if (m() == 1 && cvc(k)) setto("e");
			}
		}


		private void step2() 
		{
			if (ends("y") && vowelinstem())
				_b[k] = 'i';
		}

		private void step3() 
		{
			if (k == 0)
				return;
			
			// For Bug 1 
			switch (_b[k-1]) 
			{
				case 'a':
					if (ends("ational")) { r("ate"); break; }
					if (ends("tional")) { r("tion"); break; }
					break;
				case 'c':
					if (ends("enci")) { r("ence"); break; }
					if (ends("anci")) { r("ance"); break; }
					break;
				case 'e':
					if (ends("izer")) { r("ize"); break; }
					break;
				case 'l':
					if (ends("bli")) { r("ble"); break; }
					if (ends("alli")) { r("al"); break; }
					if (ends("entli")) { r("ent"); break; }
					if (ends("eli")) { r("e"); break; }
					if (ends("ousli")) { r("ous"); break; }
					break;
				case 'o':
					if (ends("ization")) { r("ize"); break; }
					if (ends("ation")) { r("ate"); break; }
					if (ends("ator")) { r("ate"); break; }
					break;
				case 's':
					if (ends("alism")) { r("al"); break; }
					if (ends("iveness")) { r("ive"); break; }
					if (ends("fulness")) { r("ful"); break; }
					if (ends("ousness")) { r("ous"); break; }
					break;
				case 't':
					if (ends("aliti")) { r("al"); break; }
					if (ends("iviti")) { r("ive"); break; }
					if (ends("biliti")) { r("ble"); break; }
					break;
				case 'g':
					if (ends("logi")) { r("log"); break; }
					break;
				default :
					break;
			}
		}

		// step4() deals with -ic-, -full, -ness etc. similar strategy to step3.
		private void step4() 
		{
			switch (_b[k]) 
			{
				case 'e':
					if (ends("icate")) { r("ic"); break; }
					if (ends("ative")) { r(""); break; }
					if (ends("alize")) { r("al"); break; }
					break;
				case 'i':
					if (ends("iciti")) { r("ic"); break; }
					break;
				case 'l':
					if (ends("ical")) { r("ic"); break; }
					if (ends("ful")) { r(""); break; }
					break;
				case 's':
					if (ends("ness")) { r(""); break; }
					break;
			}
		}

		// step5() takes off -ant, -ence etc., in context <c>vcvc<v>. 
		private void step5() 
		{
			if (k == 0)
				return;

			// for Bug 1 
			switch ( _b[k-1] ) 
			{
				case 'a':
					if (ends("al")) break; return;
				case 'c':
					if (ends("ance")) break;
					if (ends("ence")) break; return;
				case 'e':
					if (ends("er")) break; return;
				case 'i':
					if (ends("ic")) break; return;
				case 'l':
					if (ends("able")) break;
					if (ends("ible")) break; return;
				case 'n':
					if (ends("ant")) break;
					if (ends("ement")) break;
					if (ends("ment")) break;
					// element etc. not stripped before the m 
					if (ends("ent")) break; return;
				case 'o':
					if (ends("ion") && j >= 0 && (_b[j] == 's' || _b[j] == 't')) break;
					// j >= 0 fixes Bug 2 
					if (ends("ou")) break; return;
					// takes care of -ous 
				case 's':
					if (ends("ism")) break; return;
				case 't':
					if (ends("ate")) break;
					if (ends("iti")) break; return;
				case 'u':
					if (ends("ous")) break; return;
				case 'v':
					if (ends("ive")) break; return;
				case 'z':
					if (ends("ize")) break; return;
				default:
					return;
			}
			if (m() > 1)
				k = j;
		}

		// step6() removes a final -e if m() > 1. 
		private void step6() 
		{
			j = k;
			
			if (_b[k] == 'e') 
			{
				int a = m();
				if (a > 1 || a == 1 && !cvc(k-1))
					k--;
			}
			if (_b[k] == 'l' && doublec(k) && m() > 1)
				k--;
		}

		public void stem() 
		{
			k = i - 1;
			if (k > 1) 
			{
				step1();
				step2();
				step3();
				step4();
				step5();
				step6();
			}
			i_end = k+1;
			i = 0;
		}
}
