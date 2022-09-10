/*
 * @(#)DatagramPacket.java	1.14 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * This class represents a datagram packet. 
 * <p>
 * 数据报包用于实现无连接的包传递服务。每条消息仅根据该数据包中包含的信息从一台机器路由到另一台机器。
 * 从一台机器发送到另一台机器的多个数据包可能会以不同的方式路由，并且可能以任何顺序到达。
 *
 * @author  Pavani Diwanji
 * @version 1.14, 12/12/01
 * @since   JDK1.0
 */
public final 
class DatagramPacket {
    /*
     * 此类的字段是包私有的，因为 DatagramSocketImpl 类需要访问它们。
     */
    byte[] buf;
    int length;
    InetAddress address;
    int port;

    /**
     * Constructs a <code>DatagramPacket</code> for receiving packets of 
     * length <code>ilength</code>. 
     * <p>
     * The <code>length</code> argument must be less than or equal to 
     * <code>ibuf.length</code>. 
     *
     * @param   ibuf      buffer for holding the incoming datagram.
     * @param   ilength   the number of bytes to read.
     * @since   JDK1.0
     */
    public DatagramPacket(byte ibuf[], int ilength) {
	if (ilength > ibuf.length) {
	    throw new IllegalArgumentException("illegal length");
	}
	buf = ibuf;
	length = ilength;
	address = null;
	port = -1;
    }
    
    /**
     * 构造一个数据报包，用于将长度为 ilength 的数据包发送到指定主机上的指定端口号。
     * length参数必须小于或等于 ibuf.length。
     *
     * @param   ibuf      the packet data.
     * @param   ilength   the packet length.
     * @param   iaddr     the destination address.
     * @param   iport     the destination port number.
     * @see     InetAddress
     * @since   JDK1.0
     */
    public DatagramPacket(byte ibuf[], int ilength,
			  InetAddress iaddr, int iport) {
	if (ilength > ibuf.length) {
	    throw new IllegalArgumentException("illegal length");
	}
	if (iport < 0 || iport > 0xFFFF) {
	    throw new IllegalArgumentException("Port out of range:"+ iport);
	}
	buf = ibuf;
	length = ilength;
	address = iaddr;
	port = iport;
    }
    
    /**
     * 返回此数据报被发送到或接收到数据报的机器的 IP 地址。
     *
     * @return  发送该数据报的机器的 IP 地址或接收该数据报的机器的 IP 地址。
     * @see     InetAddress
     * @since   JDK1.0
     */
    public synchronized InetAddress getAddress() {
	return address;
    }
    
    /**
     * 返回此数据报被发送到或从其接收到的远程主机上的端口号。
     *
     * @return  发送该数据报或接收该数据报的远程主机上的端口号。
     * @since   JDK1.0
     */
    public synchronized int getPort() {
	return port;
    }
    
    /**
     * Returns the data received or the data to be sent.
     *
     * @return  the data received or the data to be sent.
     * @since   JDK1.0
     */
    public synchronized byte[] getData() {
	return buf;
    }
    
    /**
     * 返回要发送的数据的长度或接收的数据的长度。
     *
     * @return  the length of the data to be sent or the length of the
     *          data received.
     * @since   JDK1.0
     */
    public synchronized int getLength() {
	return length;
    }

    /**
     * @since   JDK1.1
     */
    public synchronized void setAddress(InetAddress iaddr) {
	address = iaddr;
    }

    /**
     * @since   JDK1.1
     */
    public synchronized void setPort(int iport) {
	if (iport < 0 || iport > 0xFFFF) {
	    throw new IllegalArgumentException("Port out of range:"+ iport);
	}
	port = iport;
    }

    /**
     * @since   JDK1.1
     */
    public synchronized void setData(byte[] ibuf) {
	buf = ibuf;
    }

    /**
     * @since   JDK1.1
     */
    public synchronized void setLength(int ilength) {
	if (ilength > buf.length) {
	    throw new IllegalArgumentException("illegal length");
	}
	length = ilength;
    }
}
