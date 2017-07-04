// =============================================================================
// Author(s): Karl Kevin Tiba Fossoh
// Course:    CS 4308 (94)
// Instr:     Dr. Garrido
// Project:   Module 3 - 1st Deliverable
// File:      Token.java
// =============================================================================
// Description:
// This file is the implementation of the scanner class.
// =============================================================================

package scanner;

/**
 *
 * @author Karl Kevin TIBA FOSSOH #000801608
 */
public class Token {
    
    public final int token;
    public final String sequence;
    public int col_num;
    public int row_num;
    
    // =========================================================================
    // __constructor__ Token(int token, String sequance, int col, int row)
    // initialize a token
    // int token: defines the code assigned by the tokeninfo
    // String sequence: defines the literal sequence of the token
    // int row: gives information about the line of the token
    // int col: gives information about the column of the token
    // =========================================================================
    
    public Token(int token, String sequence, int col, int row) {
        super();
        this.token = token;
        this.sequence = sequence;
        this.col_num = col;
        this.row_num = row;
    }
    
    
}
