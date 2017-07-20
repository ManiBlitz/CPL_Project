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
public class BooleanExpression {
    
    private ArithmeticExpression left;
    private ArithmeticExpression right;
    private int op;
    
    public BooleanExpression(ArithmeticExpression l, int op, ArithmeticExpression r){
        this.left = l;
        this.right = r;
        this.op = op;
    }
    
    public ArithmeticExpression getLeft(){
        return this.left;
    }
    
    public ArithmeticExpression getRight(){
        return this.right;
    }
    
    public int getOperator(){
        return this.op;
    }
    
    public boolean evaluate(){
        int left_value = ArithmeticExpression.eval(this.left);
        int right_value = ArithmeticExpression.eval(this.right);
        boolean result = false;
        
        switch (this.getOperator()) {
            case Tokenizer.EQ_OPERATOR_N:
                result = left_value == right_value;
                break;
            case Tokenizer.GE_OPERATOR_N:
                result = left_value <= right_value;
                break;
            case Tokenizer.GT_OPERATOR_N:
                result = left_value < right_value;
                break;
            case Tokenizer.LE_OPERATOR_N:
                result = left_value >= right_value;
                break;
            case Tokenizer.LT_OPERATOR_N:
                result = left_value > right_value;
                break;
            case Tokenizer.NE_OPERATOR_N:
                result = left_value == right_value; //We have integers and it is the only valid representations
                break;
            default:
                break;
        }
        
        return result;
        
    }

}
