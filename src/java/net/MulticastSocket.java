/*
 * @(#)MulticastSocket.java	1.20 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * 多播数据报套接字类对于发送和接收 IP 多播数据包很有用。
 * MulticastSocket 是一个 (UDP) DatagramSocket，具有加入 Internet 上其他多播主机“组”的附加功能。
 * <P>
 * 多播组由 D 类 IP 地址指定，范围在 224.0.0.1 到 239.255.255.255（含）和标准 UDP 端口号。
 * 可以通过首先使用所需端口创建 MulticastSocket，然后调用 joinGroup(InetAddress groupAddr) 方法来加入多播组：
 * <PRE>
 * // join a Multicast group and send the group salutations
 * ...
 * byte[] msg = {'H', 'e', 'l', 'l', 'o'};
 * InetAddress group = InetAddress.getByName("228.5.6.7");
 * MulticastSocket s = new MulticastSocket(6789);
 * s.joinGroup(group);
 * DatagramPacket hi = new DatagramPacket(msg, msg.length,
 *                             group, 6789);
 * s.send(hi);
 * // get their responses! 
 * byte[] buf = new byte[1000];
 * DatagramPacket recv = new DatagramPacket(buf, buf.length);
 * s.receive(recv);
 * ...
 * // OK, I'm done talking - leave the group...
 * s.leaveGroup(group);
 * </PRE>
 * 
 * 当一个人向多播组发送消息时，所有订阅该主机和端口的接收者都会收到该消息（在数据包的生存时间范围内，见下文）。
 * 套接字不必是多播组的成员即可向其发送消息。
 * <P>
 * 当套接字订阅多播组端口时，它会接收其他主机发送到组端口的数据报，组和端口的所有其他成员也是如此。
 * 套接字通过 leaveGroup(InetAddress addr) 方法放弃组中的成员资格。
 * 多个MulticastSocket 的可以同时订阅一个组播组和端口，它们都将接收组数据报。
 * <P>
 * Currently applets are not allowed ot use multicast sockets.
 *
 * @author Pavani Diwanji
 * @since  JDK1.1
 */
public
class MulticastSocket extends DatagramSocket {
    /**
     * Create a multicast socket.
     * @since   JDK1.1
     */
    public MulticastSocket() throws IOException {
	super();
    }

    /**
     * Create a multicast socket and bind it to a specific port.
     * @param local port to use
     * @since   JDK1.1
     */
    public MulticastSocket(int port) throws IOException {
	super(port);
    }

    /* 做创建香草多播套接字的工作。重要的是，此方法的签名不会更改，
    即使它是包私有的，因为它覆盖了 DatagramSocket 中的方法，该方法不得设置 SO_REUSEADDR。
     */
    void create(int port, InetAddress ignore) throws SocketException {
	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    security.checkListen(port);
	}
	try {
	    this.impl = (DatagramSocketImpl) implClass.newInstance();
	} catch (Exception e) {
	    throw new SocketException("can't instantiate DatagramSocketImpl" + e.toString());
	}
	impl.create();
	impl.setOption(SocketOptions.SO_REUSEADDR, new Integer(-1));
	impl.bind(port, InetAddress.anyLocalAddress);
    }
    
    /**
     * 设置在此套接字上发出的多播数据包的默认生存时间。
	 * TTL 设置发送到 MulticastGroup 的 DatagramPackets的 IP 生存时间，它指定数据包在到期之前将在网络上转发多少“跳”。
     * <P>
     * The ttl is an <b>unsigned</b> 8-bit quantity, and so <B>must</B> be
     * in the range <code> 0 < ttl <= 0xFF </code>.
     * @param ttl the time-to-live
     * @since   JDK1.1
     */
    public void setTTL(byte ttl) throws IOException {
	impl.setTTL(ttl);
    }

    /**
     * Get the default time-to-live for multicast packets sent out
     * on the socket.
     * @since   JDK1.1
     */
    public byte getTTL() throws IOException {
	return impl.getTTL();
    }

    /**
     * Joins a multicast group.
     * @param mcastaddr is the multicast address to join 
     * @exception IOException is raised if there is an error joining
     * or when address is not a multicast address.
     * @since   JDK1.1
     */
    public void joinGroup(InetAddress mcastaddr) throws IOException {

	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    security.checkMulticast(mcastaddr);
	}
	impl.join(mcastaddr);
    }

    /**
     * Leave a multicast group.
     * @param mcastaddr is the multicast address to leave
     * @exception IOException is raised if there is an error leaving
     * or when address is not a multicast address.
     * @since   JDK1.1
     */
    public void leaveGroup(InetAddress mcastaddr) throws IOException {

	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    security.checkMulticast(mcastaddr);
	}
	impl.leave(mcastaddr);
    }

    /**
     * 将此套接字上的多播数据包的传出网络接口设置为系统默认值以外的接口。对于多宿主主机很有用。
     * @since   JDK1.1
     */
    public void setInterface(InetAddress inf) throws SocketException {
	impl.setOption(SocketOptions.IP_MULTICAST_IF, inf);
    }
    
    /**
     * Retrieve the address of the network interface used for
     * multicast packets.
     * @since   JDK1.1
     */
    public InetAddress getInterface() throws SocketException {
	return (InetAddress) impl.getOption(SocketOptions.IP_MULTICAST_IF);
    }
    
    /**
     * 将数据报包发送到目的地，其 TTL（生存时间）不是套接字的默认值。这种方法只需要在需要特定 TTL 的情况下使用；
	 * 否则最好在套接字上设置一次 TTL，并为所有数据包使用该默认 TTL。此方法不会  而不是  更改套接字的默认 TTL。
     * @param p	是要发送的数据包。数据包应包含目标多播 IP 地址和要发送的数据。无需成为组的成员即可将数据包发送到目标多播地址。
     * @param ttl optional time to live for multicast packet.
     * default ttl is 1.
     * @exception IOException is raised if an error occurs i.e
     * error while setting ttl.
     * @see DatagramSocket#send
     * @see DatagramSocket#receive
     * @since   JDK1.1
     */
    public synchronized void send(DatagramPacket p, byte ttl)
	 throws IOException {

        // 安全管理器确保多播地址是允许的，并且使用的 ttl 小于允许的 maxttl。
	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    if (p.getAddress().isMulticastAddress()) {
		security.checkMulticast(p.getAddress(), ttl);
	    } else {
		security.checkConnect(p.getAddress().getHostAddress(), p.getPort());
	    }
	}

	byte dttl = getTTL();
	
	if (ttl != dttl) {
	// set the ttl
	    impl.setTTL(ttl);
	}
	// call the datagram method to send
	impl.send(p);
	// set it back to default
	if (ttl != dttl) {
	    impl.setTTL(dttl);
	} 
    }  
}
