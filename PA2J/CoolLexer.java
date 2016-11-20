/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private int comment_open_num = 0;
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int BLOCK_COMMENT = 4;
	private final int LINE_COMMENT = 3;
	private final int YYINITIAL = 0;
	private final int STRING_NULL = 2;
	private final int yy_state_dtrans[] = {
		0,
		65,
		89,
		59,
		93
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"5,7:8,11,4,11:2,2,7:18,11,7,1,7:5,8,10,9,53,57,6,55,54,12:10,59,58,52,50,51" +
",7,60,32,33,34,35,36,22,33,37,38,33:2,39,33,40,41,42,33,43,44,26,45,46,47,3" +
"3:3,7,3,7:2,48,7,15,49,13,29,17,18,49,25,23,49:2,14,49,24,28,30,49,20,16,19" +
",21,27,31,49:3,62,7,61,56,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,175,
"0,1:2,2,1:2,3,4,5,1,6,7,8,9,10,1:13,11:2,12,11,1:3,11:15,1,13,1:7,14,1:5,15" +
",16,17,18,19:2,20,19:14,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37," +
"38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62," +
"63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87," +
"88,89,90,91,92,93,94,95,96,97,98,11,19,99,100,101,102,103,104,105,106,107")[0];

	private int yy_nxt[][] = unpackFromString(108,63,
"1,2,3,4,5,4,6,4,7,8,9,3,10,11,124,164:2,166,67,168,164:2,12,90,126,164,68,1" +
"64,94,164,170,172,165:2,167,165,169,165,91,125,127,95,171,165:4,173,4,164,1" +
"3,4,14,15,16,17,18,19,20,21,22,23,24,-1:65,3,-1:8,3,-1:57,25,-1:65,26,-1:63" +
",27,-1:64,10,-1:62,164:2,174,128,164:16,128,164:6,174,164:10,-1:25,165:11,6" +
"9,165:14,69,165:11,-1:64,32,-1:17,33,-1:43,34,-1:24,164:38,-1:25,164:13,154" +
",164:11,154,164:12,-1:15,51,-1:3,51:57,1,87,-1,87,60,87:58,1,50,51,66,52,53" +
",51:57,-1,54:3,55,53,54:57,-1:12,164:3,136,164:7,28,164:8,136,164:5,28,164:" +
"11,-1:25,165:13,129,165:11,129,165:12,-1:25,165:38,-1:25,165:13,151,165:11," +
"151,165:12,-1:17,58,-1:59,87,-1,87,60,87:58,-1:9,63,-1:53,1,56,57,86,56,57:" +
"58,-1:12,164:4,142,164,29,164:3,29,164,30,164:15,30,164:3,142,164:5,-1:25,1" +
"65:4,139,165,70,165:3,70,165,71,165:15,71,165:3,139,165:5,-1:23,64,-1:52,1," +
"61,-1,61,62,61:3,88,92,61:53,-1:12,164:6,31,164:3,31,164:27,-1:25,165:6,72," +
"165:3,72,165:27,-1:25,164:7,35,164:6,35,164:23,-1:25,165:7,73,165:6,73,165:" +
"23,-1:25,164:19,36,164:15,36,164:2,-1:25,165:19,74,165:15,74,165:2,-1:25,16" +
"4:7,37,164:6,37,164:23,-1:25,165:7,75,165:6,75,165:23,-1:25,164:5,38,164:18" +
",38,164:13,-1:25,165:12,80,165:15,80,165:9,-1:25,164:18,39,164:11,39,164:7," +
"-1:25,165:5,76,165:18,76,165:13,-1:25,164:5,40,164:18,40,164:13,-1:25,165:5" +
",78,165:18,78,165:13,-1:25,164,41,164:20,41,164:15,-1:25,165,79,165:20,79,1" +
"65:15,-1:25,164:5,42,164:18,42,164:13,-1:25,165:18,77,165:11,77,165:7,-1:25" +
",164:12,43,164:15,43,164:9,-1:25,165:2,81,165:24,81,165:10,-1:25,164:2,44,1" +
"64:24,44,164:10,-1:25,165:4,82,165:27,82,165:5,-1:25,164:4,45,164:27,45,164" +
":5,-1:25,165:5,83,165:18,83,165:13,-1:25,164:5,46,164:18,46,164:13,-1:25,16" +
"5:17,84,165:5,84,165:14,-1:25,164:5,47,164:18,47,164:13,-1:25,165:4,85,165:" +
"27,85,165:5,-1:25,164:17,48,164:5,48,164:14,-1:25,164:4,49,164:27,49,164:5," +
"-1:25,164:5,96,164:10,130,164:7,96,164:4,130,164:8,-1:25,165:5,97,165:10,14" +
"1,165:7,97,165:4,141,165:8,-1:25,164:5,98,164:10,100,164:7,98,164:4,100,164" +
":8,-1:25,165:5,99,165:10,101,165:7,99,165:4,101,165:8,-1:25,164:4,102,164:2" +
"7,102,164:5,-1:25,165:5,103,165:18,103,165:13,-1:25,164:16,104,164:12,104,1" +
"64:8,-1:25,165:3,147,165:16,147,165:17,-1:25,164:4,106,164:27,106,164:5,-1:" +
"25,165:4,105,165:27,105,165:5,-1:25,164:3,108,164:16,108,164:17,-1:25,165:4" +
",107,165:27,107,165:5,-1:25,164:2,150,164:24,150,164:10,-1:25,165:3,109,165" +
":16,109,165:17,-1:25,164:9,110,164:23,110,164:4,-1:25,165:15,149,165:18,149" +
",165:3,-1:25,164:5,112,164:18,112,164:13,-1:25,165:16,111,165:12,111,165:8," +
"-1:25,164:15,152,164:18,152,164:3,-1:25,165:16,113,165:12,113,165:8,-1:25,1" +
"64:16,114,164:12,114,164:8,-1:25,165:11,153,165:14,153,165:11,-1:25,164:11," +
"156,164:14,156,164:11,-1:25,165:4,115,165:27,115,165:5,-1:25,164:4,116,164:" +
"27,116,164:5,-1:25,165:16,155,165:12,155,165:8,-1:25,164:4,118,164:27,118,1" +
"64:5,-1:25,165:5,157,165:18,157,165:13,-1:25,164:16,158,164:12,158,164:8,-1" +
":25,165:2,117,165:24,117,165:10,-1:25,164:5,160,164:18,160,164:13,-1:25,165" +
":11,119,165:14,119,165:11,-1:25,164:2,120,164:24,120,164:10,-1:25,165:8,159" +
",165:22,159,165:6,-1:25,164:11,122,164:14,122,164:11,-1:25,165:11,161,165:1" +
"4,161,165:11,-1:25,164:8,162,164:22,162,164:6,-1:25,165:7,121,165:6,121,165" +
":23,-1:25,164:11,163,164:14,163,164:11,-1:25,164:7,123,164:6,123,164:23,-1:" +
"25,164:2,132,164,134,164:22,132,164:4,134,164:5,-1:25,165:2,131,133,165:16," +
"133,165:6,131,165:10,-1:25,164:8,138,164:4,140,164:11,140,164:5,138,164:6,-" +
"1:25,165:2,135,165,137,165:22,135,165:4,137,165:5,-1:25,164:16,144,164:12,1" +
"44,164:8,-1:25,165:16,143,165:12,143,165:8,-1:25,164:13,146,164:11,146,164:" +
"12,-1:25,165:13,145,165:11,145,165:12,-1:25,164:3,148,164:16,148,164:17,-1:" +
"13");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case STRING:
    case STRING_NULL:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    case BLOCK_COMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
  string_buf.setLength(0);
  yybegin(STRING); 
}
					case -3:
						break;
					case 3:
						{ /* Do nothing but just eat it up */ }
					case -4:
						break;
					case 4:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                 // System.err.println("LEXER BUG - UNMATCHED: " + yytext());
                                  return new Symbol(TokenConstants.ERROR, yytext());
                                }
					case -5:
						break;
					case 5:
						{ curr_lineno++; }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.MINUS); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.MULT); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }
					case -11:
						break;
					case 11:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -12:
						break;
					case 12:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.EQ); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.PLUS); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.DIV); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.DOT); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.NEG); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.COMMA); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.SEMI); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.COLON); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.AT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -25:
						break;
					case 25:
						{ /* Line Comment */ yybegin(LINE_COMMENT); }
					case -26:
						break;
					case 26:
						{/* Block Comment */ 
  yybegin(BLOCK_COMMENT);
  comment_open_num += 1;
}
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.FI); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.IF); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.IN); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.OF); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.THEN); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.POOL); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{ /* End of string */
  yybegin(YYINITIAL);
  String str = string_buf.toString();
  if(str.length() >= MAX_STR_CONST)
    return new Symbol(TokenConstants.ERROR, "String constant too long");
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(str));
}
					case -51:
						break;
					case 51:
						{ /* Normal characters */ 
  string_buf.append(yytext()); 
}
					case -52:
						break;
					case 52:
						{ /* ERROR: unterminated string */
  yybegin(YYINITIAL);
  curr_lineno += 1;
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -53:
						break;
					case 53:
						{ /* ERROR: null character */
  yybegin(STRING_NULL);
}
					case -54:
						break;
					case 54:
						{ /* \c */
  if(yytext().equals("\\b")) 
    string_buf.append('\b');
  else if (yytext().equals("\\t"))
    string_buf.append('\t');
  else if (yytext().equals("\\n"))
    string_buf.append('\n');
  else if (yytext().equals("\\f"))
    string_buf.append('\f');
  else
    string_buf.append(yytext().charAt(1));
}
					case -55:
						break;
					case 55:
						{ /* Escaped new line */
  string_buf.append('\n');
  curr_lineno += 1;
}
					case -56:
						break;
					case 56:
						{ 
  yybegin(YYINITIAL);
  curr_lineno += 1;
  return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -57:
						break;
					case 57:
						{}
					case -58:
						break;
					case 58:
						{ /* Escaped newline is not the end of string */}
					case -59:
						break;
					case 59:
						{/* The end could be EOF */}
					case -60:
						break;
					case 60:
						{ yybegin(YYINITIAL); curr_lineno += 1; }
					case -61:
						break;
					case 61:
						{}
					case -62:
						break;
					case 62:
						{ curr_lineno += 1; }
					case -63:
						break;
					case 63:
						{ comment_open_num += 1; }
					case -64:
						break;
					case 64:
						{
  comment_open_num -= 1;
  if(comment_open_num < 0)
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
  else if(comment_open_num == 0)
    yybegin(YYINITIAL);
}
					case -65:
						break;
					case 66:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                 // System.err.println("LEXER BUG - UNMATCHED: " + yytext());
                                  return new Symbol(TokenConstants.ERROR, yytext());
                                }
					case -66:
						break;
					case 67:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -67:
						break;
					case 68:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.FI); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.IF); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.IN); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.OF); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LET); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.NEW); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.NOT); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.CASE); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.LOOP); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ELSE); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.ESAC); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.THEN); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.POOL); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.CLASS); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.WHILE); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -85:
						break;
					case 86:
						{}
					case -86:
						break;
					case 87:
						{/* The end could be EOF */}
					case -87:
						break;
					case 88:
						{}
					case -88:
						break;
					case 90:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -89:
						break;
					case 91:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -90:
						break;
					case 92:
						{}
					case -91:
						break;
					case 94:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -92:
						break;
					case 95:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -93:
						break;
					case 96:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -94:
						break;
					case 97:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -95:
						break;
					case 98:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -96:
						break;
					case 99:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -97:
						break;
					case 100:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -98:
						break;
					case 101:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -99:
						break;
					case 102:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -100:
						break;
					case 103:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -101:
						break;
					case 104:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -102:
						break;
					case 105:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -103:
						break;
					case 106:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -104:
						break;
					case 107:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -105:
						break;
					case 108:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -106:
						break;
					case 109:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -107:
						break;
					case 110:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -108:
						break;
					case 111:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -109:
						break;
					case 112:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -110:
						break;
					case 113:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -111:
						break;
					case 114:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -112:
						break;
					case 115:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -113:
						break;
					case 116:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -114:
						break;
					case 117:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -115:
						break;
					case 118:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -116:
						break;
					case 119:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -117:
						break;
					case 120:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -118:
						break;
					case 121:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -119:
						break;
					case 122:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -120:
						break;
					case 123:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -121:
						break;
					case 124:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -122:
						break;
					case 125:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -123:
						break;
					case 126:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -124:
						break;
					case 127:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -125:
						break;
					case 128:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -126:
						break;
					case 129:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -127:
						break;
					case 130:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -128:
						break;
					case 131:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -129:
						break;
					case 132:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -130:
						break;
					case 133:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -131:
						break;
					case 134:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -132:
						break;
					case 135:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -133:
						break;
					case 136:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -134:
						break;
					case 137:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -135:
						break;
					case 138:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -136:
						break;
					case 139:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -137:
						break;
					case 140:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -138:
						break;
					case 141:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -139:
						break;
					case 142:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -140:
						break;
					case 143:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -141:
						break;
					case 144:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -142:
						break;
					case 145:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -143:
						break;
					case 146:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -144:
						break;
					case 147:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -145:
						break;
					case 148:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -146:
						break;
					case 149:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -147:
						break;
					case 150:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -148:
						break;
					case 151:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -149:
						break;
					case 152:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -150:
						break;
					case 153:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -151:
						break;
					case 154:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -152:
						break;
					case 155:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -153:
						break;
					case 156:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -154:
						break;
					case 157:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -155:
						break;
					case 158:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -156:
						break;
					case 159:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -157:
						break;
					case 160:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -158:
						break;
					case 161:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -159:
						break;
					case 162:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -160:
						break;
					case 163:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -161:
						break;
					case 164:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -162:
						break;
					case 165:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -163:
						break;
					case 166:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -164:
						break;
					case 167:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -165:
						break;
					case 168:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -166:
						break;
					case 169:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -167:
						break;
					case 170:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -168:
						break;
					case 171:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -169:
						break;
					case 172:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -170:
						break;
					case 173:
						{ /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -171:
						break;
					case 174:
						{ /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -172:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
