/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
%eofval}

%class CoolLexer
%cup

WHITESPACE = [ \f\r\t\x0b] 
DIGIT = [0-9]


%state STRING
%state STRING_NULL
%state LINE_COMMENT
%state BLOCK_COMMENT

%%
<YYINITIAL>\" {
  string_buf.setLength(0);
  yybegin(STRING); 
}

<STRING>[^\"\0\n\\]+ { /* Normal characters */ 
  string_buf.append(yytext()); 
}

<STRING>\\[^\n\0]  { /* \c */
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

<STRING>\\\n { /* Escaped new line */
  string_buf.append('\n');
  curr_lineno += 1;
}

<STRING>\n { /* ERROR: unterminated string */
  yybegin(YYINITIAL);
  curr_lineno += 1;
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>\0|\\\0 { /* ERROR: null character */
  yybegin(STRING_NULL);
}

<STRING>\" { /* End of string */
  yybegin(YYINITIAL);
  String str = string_buf.toString();
  if(str.length() >= MAX_STR_CONST)
    return new Symbol(TokenConstants.ERROR, "String constant too long");
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(str));
}

<STRING_NULL>\\\n { /* Escaped newline is not the end of string */}
<STRING_NULL>[^\"\n] {}
<STRING_NULL>[\"\n] { 
  yybegin(YYINITIAL);
  curr_lineno += 1;
  return new Symbol(TokenConstants.ERROR, "String contains null character");
}


<YYINITIAL>"--"    { /* Line Comment */ yybegin(LINE_COMMENT); }
<LINE_COMMENT>.*   {/* The end could be EOF */}
<LINE_COMMENT>.*\n { yybegin(YYINITIAL); curr_lineno += 1; }

<YYINITIAL>"(*"     {/* Block Comment */ 
  yybegin(BLOCK_COMMENT);
  comment_open_num += 1;
}
<YYINITIAL>"*)"     { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
<BLOCK_COMMENT>"(*" { comment_open_num += 1; }
<BLOCK_COMMENT>.    {}
<BLOCK_COMMENT>\n   { curr_lineno += 1; }
<BLOCK_COMMENT>"*)" {
  comment_open_num -= 1;
  if(comment_open_num < 0)
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
  else if(comment_open_num == 0)
    yybegin(YYINITIAL);
}

<YYINITIAL>\n { curr_lineno++; }
<YYINITIAL>{WHITESPACE}+ { /* Do nothing but just eat it up */ }
<YYINITIAL>{DIGIT}+  { return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }

<YYINITIAL>[Cc][Ll][Aa][Ss][Ss]             { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[Ee][Ll][Ss][Ee]                 { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>f[Aa][Ll][Ss][Ee]                { return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
<YYINITIAL>t[Rr][Uu][Ee]                    { return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
<YYINITIAL>[Ff][Ii]                         { return new Symbol(TokenConstants.FI); }
<YYINITIAL>[Ii][Ff]                         { return new Symbol(TokenConstants.IF); }
<YYINITIAL>[Ii][Nn]                         { return new Symbol(TokenConstants.IN); }
<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss] { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd]         { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[Ll][Ee][Tt]                     { return new Symbol(TokenConstants.LET); }
<YYINITIAL>[Ll][Oo][Oo][Pp]                 { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[Pp][Oo][Oo][Ll]                 { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[Tt][Hh][Ee][Nn]                 { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>[Ww][Hh][Ii][Ll][Ee]             { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>[Cc][Aa][Ss][Ee]                 { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[Ee][Ss][Aa][Cc]                 { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>[Nn][Ee][Ww]                     { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[Oo][Ff]                         { return new Symbol(TokenConstants.OF); }
<YYINITIAL>[Nn][Oo][Tt]                     { return new Symbol(TokenConstants.NOT); }

<YYINITIAL>[A-Z][_A-Za-z0-9]* { /* Type Identifyer */ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
<YYINITIAL>[a-z][_A-Za-z0-9]* { /* Object Identifyer */ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }


<YYINITIAL>"=>"	    { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"<="     { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"<-"     { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"+"      { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"/"      { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"-"      { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"      { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"="      { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"      { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"."      { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~"      { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>","      { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>";"      { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"      { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"("      { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"      { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>"@"      { return new Symbol(TokenConstants.AT); }
<YYINITIAL>"}"      { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"{"      { return new Symbol(TokenConstants.LBRACE); }


.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                 // System.err.println("LEXER BUG - UNMATCHED: " + yytext());
                                  return new Symbol(TokenConstants.ERROR, yytext());
                                }
