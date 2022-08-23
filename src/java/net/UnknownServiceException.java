/*
 * @(#)UnknownServiceException.java	1.8 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;

/**
 * 抛出表示发生了未知的服务异常。 URL 连接返回的 MIME 类型没有意义，或者应用程序正在尝试写入只读 URL 连接。
 *
 * @author  unascribed
 * @version 1.8, 12/12/01
 * @since   JDK1.0
 */
public class UnknownServiceException extends IOException {
    /**
     * Constructs a new <code>UnknownServiceException</code> with no 
     * detail message. 
     *
     * @since   JDK1.0
     */
    public UnknownServiceException() {
    }

    /**
     * Constructs a new <code>UnknownServiceException</code> with the 
     * specified detail message. 
     *
     * @param   msg   the detail message.
     * @since   JDK1.0
     */
    public UnknownServiceException(String msg) {
	super(msg);
    }
}
