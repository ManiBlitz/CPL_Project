// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      RepeatStatement.java
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
public class RepeatStatement extends Statement{
    
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
    
    public RepeatStatement(StatementList sl, BooleanExpression be){
        super(Tokenizer.RSVP_REPE_N);
        this.sl = sl;
        this.be = be;
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Execute the loop and keeps doing so while the condition is met
    // =========================================================================
    
    @Override
    public void execute(){
        do{
           for(Statement st: this.sl.getStatements()){
               st.execute();
           } 
        }while(this.be.evaluate());
    }
    
}
