// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 7 - 3rd Deliverable
// File:      StatementList.java
// =============================================================================
// Description:
// This file is the implementation of the Executer
// =============================================================================

package statements;

import java.util.LinkedList;

/**
 *
 * @author Karl Kevin Tiba Fossoh
 */
class StatementList {
    
    // =========================================================================
    // Instance variable
    // Defines a list of statements
    // =========================================================================
    
    private LinkedList<Statement> statements = new LinkedList<Statement>();
    
    // =========================================================================
    // _CONSTRUCTOR_
    // Does not takes any value as the list starts empty
    // =========================================================================
    
    public StatementList(){}
    
    // =========================================================================
    // void addStatement
    // Enables to add a Statement object to the list
    // =========================================================================
    
    public void addStatement(Statement stm){
        this.statements.add(stm);
    }
    
    // =========================================================================
    // void removeStatement
    // Enables to remove a statement from the statement list
    // =========================================================================
    
    public void removeStatement(Statement stm){
        this.statements.remove(stm);
    }
    
    // =========================================================================
    // LinkedList<Statement> getStatements
    // returns the list of statements in the statement list
    // =========================================================================
    
    public LinkedList<Statement> getStatements(){
        return this.statements;
    }
    
}
