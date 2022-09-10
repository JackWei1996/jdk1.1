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
     * Constructs a datagram packet for sending packets of length 
     * <code>ilength</code> to the specified port number on the specified 
     * host. The <code>length</code> argument must be less than or equal 
     * to <code>ibuf.length</code>. 
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
     * Returns the IP address of the machine to which this datagram is being
     * sent or from which the datagram was received.
     *
     * @return  the IP address of the machine to which this datagram is being
     *          sent or from which the datagram was received.
     * @see     InetAddress
     * @since   JDK1.0
     */
    public synchronized InetAddress getAddress() {
	return address;
    }
    
    /**
     * Returns the port number on the remote host to which this datagram is
     * being sent or from which the datagram was received.
     *
     * @return  the port number on the remote host to which this datagram is
     *          being sent or from which the datagram was received.
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
     * Returns the length of the data to be sent or the length of the
     * data received.
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
