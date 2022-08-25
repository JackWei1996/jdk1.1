/*
 * @(#)NoRouteToHostException.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 表示尝试将套接字连接到远程地址和端口时发生错误的信号。通常，由于中间防火墙或中间路由器已关闭，无法访问远程主机。
 *
 * @since   JDK1.1
 */
public class NoRouteToHostException extends SocketException {
    /**
     * 使用指定的详细消息构造一个新的 NoRouteToHostException，以说明无法访问远程主机的原因。
     * 详细消息是一个字符串，它给出了这个错误的具体描述。
     * @param msg the detail message
     * @since   JDK1.1
     */
    public NoRouteToHostException(String msg) {
	super(msg);
    }

    /**
     * Construct a new NoRouteToHostException with no detailed message.
     * @since   JDK1.1
     */
    public NoRouteToHostException() {}
}
