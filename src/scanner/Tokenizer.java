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
    private static final String ASSIGNMENT_OPERATOR = ".*[=].*";
    private static final String LE_OPERATOR         = ".*[<][=].*";
    private static final String LT_OPERATOR         = ".*[<].*";
    private static final String GE_OPERATOR         = ".*[=][>].*";
    private static final String GT_OPERATOR         = ".*[>].*";
    private static final String EQ_OPERATOR         = ".*[=][=].*";
    private static final String NE_OPERATOR         = ".*[~][=].*";
    private static final String ADD_OPERATOR        = "[+]";
    private static final String SUB_OPERATOR        = "[-][a-zA-Z]?[a-zA-Z0-9_]*";
    private static final String MUL_OPERATOR        = "[*]";
    private static final String DIV_OPERATOR        = "[/]";
    private static final String LITERAL_TEXT        = "[\"].*?[\"]"; 
    private static final String LITERAL_QUOTE       = "[\"].*|.*[\"]";
    private static final String LINE_COMMENT        = "[/][/].*";
    private static final String BEGIN_COMMENT       = "[/][\\*].*";
    private static final String END_COMMENT         = ".*[\\*][/]";
    private static final String LITERAL_COMMA       = ".*[,]";
    private static final String ARRAY_DEF           = ".*[\\[]"+IDENTIFIER+"[\\]]|.*[\\[][0-9]*[\\]]|.*[\\[][\\]]";
    private static final String ARRAY_INDEX         = IDENTIFIER+ARRAY_DEF;
    private static final String OTHERS              = ".+";

    //==========================================================================
    //RSVP_[ELEMENT]
    //These are the lexical rules for the reserved words
    //Contains regex of the different words that are reserved by the language
    //they will be used to verify the 
    //==========================================================================
    
    private static final String RSVP_SPEC = "specifications";
    private static final String RSVP_SYMB = "symbol";
    private static final String RSVP_FOWA = "foward";
    private static final String RSVP_REFE = "references";
    private static final String RSVP_FUNC = "function";
    private static final String RSVP_PRIN = "printer";
    private static final String RSVP_ARRA = "array";
    private static final String RSVP_TYPE = "type";
    private static final String RSVP_STRU = "struct";
    private static final String RSVP_INTE = "integer";
    private static final String RSVP_ENUM = "enum";
    private static final String RSVP_GLOB = "glob";
    private static final String RSVP_DECL = "declarations";
    private static final String RSVP_IMPL = "implementations";
    private static final String RSVP_MAIN = "main";
    private static final String RSVP_PARA = "parameters";
    private static final String RSVP_CONS = "constant";
    private static final String RSVP_BEGI = "being";
    private static final String RSVP_ENDF = "endfun";
    private static final String RSVP_ENDI = "endif";
    private static final String RSVP_IF   = "if";
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
    private static final String RSVP_BLAN = "";
    private static final String RSVP_DO   = "do";
                   
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
        
        for(String splice : s.split("[\t]+|[\r]+|[\f]+|[ ]+")) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                if (splice.matches(info.regex.pattern())) {
                  match = true;
                  tokens.add(new Token(info.token, splice,col_num,line_number));
                  break;
                }
            }
            if (!match) throw new ParserException("\""+str+"\" : Unexpected character in input: \""+splice+"\" at line "+line_number+" col "+col_num);
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
        
        tokenizer.add(RSVP_SPEC, 5001);       
        tokenizer.add(RSVP_SYMB, 5002);       
        tokenizer.add(RSVP_FOWA, 5003);       
        tokenizer.add(RSVP_REFE, 5004);       
        tokenizer.add(RSVP_FUNC, 5005);       
        tokenizer.add(RSVP_PRIN, 5006);       
        tokenizer.add(RSVP_ARRA, 5007);       
        tokenizer.add(RSVP_TYPE, 5008);       
        tokenizer.add(RSVP_STRU, 5009);
        tokenizer.add(RSVP_INTE, 5010); 
        tokenizer.add(RSVP_ENUM, 5011); 
        tokenizer.add(RSVP_GLOB, 5012);   
        tokenizer.add(RSVP_DECL, 5013);  
        tokenizer.add(RSVP_IMPL, 5014);   
        tokenizer.add(RSVP_MAIN, 5015);     
        tokenizer.add(RSVP_PARA, 5016);   
        tokenizer.add(RSVP_CONS, 5017);     
        tokenizer.add(RSVP_BEGI, 5018);     
        tokenizer.add(RSVP_ENDF, 5019);   
        tokenizer.add(RSVP_ENDI, 5020);   
        tokenizer.add(RSVP_IF,   5021);    
        tokenizer.add(RSVP_THEN, 5022);    
        tokenizer.add(RSVP_ELSE, 5023);   
        tokenizer.add(RSVP_REPE, 5024);     
        tokenizer.add(RSVP_UNTI, 5025);    
        tokenizer.add(RSVP_ENDR, 5026);    
        tokenizer.add(RSVP_DISP, 5027);   
        tokenizer.add(RSVP_SET,  5028);   
        tokenizer.add(RSVP_RETU, 5029);
        tokenizer.add(RSVP_DEFI, 5050);
        tokenizer.add(RSVP_IMPO, 5051);
        tokenizer.add(RSVP_DO  , 5052);
        tokenizer.add(RSVP_BLAN, 5053);
        
        //======================================================================
        //Operators
        //======================================================================
    
        tokenizer.add(LITERAL_INTEGER,      5030); 
        tokenizer.add(EQ_OPERATOR,          5031);
        tokenizer.add(LE_OPERATOR,          5032);   
        tokenizer.add(LT_OPERATOR,          5033);   
        tokenizer.add(GE_OPERATOR,          5034);     
        tokenizer.add(GT_OPERATOR,          5035);    
        tokenizer.add(NE_OPERATOR,          5036); 
        tokenizer.add(ASSIGNMENT_OPERATOR,  5037); 
        tokenizer.add(ADD_OPERATOR,         5038);  
        tokenizer.add(SUB_OPERATOR,         5039);   
        tokenizer.add(MUL_OPERATOR,         5040);   
        tokenizer.add(DIV_OPERATOR,         5041);  
        tokenizer.add(LITERAL_TEXT,         5042); 
        tokenizer.add(ARRAY_INDEX,          5043);
        tokenizer.add(LITERAL_QUOTE,        5044);
        tokenizer.add(LITERAL_COMMA,        5045);
        tokenizer.add(LINE_COMMENT,         5046);
        tokenizer.add(BEGIN_COMMENT,        5047);
        tokenizer.add(END_COMMENT,          5048);
        tokenizer.add(IDENTIFIER,           5049);
        tokenizer.add(ARRAY_DEF,            5050);
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
            tokenizer.tokenize("function result = text + 5 + [result]",9);

            for (Token tok : tokenizer.getTokens()) {
                System.out.println("row: "+tok.row_num+ " , col: " + tok.col_num + " | token_code: " + tok.token + " | token_sequence: " + tok.sequence);
            }
        }
        catch (ParserException e) {
            System.out.println("\n\n"+e.getMessage());
        }
    }
    
    
    
}
