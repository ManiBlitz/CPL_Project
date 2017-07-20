// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      ForStatement.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import scanner.Tokenizer;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class ForStatement extends Statement{
    
    // =========================================================================
    // Instance variables
    // @param int value_start gives the beginning point
    // @param int value_end gives the ending point
    // @param StatementList sl gives the list of statements ot be executed
    // =========================================================================
    
    private int value_start;
    private int value_end;
    private StatementList sl;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // Takes a statement list, a start point and end point
    // =========================================================================
    
    public ForStatement(StatementList sl,int start, int end){
        super(Tokenizer.RSVP_FOR_N);
        this.sl = sl;
        this.value_start = start;
        this.value_end = end;
    }
    
    // =========================================================================
    // boolean reach
    // Verifies if the starting value is equal to the ending point
    // =========================================================================
    
    private boolean reach(){
        return this.value_end == this.value_start;
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Proceeds with the execution of the loop until the condition is met
    // =========================================================================
    
    @Override
    public void execute(){
        while(!reach()){
            for(Statement st: this.sl.getStatements()){
                st.execute();
            }
            this.update_iterator(this.getIteratorValue() + 1);
        }
    }
    
    
    
    
}
