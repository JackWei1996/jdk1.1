/*
 * @(#)DatagramSocket.java	1.27 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * This class represents a socket for sending and receiving datagram packets.
 * <p>
 * 数据报套接字是无连接数据包传送服务。发送的每个数据包或在数据报套接字上接收的是单独寻址和路由。
 * 从一台机器发送到另一台机器的多个数据包可能是路线不同，可能以任何顺序到达。
 *
 * @author  Pavani Diwanji
 * @version 1.27, 12/12/01
 * @see     DatagramPacket
 * @since   JDK1.0
*/
public
class DatagramSocket {
    /*
     * The implementation of this DatagramSocket.
     */
    DatagramSocketImpl impl;

    /*
     * The Class of DatagramSocketImpl we use for this runtime.  
     */

    static Class implClass;

    static {
	String prefix = "";
	try {

	    prefix = System.getProperty("impl.prefix", "Plain");

	    implClass = Class.forName("java.net."+prefix+"DatagramSocketImpl");
	} catch (Exception e) {
	    System.err.println("Can't find class: java.net." + prefix +
			       "DatagramSocketImpl: check impl.prefix property");
	}

	if (implClass == null) {
	    try {
		implClass = Class.forName("java.net.PlainDatagramSocketImpl");
	    } catch (Exception e) {
		throw new Error("System property impl.prefix incorrect");
	    }
	}
    }

    /**
     * 构造数据报套接字并将其绑定到本地主机上的任何可用端口。
     *
     * @exception  java.net.SocketException  如果无法打开套接字，或者套接字无法绑定到指定的本地端口。
     * @since      JDK1.0
     */
    public DatagramSocket() throws SocketException {
	// create a datagram socket.
	create(0, null);
    }

    /**
     * 构造数据报套接字并将其绑定到指定端口在本地主机上。
     *
     * @param      local   port to use.
     * @exception  java.net.SocketException  如果无法打开套接字，或者套接字无法绑定到指定的本地端口。
     * @since      JDK1.0
     */
    public DatagramSocket(int port) throws SocketException {
	this(port, null);
    }

    /**
     * 创建绑定到指定本地地址的数据报套接字。本地端口必须介于0到65535之间（包括0和65535）。
     * @param port local port to use
     * @param laddr local address to bind
     * @since   JDK1.1
     */
    public DatagramSocket(int port, InetAddress laddr) throws SocketException {
	if (port < 0 || port > 0xFFFF)
	    throw new IllegalArgumentException("Port out of range:"+port);

	create(port, laddr);
    }

    /* 完成创建vanilla数据报套接字的工作。
    *它是重要的是，此方法的签名不会更改，即使它是包私有的，因为它被MulticastSocket，必须设置SO_REUSEADDR。
     */
    void create(int port, InetAddress laddr) throws SocketException {
	SecurityManager sec = System.getSecurityManager();
	if (sec != null) {
	    sec.checkListen(port);
	}
	try {
	    impl = (DatagramSocketImpl) implClass.newInstance();
	} catch (Exception e) {
	    throw new SocketException("can't instantiate DatagramSocketImpl");
	}
	// creates a udp socket
	impl.create();
	// binds the udp socket to desired port + address
	if (laddr == null) {
	    laddr = InetAddress.anyLocalAddress;
	}
	impl.bind(port, laddr);
    }

    /**
     * 从此套接字发送数据报数据包。这个 DatagramPacket 包含指示要发送的数据、其长度、远程主机的IP地址，和远程主机上的端口号。
     *
     * @param      p   the <code>DatagramPacket</code> to be sent.
     * @exception  java.io.IOException  if an I/O error occurs.
     * @see        DatagramPacket
     * @since      JDK1.0
     */
    public void send(DatagramPacket p) throws IOException  {

	// check the address is ok wiht the security manager on every send.
	SecurityManager security = System.getSecurityManager();

	// 要同步数据报数据包的原因是因为您不希望小程序更改地址例如，当您尝试发送数据包时在安全检查之后但在发送之前。
	synchronized (p) {
	    if (security != null) {
		if (p.getAddress().isMulticastAddress()) {
		    security.checkMulticast(p.getAddress());
		} else {
		    security.checkConnect(p.getAddress().getHostAddress(), 
					  p.getPort());
		}
	    }
            // call the  method to send
            impl.send(p);
        }
    }

    /**
     * 从这个套接字接收一个数据报包。当这种方法返回时，DatagramPacket的缓冲区被填满接收到的数据。
	 * 数据报包还包含发送方的信息IP地址，以及发送方机器上的端口号。
     * <p>
     * 此方法阻塞，直到接收到数据报。的length字段包含的数据报对象接收消息的长度。
	 * 如果消息的长度大于缓冲区长度，消息被截断。
     *
     * @param      p   the <code>DatagramPacket</code> into which to place
     *                 the incoming data.
     * @exception  java.io.IOException  if an I/O error occurs.
     * @see        DatagramPacket
     * @see        DatagramSocket
     * @since      JDK1.0
     */
    public synchronized void receive(DatagramPacket p) throws IOException {
	// check the address is ok with the security manager before every recv.
	SecurityManager security = System.getSecurityManager();

      	synchronized (p) {
	    if (security != null) {

		while(true) {
		    // peek at the packet to see who it is from.
		    InetAddress peekAddress = new InetAddress();
		    int peekPort = impl.peek(peekAddress);

		    try {
			security.checkConnect(peekAddress.getHostAddress(), peekPort);
			// security check succeeded - so now break and recv the packet.
			break;
		    } catch (SecurityException se) {
			// Throw away the offending packet by consuming
			// it in a tmp buffer.
			DatagramPacket tmp = new DatagramPacket(new byte[1], 1);
			impl.receive(tmp);

			// 悄悄丢弃有问题的数据包并继续：网络上的未知/恶意实体不应运行时抛出安全异常并中断applet通过发送随机数据包。
			continue;
		    } 
		} // end of while
	    }
	    // If the security check succeeds, then receive the packet
 	    impl.receive(p);
	}	
    }

    /**
     * Gets the local address to which the socket is bound.
     *
     * @since   1.1
     */
    public InetAddress getLocalAddress() {
	InetAddress in = null;
	try {
	    in = (InetAddress) impl.getOption(SocketOptions.SO_BINDADDR);
	    SecurityManager s = System.getSecurityManager();
	    if (s != null) {
		s.checkConnect(in.getHostAddress(), -1);
	    }
	} catch (Exception e) {
	    in = InetAddress.anyLocalAddress; // "0.0.0.0"
	}
	return in;
    }    

    /**
     * Returns the port number on the local host to which this socket is bound.
     *
     * @return  the port number on the local host to which this socket is bound.
     * @since   JDK1.0
     */
    public int getLocalPort() {
	return impl.getLocalPort();
    }

    /**
	 * 使用指定的超时（毫秒）启用/禁用SO_TIMEOUT。当此选项设置为非零超时时，
	 * 对此DatagramSocket的receive（）调用将仅阻塞此时间段。
	 * 如果超时过期，将显示java.io。尽管ServerSocket仍然有效，但引发了InterruptedIOException。
	 * 必须在进入阻塞操作之前启用选项<B>才能生效。超时必须大于0。超时为零将被解释为无限超时。
     *
     * @since   JDK1.1
     */
    public synchronized void setSoTimeout(int timeout) throws SocketException {
	impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
    }

    /**
     * Retrive setting for SO_TIMEOUT.  0 returns implies that the
     * option is disabled (i.e., timeout of infinity).
     *
     * @since   JDK1.1
     */
    public synchronized int getSoTimeout() throws SocketException {
	Object o = impl.getOption(SocketOptions.SO_TIMEOUT);
	/* extra type safety */
	if (o instanceof Integer) {
	    return ((Integer) o).intValue();
	} else {
	    return 0;
	}
    }

    /**
     * Closes this datagram socket. 
     *
     * @since   JDK1.0
     */
    public void close() {
	impl.close();
    }
}
