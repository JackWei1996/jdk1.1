/*
 * @(#)SocketImpl.java	1.25 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileDescriptor;

/**
 * 抽象类 SocketImpl 是所有实际实现套接字的类的公共超类。它用于创建客户端和服务器套接字。
 * <p>
 * “普通”套接字完全按照描述实现这些方法，而不试图通过防火墙或代理。
 *
 * @author  unascribed
 * @version 1.25, 12/12/01
 * @since   JDK1.0
 */
public abstract class SocketImpl implements SocketOptions {
    /**
     * The file descriptor object for this socket. 
     *
     * @since   JDK1.0
     */
    protected FileDescriptor fd;
    
    /**
     * The IP address of the remote end of this socket. 
     *
     * @since   JDK1.0
     */
    protected InetAddress address;
   
    /**
     * The port number on the remote host to which this socket is connected. 
     *
     * @since   JDK1.0
     */
    protected int port;

    /**
     * The local port number to which this socket is connected. 
     *
     * @since   JDK1.0
     */
    protected int localport;   

    /**
     * Creates either a stream or a datagram socket. 
     *
     * @param      stream   if <code>true</code>, create a stream socket;
     *                      otherwise, create a datagram socket.
     * @exception  IOException  if an I/O error occurs while creating the
     *               socket.
     * @since      JDK1.0
     */
    protected abstract void create(boolean stream) throws IOException;

    /**
     * Connects this socket to the specified port on the named host. 
     *
     * @param      host   the name of the remote host.
     * @param      port   the port number.
     * @exception  IOException  if an I/O error occurs when connecting to the
     *               remote host.
     * @since      JDK1.0
     */
    protected abstract void connect(String host, int port) throws IOException;

    /**
     * Connects this socket to the specified port number on the specified host.
     *
     * @param      address   the IP address of the remote host.
     * @param      port      the port number.
     * @exception  IOException  if an I/O error occurs when attempting a
     *               connection.
     * @since      JDK1.0
     */
    protected abstract void connect(InetAddress address, int port) throws IOException;

    /**
     * Binds this socket to the specified port number on the specified host. 
     *
     * @param      host   the IP address of the remote host.
     * @param      port   the port number.
     * @exception  IOException  if an I/O error occurs when binding this socket.
     * @since      JDK1.0
     */
    protected abstract void bind(InetAddress host, int port) throws IOException;

    /**
     * 将传入连接指示（连接请求）的最大队列长度设置为 <code>count<code> 参数。如果队列满时有连接指示到达，则拒绝连接。
     *
     * @param      backlog   the maximum length of the queue.
     * @exception  IOException  if an I/O error occurs when creating the queue.
     * @since      JDK1.0
     */
    protected abstract void listen(int backlog) throws IOException;

    /**
     * Accepts a connection. 
     *
     * @param      s   the accepted connection.
     * @exception  IOException  if an I/O error occurs when accepting the
     *               connection.
     * @since   JDK1.0
     */
    protected abstract void accept(SocketImpl s) throws IOException;

    /**
     * Returns an input stream for this socket.
     *
     * @return     a stream for reading from this socket.
     * @exception  IOException  if an I/O error occurs when creating the
     *               input stream.
     * @since      JDK1.0
    */
    protected abstract InputStream getInputStream() throws IOException;

    /**
     * Returns an output stream for this socket.
     *
     * @return     an output stream for writing to this socket.
     * @exception  IOException  if an I/O error occurs when creating the
     *               output stream.
     * @since      JDK1.0
     */
    protected abstract OutputStream getOutputStream() throws IOException;

    /**
     * 返回可以不阻塞地从此套接字读取的字节数。
     *
     * @return     the number of bytes that can be read from this socket
     *             without blocking.
     * @exception  IOException  if an I/O error occurs when determining the
     *               number of bytes available.
     * @since      JDK1.0
     */
    protected abstract int available() throws IOException;

    /**
     * Closes this socket. 
     *
     * @exception  IOException  if an I/O error occurs when closing this socket.
     * @since      JDK1.0
     */
    protected abstract void close() throws IOException;

    /**
     * Returns the value of this socket's <code>fd</code> field.
     *
     * @return  the value of this socket's <code>fd</code> field.
     * @see     SocketImpl#fd
     * @since   JDK1.0
     */
    protected FileDescriptor getFileDescriptor() {
	return fd;
    }

    /**
     * Returns the value of this socket's <code>address</code> field.
     *
     * @return  the value of this socket's <code>address</code> field.
     * @see     SocketImpl#address
     * @since   JDK1.0
     */
    protected InetAddress getInetAddress() {
	return address;
    }

    /**
     * Returns the value of this socket's <code>port</code> field.
     *
     * @return  the value of this socket's <code>port</code> field.
     * @see     SocketImpl#port
     * @since   JDK1.0
     */
    protected int getPort() {
	return port;
    }

    /**
     * Returns the value of this socket's <code>localport</code> field.
     *
     * @return  the value of this socket's <code>localport</code> field.
     * @see     SocketImpl#localport
     * @since   JDK1.0
     */
    protected int getLocalPort() {
	return localport;
    }
    
    /**
     * Returns the address and port of this socket as a <code>String</code>.
     *
     * @return  a string representation of this socket.
     * @since   JDK1.0
     */
    public String toString() {
	return "Socket[addr=" + getInetAddress() +
	    ",port=" + getPort() + ",localport=" + getLocalPort()  + "]";
    }

    void reset() throws IOException {
	address = null;
	port = 0;
	localport = 0;
	close();
    }
}
