/*
 * @(#)ProtocolException.java	1.10 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;

/**
 * 抛出表示底层协议存在错误，例如 TCP 错误。
 *
 * @author  Chris Warth
 * @version 1.10, 12/12/01
 * @since   JDK1.0
 */
public 
class ProtocolException extends IOException { 
    /**
     * Constructs a new <code>ProtocolException</code> with the 
     * specified detail message. 
     *
     * @param   host   the detail message.
     * @since   JDK1.0
     */
    public ProtocolException(String host) {
	super(host);
    }
    
    /**
     * Constructs a new <code>ProtocolException</code> with no detail message.
     *
     * @since   JDK1.0
     */
    public ProtocolException() {
    }
}
