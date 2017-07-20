// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      WhileStatement.java
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
public class WhileStatement extends Statement {
    
    // =========================================================================
    // Instance variables
    // @param BooleanExpression be defines the condition to meet
    // @param StatementList sl is the list of statements to execute
    // =========================================================================
    
    private BooleanExpression be;
    private StatementList sl;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // takes a Boolean Expressiona and a Statement List
    // =========================================================================
    
    public WhileStatement(BooleanExpression boe, StatementList sl){
        super(Tokenizer.RSVP_WHIL_N);
        this.be = boe;
        this.sl = sl;
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Proceeds with the execution of the loop while the condition is met
    // =========================================================================
    
    @Override
    public void execute(){
        while(this.be.evaluate()){
            for(Statement st: sl.getStatements()){
                st.execute();
            }
        }
    }
    
}
