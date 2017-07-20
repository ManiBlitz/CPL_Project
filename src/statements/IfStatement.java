// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      IfStatement.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import executer.BooleanExpression;
import scanner.Tokenizer;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class IfStatement extends Statement{
    
    // =========================================================================
    // Instance variables
    // @param BooleanExpression be represent the expression to be evaluated 
    // @param StatementList sl represents the different statements interior
    // =========================================================================
    
    BooleanExpression be;
    StatementList sl;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // @param BooleanExpression be
    // =========================================================================
    
    public IfStatement(BooleanExpression be){
        super(Tokenizer.RSVP_IF_N);
        this.be = be;
    }
    
    // =========================================================================
    // boolean getResult
    // provides the result of the boolean expression
    // =========================================================================
    
    public boolean getResult(){
        return be.evaluate();
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Verifies the condition then either execute or not the statement list
    // =========================================================================
    
    @Override
    public void execute(){
        if(this.getResult()){
            for(Statement st: sl.getStatements()){
                st.execute();
            }
        }
    }
    
}
