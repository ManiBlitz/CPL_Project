// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 3 - 1st Deliverable
// File:      Tokenizer.java
// =============================================================================
// Description:
// This file is the implementation of the scanner class.
// =============================================================================

package scanner;


import exceptionsPack.ParserException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Karl Kevin TIBA FOSSOH #000801608
 */

public class Tokenizer {
    
    private class TokenInfo {
        public final Pattern regex;
        public final int  token;
                
        public TokenInfo(Pattern regex, int token) {
            super();
            this.regex = regex;
            this.token = token;
        }  
        
    }
    
    //==========================================================================
    //LEXICAL RULES 
    //These are the rules specified for the different operators, and identifiers
    //Contains regex of the different operators, literals and identifiers
    //they will be used to scan the sample code
    //==========================================================================
    
    private static final String IDENTIFIER          = "[a-zA-Z][a-zA-Z0-9_]*";
    private static final String LITERAL_INTEGER     = "[0-9]+";
    private static final String ASSIGNMENT_OPERATOR = "[=]";
    private static final String LE_OPERATOR         = "[<][=]";
    private static final String LT_OPERATOR         = "[<]";
    private static final String GE_OPERATOR         = "[=][>]";
    private static final String GT_OPERATOR         = "[>]";
    private static final String EQ_OPERATOR         = "[=][=]";
    private static final String NE_OPERATOR         = "[~][=]";
    private static final String ADD_OPERATOR        = "[+]";
    private static final String SUB_OPERATOR        = "[-]";
    private static final String MUL_OPERATOR        = "[^/|^*][*][^/|^*]";
    private static final String DIV_OPERATOR        = "[^/|^*][/][^/|^*]";
    private static final String POW_OPERATOR        = "[\\^]";
    private static final String LITERAL_QUOTE       = "[\"]";
    private static final String LINE_COMMENT        = "[/][/]";
    private static final String BEGIN_COMMENT       = "[/][\\*]";
    private static final String END_COMMENT         = "[\\*][/]";
    private static final String LITERAL_COMMA       = "[,]";
    private static final String OPEN_BRACKET        = "[(]";
    private static final String CLOSE_BRACKET       = "[)]";
    private static final String OPEN_BRACE          = "[\\[]";
    private static final String CLOSE_BRACE         = "[\\]]";
    private static final String WHITE_SPACE         = "[\t]+|[\r]+|[\f]+|[ ]+";
    private static final String DOT_PTS             = "[.]";
    private static final String LITERAL_TEXT        = "[^\"\\\\\\r\\n]*(?:\\\\.[^\"\\\\\\r\\n]*)*";
    private static final String OTHERS              = ".+";

    //==========================================================================
    //RSVP_[ELEMENT]
    //These are the lexical rules for the reserved words
    //Contains regex of the different words that are reserved by the language
    //they will be used to verify the 
    //==========================================================================
    
    private static final String RSVP_SPEC = "specifications";
    private static final String RSVP_SYMB = "symbol";
    private static final String RSVP_FOWA = "forward";
    private static final String RSVP_REFE = "references";
    private static final String RSVP_FUNC = "function";
    private static final String RSVP_PRIN = "printer";
    private static final String RSVP_ARRA = "array";
    private static final String RSVP_TYPE = "type";
    private static final String RSVP_STRU = "struct";
    private static final String RSVP_INTE = "integer";
    private static final String RSVP_ENUM = "enum";
    private static final String RSVP_GLOB = "global";
    private static final String RSVP_DECL = "declarations";
    private static final String RSVP_IMPL = "implementations";
    private static final String RSVP_MAIN = "main";
    private static final String RSVP_PARA = "parameters";
    private static final String RSVP_CONS = "constants";
    private static final String RSVP_BEGI = "begin";
    private static final String RSVP_ENDF = "endfun";
    private static final String RSVP_ENDI = "endif";
    private static final String RSVP_IF   = "if";
    private static final String RSVP_OF   = "of";
    private static final String RSVP_IS   = "is";
    private static final String RSVP_THEN = "then";
    private static final String RSVP_ELSE = "else";
    private static final String RSVP_REPE = "repeat";
    private static final String RSVP_UNTI = "until";
    private static final String RSVP_ENDR = "endrepeat";
    private static final String RSVP_DISP = "display";
    private static final String RSVP_SET  = "set";
    private static final String RSVP_RETU = "return";
    private static final String RSVP_DEFI = "define";
    private static final String RSVP_IMPO = "import";
    private static final String RSVP_COLO = ":";
    private static final String RSVP_DO   = "do";
    private static final String RSVP_VARI = "variables";
    private static final String RSVP_WHIL = "while";
    private static final String RSVP_ENDW = "endwhile";
    private static final String RSVP_SHOR = "short";
    private static final String RSVP_MVOI = "mvoid";
    private static final String RSVP_DESC = "description";
    private static final String RSVP_INPU = "input";
    private static final String RSVP_FOR  = "for";
    private static final String RSVP_TO   = "to";
    private static final String RSVP_ENDO = "endfor";
    private static final String RSVP_EXIT = "exit";
    
    //==========================================================================
    //LEXICAL RULES NUMBERS
    //Each token has a code that is associated to it
    //It will be used in the parser definition for syntax analysis
    //The different codes will be associated to the token regex in key list
    //==========================================================================
    
    public static final int IDENTIFIER_N            = 5047;
    public static final int LITERAL_INTEGER_N       = 5030;
    public static final int ASSIGNMENT_OPERATOR_N   = 5037;
    public static final int LE_OPERATOR_N           = 5032;
    public static final int LT_OPERATOR_N           = 5033;
    public static final int GE_OPERATOR_N           = 5034;
    public static final int GT_OPERATOR_N           = 5035;
    public static final int EQ_OPERATOR_N           = 5031;
    public static final int NE_OPERATOR_N           = 5036;
    public static final int ADD_OPERATOR_N          = 5038;
    public static final int SUB_OPERATOR_N          = 5039;
    public static final int MUL_OPERATOR_N          = 5048;
    public static final int DIV_OPERATOR_N          = 5049;
    public static final int POW_OPERATOR_N          = 5057;
    public static final int LITERAL_QUOTE_N         = 5044;
    public static final int LINE_COMMENT_N          = 5041;
    public static final int BEGIN_COMMENT_N         = 5040;
    public static final int END_COMMENT_N           = 5046;
    public static final int LITERAL_COMMA_N         = 5045;
    public static final int OPEN_BRACKET_N          = 5042;
    public static final int CLOSE_BRACKET_N         = 5043;
    public static final int OPEN_BRACE_N            = 5050;
    public static final int CLOSE_BRACE_N           = 5054;
    public static final int WHITE_SPACE_N           = 5055;
    public static final int DOT_PTS_N               = 5056;
    public static final int LITERAL_TEXT_N          = 5066;
    
    //==========================================================================
    //RESERVED WORDS NUMBERS
    //Each reserved has a code that is associated to it
    //It will be used in the parser definition for syntax analysis
    //The different codes will be associated to the token regex in key list
    //==========================================================================
    
    public static final int RSVP_SPEC_N = 5001;
    public static final int RSVP_SYMB_N = 5002;
    public static final int RSVP_FOWA_N = 5003;
    public static final int RSVP_REFE_N = 5004;
    public static final int RSVP_FUNC_N = 5005;
    public static final int RSVP_PRIN_N = 5006;
    public static final int RSVP_ARRA_N = 5007;
    public static final int RSVP_TYPE_N = 5008;
    public static final int RSVP_STRU_N = 5009;
    public static final int RSVP_INTE_N = 5010;
    public static final int RSVP_ENUM_N = 5011;
    public static final int RSVP_GLOB_N = 5012;
    public static final int RSVP_DECL_N = 5013;
    public static final int RSVP_IMPL_N = 5014;
    public static final int RSVP_MAIN_N = 5015;
    public static final int RSVP_PARA_N = 5016;
    public static final int RSVP_CONS_N = 5017;
    public static final int RSVP_BEGI_N = 5018;
    public static final int RSVP_ENDF_N = 5019;
    public static final int RSVP_ENDI_N = 5020;
    public static final int RSVP_IF_N   = 5021;
    public static final int RSVP_THEN_N = 5022;
    public static final int RSVP_ELSE_N = 5023;
    public static final int RSVP_REPE_N = 5024;
    public static final int RSVP_UNTI_N = 5025;
    public static final int RSVP_ENDR_N = 5026;
    public static final int RSVP_DISP_N = 5027;
    public static final int RSVP_SET_N  = 5028;
    public static final int RSVP_RETU_N = 5029;
    public static final int RSVP_DEFI_N = 5050;
    public static final int RSVP_IMPO_N = 5051;
    public static final int RSVP_COLO_N = 5053;
    public static final int RSVP_DO_N   = 5052;
    public static final int RSVP_VARI_N = 5057;
    public static final int RSVP_WHIL_N = 5058;
    public static final int RSVP_ENDW_N = 5059;
    public static final int RSVP_SHOR_N = 5060;
    public static final int RSVP_MVOI_N = 5061;
    public static final int RSVP_DESC_N = 5062;
    public static final int RSVP_IS_N   = 5063;
    public static final int RSVP_OF_N   = 5064;
    public static final int RSVP_INPU_N = 5065;
    public static final int RSVP_FOR_N  = 5067;
    public static final int RSVP_TO_N   = 5068;
    public static final int RSVP_ENDO_N = 5069;
    public static final int RSVP_EXIT_N = 5070;
    
    
    
    //==========================================================================
    //Special lexical Rule
    //EPSILON
    //Enables to define the end of a recursive expression
    //Will be used in the parser
    //==========================================================================
    
    public static final int EPSILON = 0;
                   
    //==========================================================================
    //tokenInfos
    //linked list of TokenInfo
    //contains the differnt tokens regular experession correspondents
    //Used to make the comparisons during scanning to find literals
    //==========================================================================
    
    private LinkedList<TokenInfo> tokenInfos;
    
    //==========================================================================
    //tokens
    //Linked list of Token
    //Contains the differnt tokens that will be found in the sample file
    //==========================================================================
    
    private LinkedList<Token> tokens;

    // =========================================================================
    // __constructor__ Tokenizer()
    // initialize tokenInfos and tokens
    // =========================================================================
    
    public Tokenizer() {
      tokenInfos = new LinkedList<TokenInfo>();
      tokens = new LinkedList<Token>();
    }
    
    // =========================================================================
    // void add(String regex, int token)
    // Add a new lexical sample to tokenInfos
    // String regex : represents the regex associated to the lexical sample
    // int token : represents the integer code associated to the token
    // =========================================================================
    
    public void add(String regex, int token) {
    tokenInfos.add(
        new TokenInfo(
        Pattern.compile("^("+regex+")"), token));
    }
    
    // =========================================================================
    // void tokenize(String str, int line_number)
    // Take a line of code and scan it to identify the different tokens
    // The different tokens found are added to a list of tokens
    // In case one token is undefined, an error message is printed out
    // String str: contains the line string to be scanned
    // int line_number: indicated the number of the line on which we are working
    // =========================================================================
    
    public void tokenize(String str, int line_number) {
        
        String s = new String(str);
        int col_num = 1;
        
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;

                    String tok = m.group().trim();
                    if(info.token != WHITE_SPACE_N){
                        tokens.add(new Token(info.token, tok, col_num, line_number));
                    }else{
                        col_num--;
                    }
                    s = m.replaceFirst("");
                    break;
                }
            }
            if (!match) throw new ParserException("Unexpected character in input: "+s);
            col_num++;
        }
        
    }
        
    
    // =========================================================================
    // LinkedList<Token> getTokens()
    // Return the linkedList containing the different tokens detected
    // Returns the attribute tokens
    // =========================================================================
    
    public LinkedList<Token> getTokens() {
        return tokens;
    }
    
    public static Tokenizer initTokenizer(){
        
        //======================================================================
        //The different tokenizer used will be taken from the lexical sample
        //To this will be added a reader that will read through the file 
        //That reader will provide the different lines to be read
        //======================================================================
        
        Tokenizer tokenizer = new Tokenizer();
        
        
        //======================================================================
        //Reserved Words
        //======================================================================
        
        tokenizer.add(RSVP_SPEC, RSVP_SPEC_N);       
        tokenizer.add(RSVP_SYMB, RSVP_SYMB_N);       
        tokenizer.add(RSVP_FOWA, RSVP_FOWA_N);       
        tokenizer.add(RSVP_REFE, RSVP_REFE_N);       
        tokenizer.add(RSVP_FUNC, RSVP_FUNC_N);       
        tokenizer.add(RSVP_PRIN, RSVP_PRIN_N);       
        tokenizer.add(RSVP_ARRA, RSVP_ARRA_N);       
        tokenizer.add(RSVP_TYPE, RSVP_TYPE_N);       
        tokenizer.add(RSVP_STRU, RSVP_STRU_N);
        tokenizer.add(RSVP_INTE, RSVP_INTE_N); 
        tokenizer.add(RSVP_ENUM, RSVP_ENUM_N); 
        tokenizer.add(RSVP_GLOB, RSVP_GLOB_N);   
        tokenizer.add(RSVP_DECL, RSVP_DECL_N);  
        tokenizer.add(RSVP_IMPL, RSVP_IMPL_N);   
        tokenizer.add(RSVP_MAIN, RSVP_MAIN_N);     
        tokenizer.add(RSVP_PARA, RSVP_PARA_N);   
        tokenizer.add(RSVP_CONS, RSVP_CONS_N);     
        tokenizer.add(RSVP_BEGI, RSVP_BEGI_N);     
        tokenizer.add(RSVP_ENDF, RSVP_ENDF_N);   
        tokenizer.add(RSVP_ENDI, RSVP_ENDI_N);   
        tokenizer.add(RSVP_IF,   RSVP_IF_N  );    
        tokenizer.add(RSVP_THEN, RSVP_THEN_N);    
        tokenizer.add(RSVP_ELSE, RSVP_ELSE_N);   
        tokenizer.add(RSVP_REPE, RSVP_REPE_N);     
        tokenizer.add(RSVP_UNTI, RSVP_UNTI_N);    
        tokenizer.add(RSVP_ENDR, RSVP_ENDR_N);    
        tokenizer.add(RSVP_DISP, RSVP_DISP_N);   
        tokenizer.add(RSVP_SET,  RSVP_SET_N );   
        tokenizer.add(RSVP_RETU, RSVP_RETU_N);
        tokenizer.add(RSVP_DEFI, RSVP_DEFI_N);
        tokenizer.add(RSVP_IMPO, RSVP_IMPO_N);
        tokenizer.add(RSVP_DO  , RSVP_DO_N  );
        tokenizer.add(RSVP_COLO, RSVP_COLO_N);
        tokenizer.add(RSVP_VARI, RSVP_VARI_N);
        tokenizer.add(RSVP_WHIL, RSVP_WHIL_N);
        tokenizer.add(RSVP_ENDW, RSVP_ENDW_N);
        tokenizer.add(RSVP_SHOR, RSVP_SHOR_N);
        tokenizer.add(RSVP_MVOI, RSVP_MVOI_N);
        tokenizer.add(RSVP_DESC, RSVP_DESC_N);
        tokenizer.add(RSVP_OF  , RSVP_OF_N  );
        tokenizer.add(RSVP_IS  , RSVP_IS_N  );
        tokenizer.add(RSVP_INPU, RSVP_INPU_N);
        tokenizer.add(RSVP_FOR , RSVP_FOR_N );
        tokenizer.add(RSVP_TO  , RSVP_TO_N  );
        tokenizer.add(RSVP_ENDO, RSVP_ENDO_N);
        tokenizer.add(RSVP_EXIT, RSVP_EXIT_N);
        
        //======================================================================
        //Operators
        //======================================================================
    
        tokenizer.add(LITERAL_INTEGER,      LITERAL_INTEGER_N); 
        tokenizer.add(EQ_OPERATOR,          EQ_OPERATOR_N);
        tokenizer.add(LE_OPERATOR,          LE_OPERATOR_N);   
        tokenizer.add(LT_OPERATOR,          LT_OPERATOR_N);   
        tokenizer.add(GE_OPERATOR,          GE_OPERATOR_N);     
        tokenizer.add(GT_OPERATOR,          GT_OPERATOR_N);    
        tokenizer.add(NE_OPERATOR,          NE_OPERATOR_N); 
        tokenizer.add(ASSIGNMENT_OPERATOR,  ASSIGNMENT_OPERATOR_N); 
        tokenizer.add(ADD_OPERATOR,         ADD_OPERATOR_N);  
        tokenizer.add(SUB_OPERATOR,         SUB_OPERATOR_N);   
        tokenizer.add(MUL_OPERATOR,         MUL_OPERATOR_N);   
        tokenizer.add(DIV_OPERATOR,         DIV_OPERATOR_N);  
        tokenizer.add(OPEN_BRACKET,         OPEN_BRACKET_N); 
        tokenizer.add(CLOSE_BRACKET,        CLOSE_BRACKET_N);
        tokenizer.add(LITERAL_QUOTE,        LITERAL_QUOTE_N);
        tokenizer.add(LITERAL_COMMA,        LITERAL_COMMA_N);
        tokenizer.add(LINE_COMMENT,         LINE_COMMENT_N);
        tokenizer.add(BEGIN_COMMENT,        BEGIN_COMMENT_N);
        tokenizer.add(END_COMMENT,          END_COMMENT_N);
        tokenizer.add(IDENTIFIER,           IDENTIFIER_N);
        tokenizer.add(OPEN_BRACE,           OPEN_BRACE_N);
        tokenizer.add(CLOSE_BRACE,          CLOSE_BRACE_N);
        tokenizer.add(WHITE_SPACE,          WHITE_SPACE_N);
        tokenizer.add(DOT_PTS,              DOT_PTS_N);
        tokenizer.add(POW_OPERATOR,         POW_OPERATOR_N);
        tokenizer.add(LITERAL_TEXT,         LITERAL_TEXT_N);
        tokenizer.add(OTHERS,               6000);
        
        
        return tokenizer;
        
    }
    
    //==========================================================================
    //Main function
    //Used for testing of the different lexical samples
    //Takes one line and return the differnt tokens found
    //Does not give information about the line or the column
    //==========================================================================

    
    public static void main(String[] args) {
        
        Tokenizer tokenizer = initTokenizer();
        
        try {
            tokenizer.tokenize("define varm2 array[MM] of type integer",9);

            for (Token tok : tokenizer.getTokens()) {
                System.out.println("row: "+tok.row_num+ " , col: " + tok.col_num + " | token_code: " + tok.token + " | token_sequence: " + tok.sequence);
            }
        }
        catch (ParserException e) {
            System.out.println("\n\n"+e.getMessage());
        }
    }
    
    
    
}
