// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      ArgumentsList.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements.funct;

import java.util.LinkedList;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
public class ArgumentsList {
    
    // =========================================================================
    // Instance variables
    // @param LinkedList<Argument> arg_list
    // defines the list of arguments related to a statement
    // =========================================================================
    
    LinkedList<Argument> arg_list = new LinkedList<Argument>();
    
    // =========================================================================
    // _CONSTRUCTOR_
    // Empty constructor
    // defines an empty list
    // =========================================================================
    
    public ArgumentsList(){}
    
    // =========================================================================
    // void addArgument
    // @param Argument ar
    // add a new argument to the list of arguments
    // =========================================================================
    
    public void addArgument(Argument ar){
        this.arg_list.add(ar);
    }
    
    // =========================================================================
    // LinkedList<Argument> getargumentList
    // return the list of arguments
    // =========================================================================
    
    public LinkedList<Argument> getArgumentList(){
        return this.arg_list;
    }
    
    // =========================================================================
    // String getText
    // return the set of args in a text-like manner
    // =========================================================================
    
    public String getText(){
        String txt = "";
        for(Argument ar: this.arg_list){
            txt += ar.getValueArg()+" ";
        }
        return txt;
    }
    
    // =========================================================================
    // void removeArgument
    // @param Argument ar
    // Removes the following argument from the argument list
    // =========================================================================
    
    public void removeArgument(Argument ar){
        this.arg_list.remove(ar);
    }
    
}
