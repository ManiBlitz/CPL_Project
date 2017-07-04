// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 3 - 1st Deliverable
// File:      ParserException.java
// =============================================================================
// Description:
// This file is the implementation of the ParserException class.
// =============================================================================

package exceptionsPack;

/**
 *
 * @author Karl Kevin TIBA FOSSOH #000801608
 */


// =============================================================================
// Exception class designed for the scanner
// =============================================================================

public class ParserException extends RuntimeException {
    public ParserException(String msg) {
        super(msg);
    }
}
