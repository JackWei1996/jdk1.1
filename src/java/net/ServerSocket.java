/*
 * @(#)ServerSocket.java	1.30 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;
import java.io.FileDescriptor;

/**
 * 此类实现服务器套接字。服务器套接字等待通过网络传入的请求。
 * 它根据该请求执行一些操作，然后可能将结果返回给请求者。
 * <p>
 * 服务器套接字的实际工作由SocketImpl类的实例执行。
 * 应用程序可以更改创建套接字实现的套接字工厂，以配置自身以创建适合本地防火墙的套接符。
 *
 * @author  unascribed
 * @version 1.30, 12/12/01
 * @see     SocketImpl
 * @see     ServerSocket#setSocketFactory(SocketImplFactory)
 * @since   JDK1.0
 */
public 
class ServerSocket {
    /**
     * The implementation of this Socket.
     */
    private SocketImpl impl;

    /**
     * Creates an unconnected server socket. Note: this method
     * should not be public.
     * @exception IOException IO error when opening the socket.
     */
    private ServerSocket() throws IOException {
	impl = (factory != null) ? factory.createSocketImpl() : 
	    new PlainSocketImpl();
    }

    /**
     * 在指定端口上创建服务器套接字。端口0在任何可用端口上创建套接字。
     * <p>
     * 传入连接指示（连接请求）的最大队列长度设置为<code>50。如果在队列已满时到达连接指示，则会拒绝连接。
     * <p>
     * 如果应用程序指定了服务器套接字工厂，则调用该工厂的createSocketImpl方法来创建实际的套接字实现。否则将创建一个“普通”套接字。
     *
     * @param      port  the port number, or <code>0</code> to use any
     *                   free port.
     * @exception  IOException  if an I/O error occurs when opening the socket.
     * @see        SocketImpl
     * @see        SocketImplFactory#createSocketImpl()
     * @see        ServerSocket#setSocketFactory(SocketImplFactory)
     * @since      JDK1.0
     */
    public ServerSocket(int port) throws IOException {
	this(port, 50, null);
    }

    /**
     * 创建服务器套接字并将其绑定到指定的本地端口号。端口号为0会在任何可用端口上创建套接字。
     * <p>
     * 传入连接指示（连接请求）的最大队列长度设置为count参数。如果队列已满时出现连接指示，则拒绝连接。
     * <p>
     * 如果应用程序指定了服务器套接字工厂，则调用该工厂的createSocketImpl方法来创建实际的套接字实现。
	 * 否则将创建一个“普通”套接字。
     *
     * @param      port     the specified port, or <code>0</code> to use
     *                      any free port.
     * @param      backlog  the maximum length of the queue.
     * @exception  IOException  if an I/O error occurs when opening the socket.
     * @see        SocketImpl
     * @see        SocketImplFactory#createSocketImpl()
     * @see        ServerSocket#setSocketFactory(SocketImplFactory)
     * @since      JDK1.0
     */
    public ServerSocket(int port, int backlog) throws IOException {
	this(port, backlog, null);
    }

    /** 
     * 创建一个具有指定端口、侦听积压工作（backlog）和要绑定到的本地IP地址的服务器。
	 * bindAddr参数可用于ServerSocket的多宿主主机，该主机只接受对其一个地址的连接请求。
	 * 如果bindAddr为空，则默认接受任何/所有本地地址上的连接。
	 * 端口必须介于0到65535之间（包括0和65535）。
     * <P>
     * @param port the local TCP port
     * @param backlog the listen backlog
     * @param bindAddr the local InetAddress the server will bind to
     * @see SocketConstants
     * @see SocketOption
     * @see SocketImpl
     * @see   JDK1.1
     */
    public ServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
    	this();

	if (port < 0 || port > 0xFFFF)
	    throw new IllegalArgumentException(
		       "Port value out of range: " + port);
	try {
	    SecurityManager security = System.getSecurityManager();
	    if (security != null) {
		security.checkListen(port);
	    }

	    impl.create(true); // a stream socket
	    if (bindAddr == null)
		bindAddr = InetAddress.anyLocalAddress;	

	    impl.bind(bindAddr, port);
	    impl.listen(backlog);

	} catch(SecurityException e) {
	    impl.close();
	    throw e;
	} catch(IOException e) {
	    impl.close();
	    throw e;
	}
    }

    /**
     * Returns the local address of this server socket.
     *
     * @return  the address to which this socket is connected,
     *          or <code>null</code> if the socket is not yet connected.
     * @since   JDK1.0
     */
    public InetAddress getInetAddress() {
	return impl.getInetAddress();
    }

    /**
     * Returns the port on which this socket is listening.
     *
     * @return  the port number to which this socket is listening.
     * @since   JDK1.0
     */
    public int getLocalPort() {
	return impl.getLocalPort();
    }

    /**
     * 侦听要建立到此套接字的连接并接受它。该方法将阻塞，直到建立连接。
     *
     * @exception  IOException  if an I/O error occurs when waiting for a
     *               connection.
     * @since      JDK1.0
     */
    public Socket accept() throws IOException {
	Socket s = new Socket();
	implAccept(s);
	return s;
    }

    /**
     * ServerSocket的子类使用此方法重写accept（）以返回它们自己的套接字子类。
	 * 因此，FooServerSocket通常会将一个空FooSocket（）传递给这个方法。
	 * 从implAccept返回时，FooSocket将连接到客户端。
     *
     * @since   JDk1.1
     */
    protected final void implAccept(Socket s) throws IOException {
	SocketImpl si = s.impl;
	try {
	    s.impl = null;
	    si.address = new InetAddress();
	    si.fd = new FileDescriptor();
	    impl.accept(si);
	    
	    SecurityManager security = System.getSecurityManager();
	    if (security != null) {
		security.checkAccept(si.getInetAddress().getHostAddress(),
				     si.getPort());
	    }
	} catch (IOException e) {
	    si.reset();
	    s.impl = si;
	    throw e;
	} catch (SecurityException e) {
	    si.reset();
	    s.impl = si;
	    throw e;
	}
	s.impl = si;
    }

    /**
     * Closes this socket. 
     *
     * @exception  IOException  if an I/O error occurs when closing the socket.
     * @since      JDK1.0
     */
    public void close() throws IOException {
	impl.close();
    }

    /**
	 * 使用指定的超时（毫秒）启用/禁用SO_TIMEOUT。
	 * 如果将此选项设置为非零超时，则此ServerSocket的accept（）调用将仅阻塞此时间量。
	 * 如果超时过期，将显示java.io。尽管ServerSocket仍然有效，但引发了InterruptedIOException。
	 * 必须在进入阻止操作之前启用该选项才能生效。超时必须大于0。
     *  A timeout of zero is interpreted as an infinite timeout.  
     *
     * @since   JDK1.1
     */
    public synchronized void setSoTimeout(int timeout) throws SocketException {
	impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
    }

    /** Retrive setting for SO_TIMEOUT.  0 returns implies that the
     *  option is disabled (i.e., timeout of infinity).
     *
     * @since   JDK1.1
     */
    public synchronized int getSoTimeout() throws IOException {
	Object o = impl.getOption(SocketOptions.SO_TIMEOUT);
	/* extra type safety */
	if (o instanceof Integer) {
	    return ((Integer) o).intValue();
	} else {
	    return 0;
	}
    }

    /**
     * Returns the implementation address and implementation port of 
     * this socket as a <code>String</code>.
     *
     * @return  a string representation of this socket.
     * @since   JDK1.0
     */
    public String toString() {
	return "ServerSocket[addr=" + impl.getInetAddress() + 
		",port=" + impl.getPort() + 
		",localport=" + impl.getLocalPort()  + "]";
    }

    /**
     * The factory for all server sockets.
     */
    private static SocketImplFactory factory;

    /**
     * Sets the server socket implementation factory for the 
     * application. The factory can be specified only once. 
     * <p>
     * When an application creates a new server socket, the socket 
     * implementation factory's <code>createSocketImpl</code> method is 
     * called to create the actual socket implementation. 
     *
     * @param      fac   the desired factory.
     * @exception  IOException  if an I/O error occurs when setting the
     *               socket factory.
     * @exception  SocketException  if the factory has already been defined.
     * @see        SocketImplFactory#createSocketImpl()
     * @since      JDK1.0
     */
    public static synchronized void setSocketFactory(SocketImplFactory fac) throws IOException {
	if (factory != null) {
	    throw new SocketException("factory already defined");
	}
	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    security.checkSetFactory();
	}
	factory = fac;
    }
}
