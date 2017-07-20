// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      Argument.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements.funct;

import parser.Identifier;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class Argument {
    
    // =========================================================================
    // Instance variable
    // @param String argument_value
    // Used as reference to the display of elements
    // =========================================================================
    
    private String argument_value;
    
    // =========================================================================
    // _CONSTRUCTOR_
    // takes an Identifier
    // =========================================================================
    
    public Argument(Identifier id){
        this.argument_value = Integer.toString(id.getValue());
    }
    
    // =========================================================================
    // _CONSTRUCTOR_
    // takes an int
    // =========================================================================
    
    public Argument(int value){
        this.argument_value = Integer.toString(value);
    }
    
    // =========================================================================
    // _CONSTRUCTOR_
    // takes a String
    // =========================================================================
    
    public Argument(String content){
        this.argument_value = content;
    }
    
    // =========================================================================
    // static void appendArg
    // @param Argument arg is the argument to which we attach element
    // @param String content is the element added
    // append a value to the original Argument arg
    // =========================================================================
    
    public static void appendArg(Argument arg, String content){
        arg.setValueArg(arg.getValueArg()+" "+content);
    }
    
    // =========================================================================
    // String getValueArg
    // Returns the value of the argument
    // =========================================================================
    
    public String getValueArg(){
        return this.argument_value;
    }
    
    // =========================================================================
    // void setValueArg
    // takes a String value
    // enables to modify or assign a value to an argument
    // =========================================================================
    
    public void setValueArg(String value){
        this.argument_value = value;
    }
    
}
