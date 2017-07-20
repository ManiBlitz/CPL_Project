// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 4 - 2nd Deliverable
// File:      Parser.java
// =============================================================================
// Description:
// This file is the implementation of the Parser class.
// =============================================================================

package parser;

import exceptionsPack.ParserException;
import executer.ArithmeticExpression;
import executer.BooleanExpression;
import java.util.LinkedList;
import scanner.Token;
import scanner.Tokenizer;
import statements.DisplayStatement;
import statements.InputStatement;
import statements.funct.Argument;
import statements.funct.ArgumentsList;

/**
 *
 * @author Karl Kevin TIBA FOSSOH #000801608
 */

public class Parser {
    LinkedList<Token> tokens;
    private Token lookahead;
    private IdentifierTable id_table;
    private final String ERROR_MESSAGE = "\nUnexpected Symbol: \"";
    private final String ERROR_LINE = "\" at line ";
    
    
    // =========================================================================
    // void line_exception
    // Generic execption printed each time we find a syntax error
    // uses the global variable lookahead
    // =========================================================================
    
    private void line_exception(){
        
        print_identifierTable();
        
        throw new ParserException(ERROR_MESSAGE + lookahead.sequence + 
                                  ERROR_LINE + lookahead.row_num);
    }
    
    // =========================================================================
    // void print_identifierTable
    // Generic function for printing the content of identifier table
    // uses the instance variable id_table
    // =========================================================================
    
    private void print_identifierTable(){
        
        System.out.println("\n\n\n//===================================="
                + "====//\n  Identifier Table\n//=========================="
                + "==============//\n\n");
        this.id_table.print_table();
        
    }
    
    // =========================================================================
    // void prt
    // Generic expression for printing
    // takes String val
    // =========================================================================
    
    private void prt(String val, int code){
        System.out.println(code+" ===> "+val);
    }
    
    // =========================================================================
    // _CONSTRUCTOR_
    // helps instantiate the parser
    // takes LinkedList<Token> used as input for parsing
    // =========================================================================
    
    public Parser(LinkedList<Token> tokens)
    {
        this.id_table = new IdentifierTable();
        this.tokens = (LinkedList<Token>) tokens.clone();
        lookahead = this.tokens.getFirst();
        
        start();

        if (lookahead.token != Tokenizer.EPSILON)
            throw new ParserException("Unexpected symbol "+ 
                                       lookahead.sequence+" found");
    }
    
    // =========================================================================
    // void nextToken
    // Traverses the tokenlist sequentially
    // puts the next token in the variable lookahead
    // Skips the comments
    // =========================================================================
    
    private void nextToken()
    {
        int last_line = tokens.getFirst().row_num + 1;
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new Token(Tokenizer.EPSILON, "", -1,last_line);
        else{
            lookahead = tokens.getFirst();
            scroll();
            //System.out.print(lookahead.sequence+" ");
        }
          
    }
    
    //==========================================================================
    // SCROLL COMMENT FUNCTION
    // each time a line comment is found, nextToken until the next line
    // each time a multi-line comment, nextToken until the end of comment
    //==========================================================================
    
    private void scroll(){
        if(lookahead.token == Tokenizer.LINE_COMMENT_N){
            int line_n = lookahead.row_num;
            while(lookahead.row_num == line_n){
                nextToken();    
            }
        }else if(lookahead.token == Tokenizer.BEGIN_COMMENT_N || 
                 lookahead.token == Tokenizer.RSVP_DESC_N){
            
            while(lookahead.token != Tokenizer.END_COMMENT_N){
                nextToken();
            }
            
            nextToken();
        }
    }

    // =========================================================================
    // void start()
    // Parsing starting point
    // comprises the different top levels of the code
    // starts the recursive descent of the syntactic analysis
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // start : symbols forward_refs specifications globals implement
    // =========================================================================
    
    private void start() {
        prt("START",1000);
        imports();
        symbols();
        forward_refs();
        specifications();
        globals();
        implement();
    }
    
    // =========================================================================
    // void imports
    // enables to make importation of the additional source files
    // list definition
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // imports: | import_def imports
    // =========================================================================
    
    private void imports(){
        prt("IMPORT",1001);
        import_def();
        if(lookahead.token == Tokenizer.RSVP_IMPO_N){
            imports();
        }
    }
    
    // =========================================================================
    // void import_def
    // enables to make a single importation
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // import_def: IMPORT name_file
    // =========================================================================
    
    private void import_def(){
        if(lookahead.token == Tokenizer.RSVP_IMPO_N){
            prt("IMPORT_DEF",1002);
            nextToken();
            name_file();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void name_file
    // helps define a file name
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // name_file: quote identifier dot_point identifier quote
    // =========================================================================
    
    private void name_file(){
        if(lookahead.token == Tokenizer.LITERAL_QUOTE_N){
            prt("NAME_FILE",1003);
            nextToken();
            identifier();
            dot_point();
            identifier();
            quote();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void dot_point
    // Defines terminal: "."
    // terminal
    // =========================================================================
    
    private void dot_point(){
        if(lookahead.token == Tokenizer.DOT_PTS_N){
            prt("DOT_POINT",1004);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void quote
    // defines terminal ' " '
    // terminal
    // =========================================================================
    
    private void quote(){
        if(lookahead.token == Tokenizer.LITERAL_QUOTE_N){
            prt("QUOTE",1005);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void symbols
    // Helps define upper level code
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // symbols:  | symbol_def symbols
    // =========================================================================
    
    
    private void symbols(){
        prt("SYMBOLS",1006);
        symbol_def();
        if(lookahead.token == Tokenizer.RSVP_SYMB_N){
            symbols();
        }
    }
    
    // =========================================================================
    // void symbol_def
    // Helps define symbols architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // symbol_def: SYMBOL IDENTIFIER [HCON]
    // =========================================================================
    
    private void symbol_def(){
        if(lookahead.token == Tokenizer.RSVP_SYMB_N){
            prt("SYMBOL_DEF",1007);
            nextToken();
            identifier();
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void symbol_def
    // defines terminal identifier
    // terminal
    // =========================================================================

    
    private void identifier(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            prt("IDENTIFIER",1008);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void forward_refs
    // Helps define forwar_refs architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // forward_refs: forward frefs
    // =========================================================================
    
    private void forward_refs(){
        prt("FORWARD_REFS",1009);
        forward();
        frefs();
    }
    
    // =========================================================================
    // void forward
    // Helps defining terminal "forward"
    // terminal
    // =========================================================================
    
    private void forward(){
        if(lookahead.token == Tokenizer.RSVP_FOWA_N){
            prt("FORWARD",1010);
            nextToken();
        }else{
           line_exception(); 
        }
    }
    
    // =========================================================================
    // void frefs
    // Helps define frefs architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // forward_refs: REFERENCES foward_list | DECLARATIONS foward_list 
    //             | foward_list
    // =========================================================================
    
    private void frefs(){
        prt("FREFS",1011);
        if(lookahead.token == Tokenizer.RSVP_REFE_N){
            nextToken();
        }else if(lookahead.token == Tokenizer.RSVP_DECL_N){
            nextToken();
        }
        prt("FOWARD_LIST",1012);
        foward_list();
    }
    
    // =========================================================================
    // void foward_list
    // Helps define foward_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // foward_list: fowards | fowards foward_list
    // =========================================================================
    
    private void foward_list(){
        fowards();
        if(lookahead.token == Tokenizer.RSVP_DESC_N){
            foward_list();
        }
    }
    
    // =========================================================================
    // void fowards
    // Helps define fowards architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // fowards:  | func_main dec_parameters
    // =========================================================================
    
    private void fowards(){
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            prt("FOWARDS",1013);
            func_main();
            dec_parameters();
        }else{
            
        }
    }
    
    // =========================================================================
    // void dec_parameters
    // Helps define dec_parameters architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // dec_parameters: PARAMETERS params_list
    // =========================================================================
    
    private void dec_parameters(){
        if(lookahead.token == Tokenizer.RSVP_PARA_N){
            prt("DEC_PARAMETERS",1014);
            nextToken();
            params_list();
        }
    }
    
    // =========================================================================
    // void params_list
    // Helps define params_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // dec_parameters: params | params params_list
    // =========================================================================
    
    private void params_list(){
        prt("PARAMS_LIST",1015);
        params();
        if(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            comma();
            params_list();
        }   
    }
    
    // =========================================================================
    // void params
    // Helps define params architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // params:  | IDENTIFIER [ARRAY LB RB] ret_type
    // =========================================================================
    
    private void params(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            Identifier new_id = new Identifier(lookahead.sequence);
            prt("PARAMS",1016);
            nextToken();
            if(lookahead.token == Tokenizer.RSVP_ARRA_N){
                nextToken();
                lb();
                rb();
            }
            ret_type(new_id);
            this.id_table.addIdentifier(new_id);
        }
    }
    
    // =========================================================================
    // void func_main
    // Helps define func_main architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // func_main : FUNCTION IDENTIFIER oper_type | MAIN
    // =========================================================================
    
    private void func_main(){
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            prt("FUNC_MAIN",1017);
            nextToken();
            Identifier new_id = new Identifier(lookahead.sequence);
            identifier();
            oper_type(new_id);
        }else if(lookahead.token == Tokenizer.RSVP_MAIN_N){
            prt("FUNC_MAIN",1018);
            nextToken();
        }else{
            
        }
    }
    
    // =========================================================================
    // void oper_type
    // Helps define oper_type architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // oper_type: RETURN chk_ptr chk_array ret_type
    // =========================================================================
    
    private void oper_type(Identifier id){
        if(lookahead.token == Tokenizer.RSVP_RETU_N){
            prt("OPER_TYPE",1019);
            nextToken();
            chk_ptr();
            chk_array();
            ret_type(id);
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void chk_ptr
    // Helps define chk_ptr architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // chk_ptr:  | POINTER {pointer_flag = true;}
    // =========================================================================
    
    private void chk_ptr(){
        //add pointer reserved word
    }
    
    // =========================================================================
    // void chk_array
    // Helps define chk_array architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // chk_ptr:  | ARRAY array_dim_list
    // =========================================================================
    
    private void chk_array(){
        if(lookahead.token == Tokenizer.RSVP_ARRA_N){
            prt("CHK_ARRAY",1020);
            nextToken();
            array_dim_list();
        }else{
            
        }
    }
    
    // =========================================================================
    // void array_dim_list
    // Helps define array_dim_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // array_dim_list: lb array_index rb | lb array_index rb array_dim_list
    // =========================================================================
    
    private void array_dim_list(){
        prt("ARRAY_DIM_LIST",1021);
        lb();
        array_index();
        rb();
        if(lookahead.token == Tokenizer.OPEN_BRACE_N){
            array_dim_list();
        }
    }
    
    // =========================================================================
    // void lb
    // Helps define the terminal "["
    // terminal
    // =========================================================================
    
    private void lb(){
        if(lookahead.token == Tokenizer.OPEN_BRACE_N){
            prt("LB",1022);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void array_index
    // Helps define array_index architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // array_index : IDENTIFIER | constant_value
    // =========================================================================
    
    private void array_index(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            prt("ARRAY_INDEX",1023);
            nextToken();
        }else if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            prt("ARRAY_INDEX",1023);
            nextToken();    
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void rb
    // Helps define the terminal "]"
    // terminal
    // =========================================================================
    
    private void rb(){
        if(lookahead.token == Tokenizer.CLOSE_BRACE_N){
            prt("RB",1024);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void ret_type
    // Helps define ret_type architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // ret_type : TYPE type_name | STRUCT IDENTIFIER | STRUCTYPE IDENTIFIER 
    // =========================================================================
    
    private void ret_type(Identifier id){
        if(lookahead.token == Tokenizer.RSVP_TYPE_N){
            prt("RET_TYPE",1025);
            nextToken();
            type_name(id);
        }else if(lookahead.token == Tokenizer.RSVP_STRU_N){
            prt("RET_TYPE",1025);
            nextToken();
            identifier();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void type_name
    // Helps define type_name architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // ret_type : TYPE type_name | STRUCT IDENTIFIER | STRUCTYPE IDENTIFIER 
    // =========================================================================
    
    private void type_name(Identifier id){
        if(lookahead.token == Tokenizer.RSVP_INTE_N){
            prt("TYPE_NAME",1026);
            id.setType(1);
            nextToken();
        }
        else if(lookahead.token == Tokenizer.RSVP_SHOR_N){
            prt("TYPE_NAME",1026);
            id.setType(3);
            nextToken();
        }
        else if(lookahead.token == Tokenizer.RSVP_MVOI_N){
            prt("TYPE_NAME",1026);
            id.setType(4);
            nextToken();
        }
        else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void specifications
    // Helps define specifications architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // ret_type :  | spec_list
    // =========================================================================
    
    private void specifications(){
        if(lookahead.token == Tokenizer.RSVP_SPEC_N){
            prt("SPECIFICATIONS",1027);
            nextToken();
            prt("SPEC_LIST",1028);
            spec_list();
        }else{
            
        }
    }
    
    // =========================================================================
    // void spec_list
    // Helps define spec_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // spec_list: spec_def | spec_list spec_def
    // =========================================================================
    
    private void spec_list(){
        spec_def();
        if(lookahead.token == Tokenizer.RSVP_ENUM_N || 
           lookahead.token == Tokenizer.RSVP_STRU_N){
            
            spec_list();
            
        }
    }
    
    // =========================================================================
    // void spec_def
    // Helps define spec_def architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // spec_def: ENUM | STRUCT
    // =========================================================================
    
    private void spec_def(){
        if(lookahead.token == Tokenizer.RSVP_ENUM_N){
            prt("SPEC_DEF",1029);
            nextToken();
        }else if(lookahead.token == Tokenizer.RSVP_STRU_N){
            prt("SPEC_DEF",1029);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void globals
    // Helps define spec_def architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // spec_def: ENUM | STRUCT
    // =========================================================================
    
    private void globals(){
        if(lookahead.token == Tokenizer.RSVP_GLOB_N){
            prt("GLOBALS",1030);
            nextToken();
            declarations();
        }else{
            
        }
    }
    
    // =========================================================================
    // void declarations
    // Helps define declarations architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // declarations: | DECLARATIONS const_dec var_dec | IS const_dec var_dec
    // =========================================================================
    
    private void declarations(){
        if(lookahead.token == Tokenizer.RSVP_DECL_N || 
           lookahead.token == Tokenizer.RSVP_IS_N){
            prt("DECLARATIONS",1031);
            nextToken();
            const_dec();
            var_dec();
            
        }else{
            
        }
    }
    
    // =========================================================================
    // void const_dec
    // Helps define const_dec architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // const_dec:  | CONSTANTS const_list
    // =========================================================================
    
    private void const_dec(){
        if(lookahead.token == Tokenizer.RSVP_CONS_N){
            prt("CONST_DEC",1032);
            nextToken();
            prt("CONST_LIST",1033);
            const_list();
        }else{
            //line_exception();
        }
    }
    
    // =========================================================================
    // void const_list
    // Helps define const_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // const_list: const_list DEFINE identifier rec_type equal_op constant_val
    // =========================================================================
    
    private void const_list(){
        define();
        Identifier new_id = new Identifier(lookahead.sequence);
        identifier();
        if(lookahead.token == Tokenizer.ASSIGNMENT_OPERATOR_N)
            equal_op();
        new_id.setValue(Integer.parseInt(lookahead.sequence));
        constant_val();
        of_dec();
        ret_type(new_id);
        if(lookahead.token == Tokenizer.RSVP_DEFI_N){
            const_list();
        }
        this.id_table.addIdentifier(new_id);
    }
    
    // =========================================================================
    // void equal_op
    // Helps define terminal "="
    // terminal
    // =========================================================================
    
    private void equal_op(){
        if(lookahead.token == Tokenizer.ASSIGNMENT_OPERATOR_N){
            prt("EQUAL_OP",1034);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void define
    // Helps define terminal "define"
    // terminal
    // =========================================================================
    
    private void define(){
        if(lookahead.token == Tokenizer.RSVP_DEFI_N){
            prt("DEFINE",1035);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void var_dec
    // Helps define var_dec architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // var_dec: VARIABLES var_list
    // =========================================================================
    
    private void var_dec(){
        if(lookahead.token == Tokenizer.RSVP_VARI_N){
            prt("VAR_DEC",1036);
            nextToken();
            prt("VAR_LIST",1037);
            var_list();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void var_list
    // Helps define var_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // var_list: DEFINE identifier chk_array of_dec ret_type var_list
    // =========================================================================
    
    private void var_list(){
        define();
        Identifier new_id = new Identifier(lookahead.sequence);
        identifier();
        chk_array();
        of_dec();
        ret_type(new_id);
        this.id_table.addIdentifier(new_id);
        if(lookahead.token == Tokenizer.RSVP_DEFI_N){
            var_list();
        }
    }
    
    // =========================================================================
    // void implement
    // Helps define implement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // implement: IMPLEMENTATIONS funct_list
    // =========================================================================
    
    private void implement(){
        if(lookahead.token == Tokenizer.RSVP_IMPL_N){
            prt("IMPLEMENT",1038);
            nextToken();
            prt("FUNC_LIST",1039);
            funct_list();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void func_list
    // Helps define func_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // func_list: funct_def | funct_list funct_def
    // =========================================================================
    
    private void funct_list(){
        funct_def();
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            funct_list();
        }
    }
    
    // =========================================================================
    // void func_def
    // Helps define func_def architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // func_def: funct_body
    // =========================================================================
    
    private void funct_def(){
        prt("FUNC_DEF",1040);
        funct_body();
    }
    
    // =========================================================================
    // void func_body
    // Helps define func_body architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // func_body: FUNCTION main_head parameters f_body
    // =========================================================================
    
    private void funct_body(){
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            prt("FUNC_BODY",1041);
            nextToken();
            main_head();
            parameters();
            f_body();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void main_head
    // Helps define main_head architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // main_head: MAIN | IDENTIFIER
    // =========================================================================
    
    private void main_head(){
        if(lookahead.token == Tokenizer.RSVP_MAIN_N){
            prt("MAIN_HEAD",1042);
            nextToken();
        }else if(lookahead.token == Tokenizer.IDENTIFIER_N){
            prt("MAIN_HEAD",1042);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void parameters
    // Helps define parameters architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // parameters:  | PARAMETERS param_list
    // =========================================================================
    
    private void parameters(){
        if(lookahead.token == Tokenizer.RSVP_PARA_N){
            prt("PARAMETERS",1043);
            nextToken();
            prt("PARAM_LIST",1044);
            param_list();
        }else{
            
        }
    }
    
    // =========================================================================
    // void param_list
    // Helps define param_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // param_list: param_def | param_def COMMA param_list 
    // =========================================================================
    
    private void param_list(){
        param_def();
        if(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            comma();
            param_list();
        }
        
    }
    
    // =========================================================================
    // void param_def
    // Helps define param_def architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // param_def: identifier chk_const chk_ptr chk_array of_dec ret_type
    // =========================================================================
    
    private void param_def(){
        prt("PARAM_DEF",1045);
        Identifier new_id = new Identifier(lookahead.sequence);
        identifier();
        chk_const();
        chk_ptr();
        chk_array();
        of_dec();
        ret_type(new_id);
        this.id_table.addIdentifier(new_id);
    }
    
    // =========================================================================
    // void chk_const
    // Helps define chk_const architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // chk_const:  | CONSTANT 
    // =========================================================================
    
    private void chk_const(){
        if(lookahead.token == Tokenizer.RSVP_CONS_N){
            prt("CHK_CONST",1046);
            nextToken();
        }else{
            
        }
    }
    
    // =========================================================================
    // void f_body
    // Helps define f_body architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // f_body: declarations BEGIN statement_list exit_statment ENDFUN main_head
    // =========================================================================
    
    private void f_body(){
        prt("F_BODY",1047);
        declarations();
        begin();
        statement_list();
        exit_statement();
        endfun();
        main_head();
    }
    
    // =========================================================================
    // void begin
    // Helps define terminal "begin"
    // terminal
    // =========================================================================
    
    private void begin(){
        if(lookahead.token == Tokenizer.RSVP_BEGI_N){
            prt("BEGIN",1048);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void endfun
    // Helps define terminal "endfun"
    // terminal
    // =========================================================================
    
    private void endfun(){
        if(lookahead.token == Tokenizer.RSVP_ENDF_N){
            prt("ENDFUN",1049);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void statement_list
    // Helps define statement_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // statement_list: statement | statement_list statement
    // =========================================================================
    
    private void statement_list(){
        statement();
        if     (lookahead.token == Tokenizer.RSVP_IF_N ||
                lookahead.token == Tokenizer.RSVP_REPE_N||
                lookahead.token == Tokenizer.RSVP_SET_N||
                lookahead.token == Tokenizer.RSVP_WHIL_N||
                lookahead.token == Tokenizer.RSVP_DISP_N||
                lookahead.token == Tokenizer.RSVP_FOR_N||
                lookahead.token == Tokenizer.RSVP_INPU_N){
            
            statement_list();
            
        }
    }
    
    // =========================================================================
    // void statement
    // Helps define statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // statement: if_statement
    //          | assignment_statement
    //		| while_statement
    //		| print_statement
    //		| repeat_statement
    //          | for_statement
    //          | input_statement
    // =========================================================================
    
    private void statement(){
        if(lookahead.token == Tokenizer.RSVP_IF_N){
            prt("STATEMENT",1050);
            if_statement();
        }else if(lookahead.token == Tokenizer.RSVP_REPE_N){
            prt("STATEMENT",1050);
            repeat_statement();
        }else if(lookahead.token == Tokenizer.RSVP_SET_N){
            prt("STATEMENT",1050);
            assignment_statement();
        }else if(lookahead.token == Tokenizer.RSVP_WHIL_N){
            prt("STATEMENT",1050);
            while_statement();
        }else if(lookahead.token == Tokenizer.RSVP_DISP_N){
            prt("STATEMENT",1050);
            print_statement();
        }else if(lookahead.token == Tokenizer.RSVP_FOR_N){
            prt("STATEMENT",1050);
            for_statement();
        }else if(lookahead.token == Tokenizer.RSVP_INPU_N){
            prt("STATEMENT",1050);
            input_statement();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void statement_list
    // Helps define statement_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // statement_list: statement | statement_list statement
    // =========================================================================
    
    private void for_statement(){
        if(lookahead.token == Tokenizer.RSVP_FOR_N){
            prt("FOR_STATEMENT",1051);
            nextToken();
            verification();
        }
    }
    
    // =========================================================================
    // void verification
    // Helps define verification architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // verification: IDENTIFIER equal_op constant_val reach do_statement 
    //               statement endfor
    // =========================================================================
    
    private void verification(){
        prt("VERIFICATION",1052);
        Identifier new_id = new Identifier(lookahead.sequence);
        identifier();
        equal_op();
        new_id.setValue(Integer.parseInt(lookahead.sequence));
        this.id_table.addIdentifier(new_id);
        constant_val();
        reach();
        do_statement();
        statement();
        endfor();
    }
    
    // =========================================================================
    // void reach
    // Helps define reach architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // reach: TO arithmetic_exp
    // =========================================================================
    
    private void reach(){
        if(lookahead.token == Tokenizer.RSVP_TO_N){
            prt("REACH",1053);
            nextToken();
            ArithmeticExpression ae = new ArithmeticExpression();
            arithmetic_exp(ae);
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void endfor
    // Helps define terminal "endfor"
    // terminal
    // =========================================================================
    
    private void endfor(){
        if(lookahead.token == Tokenizer.RSVP_ENDO_N){
            prt("END_FOR",1054);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void if_statement
    // Helps define if_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // reach: IF boolean_expression then statement_list else_statement 
    //        statement_list endif
    // =========================================================================
    
    private void if_statement(){
        if(lookahead.token == Tokenizer.RSVP_IF_N){
            prt("IF_STATEMENT",1055);
            nextToken();
            boolean_expression();
            then();
            statement_list();
            else_statement();
            statement_list();
            endif();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void then
    // Helps define terminal "then"
    // terminal
    // =========================================================================
    
    private void then(){
        if(lookahead.token == Tokenizer.RSVP_THEN_N){
            prt("THEN",1056);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void else
    // Helps define terminal "else"
    // terminal
    // =========================================================================
    
    private void else_statement(){
        if(lookahead.token == Tokenizer.RSVP_ELSE_N){
            prt("ELSE",1057);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void endif
    // Helps define terminal "endif"
    // terminal
    // =========================================================================
    
    private void endif(){
        if(lookahead.token == Tokenizer.RSVP_ENDI_N){
            prt("ENDIF",1058);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void while_statement
    // Helps define while_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // while_statement: WHILE boolean_expression DO statement_list ENDWHILE
    // =========================================================================
    
    private void while_statement(){
        if(lookahead.token == Tokenizer.RSVP_WHIL_N){
            prt("WHILE_STATEMENT",1059);
            nextToken();
            boolean_expression();
            do_statement();
            statement_list();
            end_while();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void do_statement
    // Helps define terminal "do"
    // terminal
    // =========================================================================
    
    private void do_statement(){
        if(lookahead.token == Tokenizer.RSVP_DO_N){
            prt("DO",1060);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void end_while
    // Helps define terminal "endwhile"
    // terminal
    // =========================================================================
    
    
    private void end_while(){
        if(lookahead.token == Tokenizer.RSVP_ENDW_N){
            prt("END_WHILE",1061);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void repeat_statement
    // Helps define repeat_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // repeat_statement: REPEAT statement_list UNTIL boolean_expression 
    //                   ENDREPEAT
    // =========================================================================
    
    private void repeat_statement(){
        if(lookahead.token == Tokenizer.RSVP_REPE_N){
            prt("REPEAT_STATEMENT",1062);
            nextToken();
            statement_list();
            until();
            boolean_expression();
            end_repeat();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void until
    // Helps define terminal "until"
    // terminal
    // =========================================================================
    
    private void until(){
        if(lookahead.token == Tokenizer.RSVP_UNTI_N){
            prt("UNTIL",1063);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void end_repeat
    // Helps define terminal "endrepeat"
    // terminal
    // =========================================================================
    
    private void end_repeat(){
        if(lookahead.token == Tokenizer.RSVP_ENDR_N){
            prt("END_REPEAT",1064);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void asssignement_statement
    // Helps define assignment_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // assignment_statement: SET identifier assignment_operator 
    //                       arithmetic_expression
    // =========================================================================
    
    private void assignment_statement(){
        if(lookahead.token == Tokenizer.RSVP_SET_N){
            prt("ASSIGNMENT_STATEMENT",1065);
            nextToken();
            Identifier id = new Identifier(lookahead.sequence);
            identifier();
            assignment_operator();
            ArithmeticExpression ae = new ArithmeticExpression();
            arithmetic_exp(ae);
            id.setValue(ArithmeticExpression.eval(ae));
            this.id_table.addIdentifier(id);
            
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void assignment_operator
    // Helps define terminal "="
    // terminal
    // =========================================================================
    
    private void assignment_operator(){
        if(lookahead.token == Tokenizer.ASSIGNMENT_OPERATOR_N){
            prt("ASSIGNMENT_OPERATOR",1066);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void input_statement
    // Helps define input_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // input_statement: INPUT arg_list
    // =========================================================================
    
    private void input_statement(){
        if(lookahead.token == Tokenizer.RSVP_INPU_N){
            prt("INPUT_STATEMENT",1067);
            nextToken();
            ArgumentsList al = new ArgumentsList();
            quote();
            text_value(al);
            quote();
            comma();
            Identifier id = new Identifier(lookahead.sequence);
            identifier();
            InputStatement is = new InputStatement(al.getText(),id);
            is.execute();
            this.id_table.addIdentifier(id);
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void print_statement
    // Helps define print_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // print_statement: DISPLAY arg_list
    // =========================================================================
    
    private void print_statement(){
        if(lookahead.token == Tokenizer.RSVP_DISP_N){
            prt("PRINT_STATEMENT",1068);
            nextToken();
            ArgumentsList al = new ArgumentsList();
            prt("ARG_LIST",1069);
            arg_list(al);
            DisplayStatement ps = new DisplayStatement(al);
            ps.execute();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void arg_list
    // Helps define arg_list architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // arg_list: args | args comma arg_list
    // =========================================================================
    
    private void arg_list(ArgumentsList al){
        args(al);
        if(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            comma();
            arg_list(al);
        }
    }
    
    // =========================================================================
    // void comma
    // Helps define terminal ","
    // terminal
    // =========================================================================
    
    private void comma(){
        if(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            prt("COMMA",1070);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void args
    // Helps define args architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // args : IDENTIFIER [array_dim_list] | quote text_value quote 
    //      | constant_val
    // =========================================================================
    
    private void args(ArgumentsList al){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            prt("ARGS",1071);
            Identifier id = new Identifier(lookahead.sequence);
            id = this.id_table.searchIdentifier(id);
            Argument ar = new Argument(id);
            al.addArgument(ar);
            identifier();
            if(lookahead.token == Tokenizer.OPEN_BRACE_N){
                array_dim_list();
            }
        }else if(lookahead.token == Tokenizer.LITERAL_QUOTE_N){
            prt("ARGS",1071);
            nextToken();
            text_value(al);
            quote();
        }else if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            prt("ARGS",1071);
            constant_val();
        }
        else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void boolean_expression
    // Helps define boolean_expression architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // boolean_expression: arithmetic_exp relative_op arithmetic_exp
    // =========================================================================
    
    private void boolean_expression(){
        prt("BOOLEAN_EXPRESSION",1072);
        ArithmeticExpression left_pane = new ArithmeticExpression();
        arithmetic_exp(left_pane);
        int operator = lookahead.token;
        relative_op();
        ArithmeticExpression right_pane = new ArithmeticExpression();
        arithmetic_exp(left_pane);
        
        BooleanExpression be = new BooleanExpression(left_pane, operator, right_pane);
        
        prt("*************The result of this boolean expression is "+be.evaluate(),1072);
    }
    
    // =========================================================================
    // void relative_op
    // Helps define relative_op architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // relative_op: le_operator | lt_operator | ge_operator | gt_operator 
    //            | eq_operator | ne_operator
    // =========================================================================
    
    private void relative_op(){
        if(lookahead.token == Tokenizer.LE_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            le_operator();
        }else if(lookahead.token == Tokenizer.LT_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            lt_operator();
        }else if(lookahead.token == Tokenizer.GE_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            ge_operator();
        }else if(lookahead.token == Tokenizer.GT_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            gt_operator();
        }else if(lookahead.token == Tokenizer.EQ_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            eq_operator();
        }else if(lookahead.token == Tokenizer.NE_OPERATOR_N){
            prt("RELATIVE_OP",1073);
            ne_operator();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void le_operator
    // Helps define terminal "<="
    // terminal
    // =========================================================================
    
    private void le_operator(){
        if(lookahead.token == Tokenizer.LE_OPERATOR_N){
            prt("LE_OPERATOR",1074);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void lt_operator
    // Helps define terminal "<"
    // terminal
    // =========================================================================
    
    private void lt_operator(){
        if(lookahead.token == Tokenizer.LT_OPERATOR_N){
            prt("LT_OPERATOR",1075);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void ge_operator
    // Helps define terminal "=>"
    // terminal
    // =========================================================================
    
    private void ge_operator(){
        if(lookahead.token == Tokenizer.GE_OPERATOR_N){
            prt("GE_OPERATOR",1076);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void gt_operator
    // Helps define terminal ">"
    // terminal
    // =========================================================================
    
    private void gt_operator(){
        if(lookahead.token == Tokenizer.GT_OPERATOR_N){
            prt("GT_OPERATOR",1077);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void eq_operator
    // Helps define terminal "=="
    // terminal
    // =========================================================================
    
    private void eq_operator(){
        if(lookahead.token == Tokenizer.EQ_OPERATOR_N){
            prt("EQ_OPERATOR",1078);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void ne_operator
    // Helps define terminal "~="
    // terminal
    // =========================================================================
    
    private void ne_operator(){
        if(lookahead.token == Tokenizer.NE_OPERATOR_N){
            prt("NE_OPERATOR",1079);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void arithmetic_expression
    // Helps define arithmetic_expression architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // arithmetic_expression: arithmetic_exp add_operator mulexp
    //                      | arithmetic_exp sub_operator mulexp
    //                      | mulexp
    // =========================================================================
    
    private void arithmetic_exp(ArithmeticExpression ae){
        prt("ARITHMETIC_EXPRESSION",1080);
        ArithmeticExpression oper_conv = new ArithmeticExpression(Tokenizer.ADD_OPERATOR_N,"+");
        ArithmeticExpression right_span = new ArithmeticExpression(Tokenizer.LITERAL_INTEGER_N,"0");
        ae = oper_conv;
        ae.setRightNode(right_span);
        mulexp(ae.getLeftNode());
        if(lookahead.token == Tokenizer.ADD_OPERATOR_N){
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            add_operator();
            arithmetic_exp(ae.getRightNode());
        }else if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            sub_operator();
            arithmetic_exp(ae.getRightNode());
        }
       
    }
    
    // =========================================================================
    // void add_operator
    // Helps define terminal "+"
    // terminal
    // =========================================================================
    
    private void add_operator(){
        if(lookahead.token == Tokenizer.ADD_OPERATOR_N){
            prt("ADD_OPERATOR",1081);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void sub_operator
    // Helps define terminal "-"
    // terminal
    // =========================================================================
    
    private void sub_operator(){
        if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            prt("SUB_OPERATOR",1082);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void mul_expression
    // Helps define mul_expression architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // mul_expression: mulexp mul_operator primary
    //               | mulexp div_operator primary
    //               | primary
    // =========================================================================
    
    private void mulexp(ArithmeticExpression ae){
        prt("MULEXP",1083);
        ArithmeticExpression oper_conv = new ArithmeticExpression(Tokenizer.ADD_OPERATOR_N,"+");
        ArithmeticExpression right_span = new ArithmeticExpression(Tokenizer.LITERAL_INTEGER_N,"0");
        ae = oper_conv;
        ae.setRightNode(right_span);
        primary(ae.getLeftNode());
        if(lookahead.token == Tokenizer.MUL_OPERATOR_N){
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            mul_operator();
            mulexp(ae.getRightNode());
        }else if(lookahead.token == Tokenizer.DIV_OPERATOR_N){
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            div_operator();
            mulexp(ae.getRightNode());
        }
        
    }
    
    // =========================================================================
    // void mul_operator
    // Helps define terminal "*"
    // terminal
    // =========================================================================
    
    private void mul_operator(){
        if(lookahead.token == Tokenizer.MUL_OPERATOR_N){
            prt("MUL_OPERATOR",1084);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void div_operator
    // Helps define terminal "/"
    // terminal
    // =========================================================================
    
    private void div_operator(){
        if(lookahead.token == Tokenizer.DIV_OPERATOR_N){
            prt("DIV_OPERATOR",1085);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void primary
    // Helps define primary architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // mul_expression: left_paren  arithmetic_exp right_paren
    //               | minus primary
    //               | constant_val
    //               | identifier
    // =========================================================================
    
    private void primary(ArithmeticExpression ae){
        if(lookahead.token == Tokenizer.OPEN_BRACKET_N){
            prt("PRIMARY",1086);
            left_paren();
            arithmetic_exp(ae);
            right_paren();
        }else if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            prt("PRIMARY",1086);
            ArithmeticExpression new_ae = new ArithmeticExpression(Tokenizer.LITERAL_INTEGER_N, "0");
            ae.setLeftNode(new_ae);
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            minus();
            primary(ae.getRightNode());
        }else if(lookahead.token == Tokenizer.IDENTIFIER_N){
            prt("PRIMARY",1086);
            Identifier id = new Identifier(lookahead.sequence);
            ae.setSequence(Integer.toString(this.id_table.getValueIdentifier(id)));
            identifier();
            if(lookahead.token == Tokenizer.OPEN_BRACKET_N){
                ArgumentsList al = new ArgumentsList();
                nextToken();
                arg_list(al);
                right_paren();
            }
        }else if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            prt("PRIMARY",1086);
            ae.setSequence(lookahead.sequence);
            ae.setTokenCode(lookahead.token);
            constant_val();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void left_paren
    // Helps define terminal "("
    // terminal
    // =========================================================================
    
    private void left_paren(){
        if(lookahead.token == Tokenizer.OPEN_BRACKET_N){
            prt("LEFT_PAREN",1087);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void right_paren
    // Helps define terminal ")"
    // terminal
    // =========================================================================
    
    private void right_paren(){
        if(lookahead.token == Tokenizer.CLOSE_BRACKET_N){
            prt("RIGHT_PAREN",1088);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void minus
    // Helps define terminal "-"
    // terminal
    // =========================================================================
    
    private void minus(){
        if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            prt("UNARY_MINUS",1089);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void constant_value
    // Helps define terminal integer
    // terminal
    // =========================================================================
    
    private void constant_val(){
        if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            prt("CONSTANT_VAL",1090);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void text_value
    // Helps define terminal string taking any value
    // terminal
    // =========================================================================
    
    private void text_value(ArgumentsList al){
        prt("TEXT_VALUE",1091);
        Argument ar = new Argument("");
        while(lookahead.token != Tokenizer.LITERAL_QUOTE_N){
            Argument.appendArg(ar, lookahead.sequence);
            nextToken();
        }
        al.addArgument(ar);
    }
    
    // =========================================================================
    // void exit_statement
    // Helps define exit_statement architecture
    // non-terminal
    // GRAMMAR CODE:
    // =========================================================================
    // exit_statement: RETURN args | EXIT
    // =========================================================================
    
    private void exit_statement(){
        if(lookahead.token == Tokenizer.RSVP_RETU_N){
            prt("EXIT_STATEMENT",1092);
            nextToken();
            ArgumentsList al = new ArgumentsList();
            args(al);
        }else if(lookahead.token == Tokenizer.RSVP_EXIT_N){
            prt("EXIT_STATEMENT",1092);
            nextToken();
        }else{
            line_exception();
        }
    }
    
    // =========================================================================
    // void of_dec
    // Helps define terminal "of"
    // terminal
    // =========================================================================
    
    private void of_dec(){
        if(lookahead.token == Tokenizer.RSVP_OF_N){
            prt("OF_DEC",1093);
            nextToken();
        }else{
            
        }
    }
    
    
}