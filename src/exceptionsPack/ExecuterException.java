/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptionsPack;

/**
 *
 * @author ADMIN
 */
public class ExecuterException extends RuntimeException {

    /**
     * Creates a new instance of <code>ExecuterException</code> without detail
     * message.
     */
    public ExecuterException() {
    }

    /**
     * Constructs an instance of <code>ExecuterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExecuterException(String msg) {
        super(msg);
    }
}
