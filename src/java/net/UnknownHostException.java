/*
 * @(#)UnknownHostException.java	1.9 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;

/**
 * 抛出表示无法确定主机的 IP 地址。
 *
 * @author  Jonathan Payne 
 * @version 1.9, 12/12/01
 * @since   JDK1.0
 */
public 
class UnknownHostException extends IOException {
    /**
     * Constructs a new <code>UnknownHostException</code> with the 
     * specified detail message. 
     *
     * @param   host   the detail message.
     * @since   JDK1.0
     */
    public UnknownHostException(String host) {
	super(host);
    }

    /**
     * Constructs a new <code>UnknownHostException</code> with no detail 
     * message. 
     *
     * @since   JDK1.0
     */
    public UnknownHostException() {
    }
}
