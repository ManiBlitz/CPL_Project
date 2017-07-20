// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      DisplayStatement.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import scanner.Tokenizer;
import statements.funct.Argument;
import statements.funct.ArgumentsList;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class DisplayStatement extends Statement{
    
    // =========================================================================
    // Instance variable
    // @param ArgumentList al
    // Will be used to list the different elements to print
    // =========================================================================
    
    private ArgumentsList al;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // Takes an ArgumentList
    // =========================================================================
    
    public DisplayStatement(ArgumentsList al){
        super(Tokenizer.RSVP_PRIN_N);
        this.al = al;
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Display the arguments in the command line
    // =========================================================================
    
    public void execute(){
        System.out.println();
        for(Argument ar: this.al.getArgumentList()){
            System.out.print(ar.getValueArg()+" ");
        }
        System.out.println("\n");
    }
    
    
    
}
