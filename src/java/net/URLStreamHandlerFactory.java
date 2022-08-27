/*
 * @(#)URLStreamHandlerFactory.java	1.10 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 此接口为 URL 流协议处理程序定义了一个工厂。
 *
 * URL 类使用它来为特定协议创建 URLStreamHandler。
 *
 * @author  Arthur van Hoff
 * @version 1.10, 12/12/01
 * @see     URL
 * @see     URLStreamHandler
 * @since   JDK1.0
 */
public interface URLStreamHandlerFactory {
    /**
     * Creates a new <code>URLStreamHandler</code> instance with the specified
     * protocol.
     *
     * @param   protocol   the protocol ("<code>ftp</code>",
     *                     "<code>http</code>", "<code>nntp</code>", etc.).
     * @return  a <code>URLStreamHandler</code> for the specific protocol.
     * @see     java.io.URLStreamHandler
     * @since   JDK1.0
     */
    URLStreamHandler createURLStreamHandler(String protocol);
}
