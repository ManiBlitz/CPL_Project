/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import exceptionsPack.ParserException;
import java.util.LinkedList;
import scanner.Token;
import scanner.Tokenizer;

/**
 *
 * @author ADMIN
 */
public class Parser {
    LinkedList<Token> tokens;
    //Tokenizer tokenizer = Tokenizer.initTokenizer();
    Token lookahead;
    final String ERROR_MESSAGE = "Unexpected Symbol: \"";
    final String ERROR_LINE = "\" at line ";
    
    private void line_exception(){
        throw new ParserException(ERROR_MESSAGE + lookahead.sequence + ERROR_LINE + lookahead.row_num);
    }
    
    public void parse(LinkedList<Token> tokens)
    {
        this.tokens = (LinkedList<Token>) tokens.clone();
        lookahead = this.tokens.getFirst();

        start();

        if (lookahead.token != Tokenizer.EPSILON)
          throw new ParserException("Unexpected symbol "+ lookahead.sequence+" found");
    }
    
    private void nextToken()
    {
        int last_line = tokens.getFirst().row_num + 1;
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
          lookahead = new Token(Tokenizer.EPSILON, "", -1,last_line);
        else
          lookahead = tokens.getFirst();
    }

    
    
    private void start() {
        symbols();
        forward_refs();
        specifications();
        globals();
        implement();
    }
    
    private void symbols(){
        symbol_def();
        while(lookahead.token == Tokenizer.RSVP_SYMB_N){
            symbols();
        }
    }
    
    private void symbol_def(){
        if(lookahead.token == Tokenizer.RSVP_SYMB_N){
            nextToken();
            identifier();
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void identifier(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void forward_refs(){
        forward();
        frefs();
    }
    
    private void forward(){
        if(lookahead.token == Tokenizer.RSVP_FOWA_N){
            nextToken();
        }else{
           line_exception(); 
        }
    }
    
    private void frefs(){
        if(lookahead.token == Tokenizer.RSVP_REFE_N){
            nextToken();
        }else if(lookahead.token == Tokenizer.RSVP_DECL_N){
            nextToken();
        }
        foward_list();
    }
    
    private void foward_list(){
        fowards();
        while(lookahead.token == Tokenizer.RSVP_FUNC_N||
                lookahead.token == Tokenizer.RSVP_MAIN_N){
            
            fowards();
            
        }
    }
    
    private void fowards(){
        func_main();
        //dec_parameters();
    }
    
    private void func_main(){
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            nextToken();
            identifier();
            oper_type();
        }else if(lookahead.token == Tokenizer.RSVP_MAIN_N){
            nextToken();
        }else{
            
        }
    }
    
    private void oper_type(){
        if(lookahead.token == Tokenizer.RSVP_RETU_N){
            nextToken();
            chk_ptr();
            chk_array();
            ret_type();
        }else{
            line_exception();
        }
    }
    
    private void chk_ptr(){
        //add pointer reserved word
    }
    
    private void chk_array(){
        if(lookahead.token == Tokenizer.RSVP_ARRA_N){
            nextToken();
            array_dim_list();
        }else{
            
        }
    }
    
    private void array_dim_list(){
        lb();
        array_index();
        rb();
        while(lookahead.token == Tokenizer.OPEN_BRACE_N){
            lb();
            array_index();
            rb();
        }
    }
    
    private void lb(){
        if(lookahead.token == Tokenizer.OPEN_BRACE_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void array_index(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            nextToken();
        }else if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            nextToken();    
        }else{
            line_exception();
        }
    }
    
    private void rb(){
        if(lookahead.token == Tokenizer.CLOSE_BRACE_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void ret_type(){
        if(lookahead.token == Tokenizer.RSVP_TYPE_N){
            nextToken();
            type_name();
        }else if(lookahead.token == Tokenizer.RSVP_STRU_N){
            nextToken();
            identifier();
        }else{
            line_exception();
        }
    }
    
    private void type_name(){
        if(lookahead.token == Tokenizer.RSVP_INTE_N){
            nextToken();
        }
        /*else if(lookahead.token == Tokenizer.RSVP_SHORT){
            nextToken();
        }
        else if(lookahead.token == Tokenizer.RSVP_MVOID){
            nextToken();
        }*/
        else{
            line_exception();
        }
    }
    
    private void specifications(){
        if(lookahead.token == Tokenizer.RSVP_SPEC_N){
            nextToken();
            spec_list();
        }else{
            
        }
    }
    
    private void spec_list(){
        spec_def();
        while(lookahead.token == Tokenizer.RSVP_ENUM_N || lookahead.token == Tokenizer.RSVP_STRU_N){
            spec_def();
        }
    }
    
    private void spec_def(){
        if(lookahead.token == Tokenizer.RSVP_ENUM_N){
            nextToken();
        }else if(lookahead.token == Tokenizer.RSVP_STRU_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void globals(){
        if(lookahead.token == Tokenizer.RSVP_GLOB_N){
            nextToken();
            declarations();
        }else{
            
        }
    }
    
    private void declarations(){
        if(lookahead.token == Tokenizer.RSVP_DECL_N){
            nextToken();
            const_dec();
            var_dec();
        }else{
            
        }
    }
    
    private void const_dec(){
        if(lookahead.token == Tokenizer.RSVP_CONS_N){
            nextToken();
            const_list();
        }else{
            line_exception();
        }
    }
    
    private void const_list(){
        define();
        identifier();
        //rec_type();
        equal_op();
        constant_val();
        ret_type();
        while(lookahead.token == Tokenizer.RSVP_DEFI_N){
            define();
            identifier();
            //rec_type();
            equal_op();
            constant_val();
            ret_type();
        }
    }
    
    private void equal_op(){
        if(lookahead.token == Tokenizer.ASSIGNMENT_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void define(){
        if(lookahead.token == Tokenizer.RSVP_DEFI_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void var_dec(){
        if(lookahead.token == Tokenizer.RSVP_VARI_N){
            nextToken();
            var_list();
        }else{
            line_exception();
        }
    }
    
    private void var_list(){
        define();
        identifier();
        //rec_type();
        ret_type();
        while(lookahead.token == Tokenizer.RSVP_DEFI_N){
            define();
            identifier();
            //rec_type();
            ret_type();
        }
    }
    
    private void implement(){
        if(lookahead.token == Tokenizer.RSVP_IMPL_N){
            nextToken();
            funct_list();
        }else{
            line_exception();
        }
    }
    
    private void funct_list(){
        funct_def();
        while(lookahead.token == Tokenizer.RSVP_FUNC_N){
            funct_def();
        }
    }
    
    private void funct_def(){
        funct_body();
    }
    
    private void funct_body(){
        if(lookahead.token == Tokenizer.RSVP_FUNC_N){
            main_head();
            parameters();
            f_body();
        }else{
            line_exception();
        }
    }
    
    private void main_head(){
        if(lookahead.token == Tokenizer.RSVP_MAIN_N){
            nextToken();
        }else if(lookahead.token == Tokenizer.IDENTIFIER_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void parameters(){
        if(lookahead.token == Tokenizer.RSVP_PARA_N){
            nextToken();
            param_list();
        }else{
            
        }
    }
    
    private void param_list(){
        param_def();
        while(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            comma();
            param_def();
        }
        
    }
    
    private void param_def(){
        identifier();
        chk_const();
        chk_ptr();
        chk_array();
        type();
        type_name();
    }
    
    private void type(){
        if(lookahead.token == Tokenizer.RSVP_TYPE_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void chk_const(){
        if(lookahead.token == Tokenizer.RSVP_CONS_N){
            nextToken();
        }else{
            
        }
    }
    
    private void f_body(){
        declarations();
        begin();
        statement_list();
        endfun();
    }
    
    private void begin(){
        if(lookahead.token == Tokenizer.RSVP_BEGI_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void endfun(){
        if(lookahead.token == Tokenizer.RSVP_ENDF_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void statement_list(){
        statement();
        while(lookahead.token == Tokenizer.RSVP_IF_N ||
                lookahead.token == Tokenizer.RSVP_REPE_N||
                lookahead.token == Tokenizer.RSVP_SET_N||
                lookahead.token == Tokenizer.RSVP_WHIL_N||
                lookahead.token == Tokenizer.RSVP_PRIN_N){
            
            statement();
            
        }
    }
    
    private void statement(){
        if(lookahead.token == Tokenizer.RSVP_IF_N){
            if_statement();
        }else if(lookahead.token == Tokenizer.RSVP_REPE_N){
            repeat_statement();
        }else if(lookahead.token == Tokenizer.RSVP_SET_N){
            assignment_statement();
        }else if(lookahead.token == Tokenizer.RSVP_WHIL_N){//Define the WHILE RSVP
            while_statement();
        }else if(lookahead.token == Tokenizer.RSVP_PRIN_N){
            print_statement();
        }else{
            line_exception();
        }
    }
    
    private void if_statement(){
        if(lookahead.token == Tokenizer.RSVP_IF_N){
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
    
    private void then(){
        if(lookahead.token == Tokenizer.RSVP_THEN_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void else_statement(){
        if(lookahead.token == Tokenizer.RSVP_ELSE_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void endif(){
        if(lookahead.token == Tokenizer.RSVP_ENDI_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void while_statement(){
        if(lookahead.token == Tokenizer.RSVP_WHIL_N){
            nextToken();
            boolean_expression();
            do_statement();
            statement_list();
            end_while();
        }else{
            line_exception();
        }
    }
    
    private void do_statement(){
        if(lookahead.token == Tokenizer.RSVP_DO_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void end_while(){
        if(lookahead.token == Tokenizer.RSVP_ENDW_N){//Add endwhile to the rsvp
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void repeat_statement(){
        if(lookahead.token == Tokenizer.RSVP_REPE_N){
            nextToken();
            statement_list();
            until();
            boolean_expression();
            end_repeat();
        }else{
            line_exception();
        }
    }
    
    private void until(){
        if(lookahead.token == Tokenizer.RSVP_UNTI_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void end_repeat(){
        if(lookahead.token == Tokenizer.RSVP_ENDR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void assignment_statement(){
        if(lookahead.token == Tokenizer.RSVP_SET_N){
            nextToken();
            identifier();
            assignment_operator();
            arithmetic_exp();
        }else{
            line_exception();
        }
    }
    
    private void assignment_operator(){
        if(lookahead.token == Tokenizer.ASSIGNMENT_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void print_statement(){
        if(lookahead.token == Tokenizer.RSVP_DISP_N){
            nextToken();
            arg_list();
        }else{
            line_exception();
        }
    }
    
    private void arg_list(){
        args();
        while(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            comma();
            args();
        }
    }
    
    private void comma(){
        if(lookahead.token == Tokenizer.LITERAL_COMMA_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void args(){
        if(lookahead.token == Tokenizer.IDENTIFIER_N){
            identifier();
        }else{
            line_exception();
        }
    }
    
    private void boolean_expression(){
        arithmetic_exp();
        relative_op();
        arithmetic_exp();
    }
    
    private void relative_op(){
        if(lookahead.token == Tokenizer.LE_OPERATOR_N){
            le_operator();
        }else if(lookahead.token == Tokenizer.LT_OPERATOR_N){
            lt_operator();
        }else if(lookahead.token == Tokenizer.GE_OPERATOR_N){
            ge_operator();
        }else if(lookahead.token == Tokenizer.GT_OPERATOR_N){
            gt_operator();
        }else if(lookahead.token == Tokenizer.EQ_OPERATOR_N){
            eq_operator();
        }else if(lookahead.token == Tokenizer.NE_OPERATOR_N){
            ne_operator();
        }else{
            line_exception();
        }
    }
    
    private void le_operator(){
        if(lookahead.token == Tokenizer.LE_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void lt_operator(){
        if(lookahead.token == Tokenizer.LT_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void ge_operator(){
        if(lookahead.token == Tokenizer.GE_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void gt_operator(){
        if(lookahead.token == Tokenizer.GT_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void eq_operator(){
        if(lookahead.token == Tokenizer.EQ_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void ne_operator(){
        if(lookahead.token == Tokenizer.NE_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void arithmetic_exp(){
        //arithmetic_exp();
        
        mulexp();
        if(lookahead.token == Tokenizer.ADD_OPERATOR_N){
            add_operator();
        }else if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            sub_operator();
        }
        while(lookahead.token == Tokenizer.OPEN_BRACKET_N||
                lookahead.token == Tokenizer.SUB_OPERATOR_N||
                lookahead.token == Tokenizer.IDENTIFIER_N||
                lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            
            mulexp();
            
        }
    }
    
    private void add_operator(){
        if(lookahead.token == Tokenizer.ADD_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void sub_operator(){
        if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void mulexp(){
        primary();
        if(lookahead.token == Tokenizer.MUL_OPERATOR_N){
            mul_operator();
        }else if(lookahead.token == Tokenizer.DIV_OPERATOR_N){
            div_operator();
        }
        while(lookahead.token == Tokenizer.OPEN_BRACKET_N||
                lookahead.token == Tokenizer.SUB_OPERATOR_N||
                lookahead.token == Tokenizer.IDENTIFIER_N||
                lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            
            primary();
            
        }
        
    }
    
    private void mul_operator(){
        if(lookahead.token == Tokenizer.MUL_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void div_operator(){
        if(lookahead.token == Tokenizer.DIV_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void primary(){
        if(lookahead.token == Tokenizer.OPEN_BRACKET_N){
            left_paren();
            arithmetic_exp();
            right_paren();
        }else if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            minus();
            primary();
        }else if(lookahead.token == Tokenizer.IDENTIFIER_N){
            identifier();
        }else if(lookahead.token == Tokenizer.LITERAL_INTEGER_N){
            constant_val();
        }
    }
    
    private void left_paren(){
        if(lookahead.token == Tokenizer.OPEN_BRACKET_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void right_paren(){
        if(lookahead.token == Tokenizer.CLOSE_BRACKET_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void minus(){
        if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    private void constant_val(){
        if(lookahead.token == Tokenizer.SUB_OPERATOR_N){
            nextToken();
        }else{
            line_exception();
        }
    }
    
    
    
}


