// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      InputStatement.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import parser.Identifier;
import scanner.Tokenizer;
import java.util.Scanner;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class InputStatement extends Statement{
    
    // =========================================================================
    // Instance Variables
    // @param String sentence will be printed as notice to the user
    // @param Identifier id is the variable that will take the value entered
    // =========================================================================
    
    private String sentence;
    private Identifier id;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // takes a String and an Identifier
    // =========================================================================
    
    public InputStatement(String text, Identifier ide){
        super(Tokenizer.RSVP_INPU_N);
        this.sentence = text;
        this.id = ide;
    }
    
    // =========================================================================
    // String getSentence
    // return the sentence of the input statement
    // =========================================================================
    
    public String getSentence(){
        return this.sentence;
    }
    
    // =========================================================================
    // Identifier getIdentifier
    // return the identifier of the statement
    // =========================================================================
    
    public Identifier getIdentifier(){
        return this.id;
    }
    
    // =========================================================================
    // void execute
    // OVERRIDING FUNCTION
    // Display the sentence and ask the user for input
    // =========================================================================
    
    @Override
    public void execute(){
        System.out.println("\n");
        System.out.print(this.getSentence());
        Scanner scn = new Scanner(System.in);
        this.id.setValue(scn.nextInt());
        System.out.println("\n");
    }
    
}
