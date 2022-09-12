/*
 * @(#)PlainDatagramSocketImpl.java	1.12 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * 具体的数据报和多播套接字实现基类。注意：这不是一个公共类，因此小程序不能直接调用实现，
 * 因此不能绕过 DatagramSocket 和 MulticastSocket 类中的安全检查。
 *
 * @author Pavani Diwanji
 */

class PlainDatagramSocketImpl extends DatagramSocketImpl
{

    /* timeout value for receive() */
    private int timeout = 0;

    /**
     * Load net library into runtime.
     */
    static {
	System.loadLibrary("net");
    }

    /**
     * Creates a datagram socket
     */
    protected synchronized void create() throws SocketException {
	fd = new FileDescriptor();
	datagramSocketCreate();
    }

    /**
     * Binds a datagram socket to a local port.
     */
    protected synchronized native void bind(int lport, InetAddress laddr) throws SocketException;

    /**
     * 发送一个数据报包。数据包包含数据和要将数据包发送到的目标地址。
     * @param packet to be sent.
     */
    protected native void send(DatagramPacket p) throws IOException;

    /**
     * Peek at the packet to see who it is from.
     * @param return the address which the packet came from.
     */
    protected synchronized native int peek(InetAddress i) throws IOException;

    /**
     * Receive the datagram packet.
     * @param Packet Received.
     */
    protected synchronized native void receive(DatagramPacket p) throws IOException;

    /**
     * Set the TTL (time-to-live) option.
     * @param TTL to be set.
     */
    protected native void setTTL(byte ttl) throws IOException;

    /**
     * Get the TTL (time-to-live) option.
     */
    protected native byte getTTL() throws IOException;

    /**
     * Join the multicast group.
     * @param multicast address to join.
     */
    protected native void join(InetAddress inetaddr) throws IOException;

    /**
     * Leave the multicast group.
     * @param multicast address to leave.
     */
    protected native void leave(InetAddress inetaddr) throws IOException;

    /**
     * Close the socket.
     */
    protected void close() {
	if (fd != null) {
	    datagramSocketClose();
	    fd = null;
	}
    }

    protected synchronized void finalize() {
	close();
    }

    /**
     * 设置一个值 - 因为我们在这里只支持（设置）二进制选项，所以 o 必须是一个布尔值
     */

     public void setOption(int optID, Object o) throws SocketException {
	 switch (optID) {
	    /* 检查类型安全 b4 是否原生。这些永远不会失败，因为只有 java.Socket 可以访问 PlainSocketImpl.setOption()。
	     */
	 case SO_TIMEOUT:
	     if (o == null || !(o instanceof Integer)) {
		 throw new SocketException("bad argument for SO_TIMEOUT");
	     }
	     int tmp = ((Integer) o).intValue();
	     if (tmp < 0) 
		 throw new IllegalArgumentException("timeout < 0");
	     timeout = tmp;
	     return;
	 case SO_BINDADDR:
	     throw new SocketException("Cannot re-bind Socket");
	 case SO_REUSEADDR:
	     if (o == null || !(o instanceof Integer)) {
		 throw new SocketException("bad argument for SO_REUSEADDR");
	     } 
	     break;
	 case IP_MULTICAST_IF: 
	     if (o == null || !(o instanceof InetAddress))
		 throw new SocketException("bad argument for IP_MULTICAST_IF");
	     break;
	 default:
	     throw new SocketException("invalid option: " + optID);
	 }
	 socketSetOption(optID, o);
     }

    /*
     * get option's state - set or not
     */

    public Object getOption(int optID) throws SocketException {
	if (optID == SO_TIMEOUT) {
	    return new Integer(timeout);
	}
	int ret = socketGetOption(optID);

	if (optID == SO_BINDADDR || optID == IP_MULTICAST_IF) {
	    InetAddress in = new InetAddress();
	    in.address = ret;
	    return in;
	} else {
	    return null;
	}
    }
	
    private native void datagramSocketCreate() throws SocketException;
    private native void datagramSocketClose();
    private native void socketSetOption(int opt, Object val) throws SocketException;
    private native int socketGetOption(int opt) throws SocketException;
    
}

