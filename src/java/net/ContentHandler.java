/*
 * @(#)ContentHandler.java	1.9 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;

/**
 * 抽象类 ContentHandler 是从 URLConnection读取 Object 的所有类的超类。
 * <p>
 * 应用程序通常不会直接调用此类中的 getContent 方法。
 * 相反，应用程序调用类 URL 或 URLConnection 中的 <code>getContent<code> 方法。
 * 应用程序的内容处理程序工厂（实现通过调用 setContentHandler 设置的接口 ContentHandlerFactory 的类的实例）
 * 使用 String 调用，并给出 MIME在套接字上接收的对象的类型。
 * 工厂返回一个 ContentHandler 子类的实例，并调用它的 getContent 方法来创建对象。
 *
 * @author  James Gosling
 * @version 1.9, 12/12/01
 * @see     ContentHandler#getContent(URLConnection)
 * @see     ContentHandlerFactory
 * @see     URL#getContent()
 * @see     URLConnection
 * @see     URLConnection#getContent()
 * @see     URLConnection#setContentHandlerFactory(ContentHandlerFactory)
 * @since   JDK1.0
 */
abstract public class ContentHandler {
    /** 
     * 给定一个位于对象表示开头的 URL 连接流，此方法读取该流并从中创建一个对象。
     *
     * @param      urlc   a URL connection.
     * @return     the object read by the <code>ContentHandler</code>.
     * @exception  IOException  if an I/O error occurs while reading the object.
     * @since      JDK1.0
     */
    abstract public Object getContent(URLConnection urlc) throws IOException;
}
