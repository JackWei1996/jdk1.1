/*
 * @(#)BindException.java	1.9 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 表示尝试将套接字绑定到本地地址和端口时发生错误的信号。通常，端口正在使用中，或者无法分配请求的本地地址。
 *
 * @since   JDK1.1
 */

public class BindException extends SocketException {

    /**
     * 使用指定的详细消息构造一个新的 BindException，以说明发生绑定错误的原因。详细消息是一个字符串，它给出了这个错误的具体描述。
     * @param msg the detail message
     * @since   JDK1.1
     */
    public BindException(String msg) {
	super(msg);
    }

    /**
     * Construct a new BindException with no detailed message.
     * @since JDK1.1
     */
    public BindException() {}
}
