/*
 * @(#)SocketOptions.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 获取套接字选项的方法接口。该接口由 SocketImpl 和 DatagramSocketImpl实现。
 * 这些子类应该覆盖这个接口的方法，以支持他们自己的选项。
 * <P>
 * 在此接口中指定选项的方法和常量仅用于实现。如果您没有继承 SocketImpl 或 DatagramSocketImpl，您将不会直接使用它们。
 * 在 Socket、ServerSocket、DatagramSocket 和 MulticastSocket 中有类型安全的方法来设置这些选项中的每一个。
 * <P>
 * JDK 基类 PlainSocketImpl 和 PlainDatagramSocketImpl 支持标准 BSD 样式套接字选项的子集。提供了每种方法及其用途的简要说明。
 * <P>
 * @version 1.7, 12/12/01
 * @author David Brown
 */


interface SocketOptions {

    /**
     * 启用禁用 optID 指定的选项。如果要启用该选项，并且它需要一个特定于选项的“值”，
     * 则在 value 中传递。值的实际类型是特定于选项的，传递不属于预期类型的内容是错误的：
     * <BR><PRE>
     * SocketImpl s;
     * ...
     * s.setOption(SO_LINGER, new Integer(10)); 
     *    // OK - set SO_LINGER w/ timeout of 10 sec.
     * s.setOption(SO_LINGER, new Double(10)); 
     *    // ERROR - expects java.lang.Integer
     *</PRE>
     * If the requested option is binary, it can be set using this method by
     * a java.lang.Boolean:
     * <BR><PRE>
     * s.setOption(TCP_NODELAY, new Boolean(true)); 
     *    // OK - enables TCP_NODELAY, a binary option
     * </PRE>
     * <BR>
     * Any option can be disabled using this method with a Boolean(false):
     * <BR><PRE>
     * s.setOption(TCP_NODELAY, new Boolean(false)); 
     *    // OK - disables TCP_NODELAY
     * s.setOption(SO_LINGER, new Boolean(false)); 
     *    // OK - disables SO_LINGER
     * </PRE>
     * <BR>
     * 对于需要特定参数的选项，将其值设置为 Boolean(false) 以外的任何值都会隐式启用它。
     * <BR>
     * 如果选项无法识别、套接字已关闭或发生一些低级错误，则抛出 SocketException
     * <BR>
     * @param optID identifies the option 
     * @param value the parameter of the socket option
     * @throws SocketException if the option is unrecognized, 
     * the socket is closed, or some low-level error occurred 
     */

    public void 
	setOption(int optID, Object value) throws SocketException;

    /**
     * Fetch the value of an option.  
     * Binary options will return java.lang.Boolean(true) 
     * if enabled, java.lang.Boolean(false) if disabled, e.g.:
     * <BR><PRE>
     * SocketImpl s;
     * ...
     * Boolean noDelay = (Boolean)(s.getOption(TCP_NODELAY));
     * if (noDelay.booleanValue()) {
     *     // true if TCP_NODELAY is enabled...
     * ...
     * }
     * </PRE>
     * <P>
     * For options that take a particular type as a parameter,
     * getOption(int) will return the paramter's value, else
     * it will return java.lang.Boolean(false):
     * <PRE>
     * Object o = s.getOption(SO_LINGER);
     * if (o instanceof Integer) {
     *     System.out.print("Linger time is " + ((Integer)o).intValue());
     * } else {
     *   // the true type of o is java.lang.Boolean(false);
     * }
     * </PRE>
     *
     * @throws SocketException if the socket is closed
     * @throws SocketException if <I>optID</I> is unknown along the
     *         protocol stack (including the SocketImpl)
     */
      
    public Object getOption(int optID) throws SocketException;

    /**
     * The java-supported BSD-style options.
     */

    /**
     * Disable Nagle's algorithm for this connection.  Written data
     * to the network is not buffered pending acknowledgement of
     * previously written data.  
     *<P>
     * Valid for TCP only: SocketImpl.
     * <P>
     * @see Socket#setTcpNoDelay
     * @see Socket#getTcpNoDelay
     */

    public final static int TCP_NODELAY = 0x0001;

    /**
     * Fetch the local address binding of a socket (this option cannot
     * be "set" only "gotten", since sockets are bound at creation time,
     * and so the locally bound address cannot be changed).  The default local
     * address of a socket is INADDR_ANY, meaning any local address on a
     * multi-homed host.  A multi-homed host can use this option to accept
     * connections to only one of its addresses (in the case of a 
     * ServerSocket or DatagramSocket), or to specify its return address 
     * to the peer (for a Socket or DatagramSocket).  The parameter of 
     * this option is an InetAddress.
     * <P>
     * This option <B>must</B> be specified in the constructor.
     * <P>
     * Valid for: SocketImpl, DatagramSocketImpl
     * <P>
     * @see Socket#getLocalAddress
     * @see Server#getLocalAddress
     * @see DatagramSocket#getLocalAddress
     */

    public final static int SO_BINDADDR = 0x000F;

    /** Sets SO_REUSEADDR for a socket.  This is used only for MulticastSockets
     * in java, and it is set by default for MulticastSockets.
     * <P>
     * Valid for: DatagramSocketImpl
     */

    public final static int SO_REUSEADDR = 0x04;

    /** Set which outgoing interface on which to send multicast packets.  
     * Useful on hosts with multiple network interfaces, where applications
     * want to use other than the system default.  Takes/returns an InetAddress.
     * <P>
     * Valid for Multicast: DatagramSocketImpl
     * <P>
     * @see MulticastSocket#setInterface
     * @see MulitcastSocket#getInterface
     */
     
    public final static int IP_MULTICAST_IF = 0x10;

    /**
     * Specify a linger-on-close timeout.  This option disables/enables 
     * immediate return from a <B>close()</B> of a TCP Socket.  Enabling 
     * this option with a non-zero Integer <I>timeout</I> means that a 
     * <B>close()</B> will block pending the transmission and acknowledgement
     * of all data written to the peer, at which point the socket is closed
     * <I>gracefully</I>.  Upon reaching the linger timeout, the socket is
     * closed <I>forcefully</I>, with a TCP RST. Enabling the option with a 
     * timeout of zero does a forceful close immediately.
     * <P>
     * <B>Note:</B>The actual implementation of SO_LINGER in the OS varies 
     * across platforms.
     * <P>
     * Valid only for TCP: SocketImpl
     * <P>
     * @see Socket#setSoLinger
     * @see Socket#getSoLinger
     */

    public final static int SO_LINGER = 0x0080;

    /** Set a timeout on blocking Socket operations:
     * <PRE>
     * ServerSocket.accept();
     * SocketInputStream.read();
     * DatagramSocket.receive();
     * </PRE>
     * <P>
     * The option must be set prior to entering a blocking operation to take effect.
     * If the timeout expires and the operation would continue to block,
     * <B>java.io.InterruptedIOException</B> is raised.  The Socket is not closed
     * in this case.
     * <P>
     * Valid for all sockets: SocketImpl, DatagramSocketImpl
     * <P>
     * @see Socket#setSoTimeout
     * @see ServerSocket#setSoTimeout
     * @see DatagramSocket#setSoTimeout
     */

    public final static int SO_TIMEOUT = 0x1006;
}






