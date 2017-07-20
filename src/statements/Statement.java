// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      Statement.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import exceptionsPack.ExecuterException;
import parser.Identifier;
import scanner.Tokenizer;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class Statement {
    
    // =========================================================================
    // Instance variables
    // @param type_def define the statement type number
    // @param stm_type gives the statement type name
    // @iterator is a partciular instance variable used for loop definitions
    // =========================================================================
    
    private int type_def;
    private String stm_type;
    private Identifier iterator;
    
    // =========================================================================
    // Constant defined for type def
    // Will be used to define the statement type name
    // =========================================================================
    
    private final int if_statement = Tokenizer.RSVP_IF_N;
    private final int for_statement = Tokenizer.RSVP_FOR_N;
    private final int while_statement = Tokenizer.RSVP_WHIL_N;
    private final int print_statement = Tokenizer.RSVP_DISP_N;
    private final int do_statement = Tokenizer.RSVP_DO_N;
    private final int repeat_statement = Tokenizer.RSVP_REPE_N;
    private final int assign_statement = Tokenizer.RSVP_SET_N;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // @param int state
    // =========================================================================
    
    public Statement(int state){
        this.type_def = state;
        try{
           this.setStatementType(); 
        }catch(ExecuterException ee){
            System.out.print(ee.getMessage());
        }
        
    }
    
    // =========================================================================
    // void setStatementType
    // defines the statement name
    // =========================================================================
    
    private void setStatementType(){
        switch (this.type_def) {
            case if_statement:
                this.stm_type = "IF_STATEMENT";
                break;
            case for_statement:
                this.stm_type = "FOR_STATEMENT";
                break;
            case while_statement:
                this.stm_type = "WHILE_STATEMENT";
                break;
            case print_statement:
                this.stm_type = "DISPLAY_STATEMENT";
                break;
            case do_statement:
                this.stm_type = "DO_STATEMENT";
                break;
            case repeat_statement:
                this.stm_type = "REPEAT_STATEMENT";
                break;
            case assign_statement:
                this.stm_type = "ASSIGNMENT_STATEMENT";
                break;
        }
    }
    
    // =========================================================================
    // void setIterator
    // assign the Identifier object to the iterator for loop control
    // The identifier used is taken form the Identifier table
    // =========================================================================
    
    public void setIterator(Identifier id){
        this.iterator = id;
    }
    
    // =========================================================================
    // Identifier getIterator
    // This function returns the identifier used for iteration
    // This iterator is the same as the one found in the Identifier Table
    // =========================================================================
    
    public Identifier getIterator(){
        return this.iterator;
    }
    
    // =========================================================================
    // int getIteratorValue
    // retrieves the integer value of the iterator of the loop
    // =========================================================================
    
    public int getIteratorValue(){
        return this.iterator.getValue();
    }
    
    // =========================================================================
    // void update_iterator
    // Permits to modify the value assigned to the iterator
    // =========================================================================
    
    public void update_iterator(int new_value){
        this.iterator.setValue(new_value);
    }

    // =========================================================================
    // int getStatementCode
    // provides the statement code of the instance
    // =========================================================================
    
    public int getStatementCode(){
        return this.type_def;
    }
    
    // =========================================================================
    // String getStatementType
    // gives back the statement type of the statement used
    // =========================================================================
    
    public String getStatementType(){
        return this.stm_type;
    }
    
    // =========================================================================
    // void execute
    // This is a function that will be overridden by the childs of this class
    // It defines the execution of a statement
    // =========================================================================

    public void execute(){}
     
}
