/*
 * @(#)ContentHandlerFactory.java	1.5 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 该接口为内容处理程序定义了一个工厂。此接口的实现应将 MIME 类型映射到 ContentHandler 的实例。
 * <p>
 * URLStreamHandler类使用此接口为 MIME 类型创建 ContentHandler。
 *
 * @author  James Gosling
 * @version 1.5, 12/12/01
 * @see     ContentHandler
 * @see     URLStreamHandler
 * @since   JDK1.0
 */
public interface ContentHandlerFactory {
    /**
     * Creates a new <code>ContentHandler</code> to read an object from 
     * a <code>URLStreamHandler</code>. 
     *
     * @param   mimetype   the MIME type for which a content handler is desired.

     * @return  a new <code>ContentHandler</code> to read an object from a
     *          <code>URLStreamHandler</code>.
     * @see     ContentHandler
     * @see     URLStreamHandler
     * @since   JDK1.0
     */
    ContentHandler createContentHandler(String mimetype);
}
