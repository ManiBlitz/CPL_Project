/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executer;

import scanner.Tokenizer;

/**
 *
 * @author ADMIN
 */
public class ArithmeticExpression {
    
    private int token_code = Tokenizer.LITERAL_INTEGER_N;
    private String sequence = "0";
    private ArithmeticExpression left = null;
    private ArithmeticExpression right = null;
    
    public ArithmeticExpression(int code, String seq){
        this.token_code = code;
        this.sequence = seq;
    }
    
    public ArithmeticExpression(){}
    
    public void setLeftNode(ArithmeticExpression new_left){
        this.left = new_left;
    }
    
    public void setRightNode(ArithmeticExpression new_right){
        this.right = new_right;
    }
    
    public void setSequence(String seq){
        this.sequence = seq;
    }
    
    public void setTokenCode(int code){
        this.token_code = code;
    }
    
    public String getSequence(){
        return this.sequence;
    }
    
    public int getTokenCode(){
        return this.token_code;
    }
    
    public ArithmeticExpression getLeftNode(){
        this.left = new ArithmeticExpression();
        return this.left;
    }
    
    public ArithmeticExpression getRightNode(){
        this.right = new ArithmeticExpression();
        return this.right;
    }
    
    public static int eval(ArithmeticExpression ae){
        int value = 0;
        switch (ae.getTokenCode()) {
            case Tokenizer.IDENTIFIER_N:
                value = Integer.parseInt(ae.getSequence());
                break;
            case Tokenizer.LITERAL_INTEGER_N:
                value = Integer.parseInt(ae.getSequence());
                break;
            case Tokenizer.ADD_OPERATOR_N:
                value = eval(ae.getLeftNode()) + eval (ae.getRightNode());
                break;
            case Tokenizer.SUB_OPERATOR_N:
                value = eval(ae.getLeftNode()) - eval(ae.getRightNode());
                break;
            case Tokenizer.MUL_OPERATOR_N:
                value = eval(ae.getLeftNode()) * eval(ae.getRightNode());
                break;
            case Tokenizer.DIV_OPERATOR_N:
                value = eval(ae.getLeftNode()) / eval(ae.getRightNode());
                break;
            default:
                break;
        }
        return value;
    }
    
}


