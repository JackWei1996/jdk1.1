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
     * 对于将特定类型作为参数的选项，getOption(int) 将返回参数的值，否则将返回 java.lang.Boolean(false)：
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
     * 禁用此连接的 Nagle 算法。写入网络的数据不会缓冲等待对先前写入数据的确认。
     *<P>
     * Valid for TCP only: SocketImpl.
     * <P>
     * @see Socket#setTcpNoDelay
     * @see Socket#getTcpNoDelay
     */

    public final static int TCP_NODELAY = 0x0001;

    /**
     * 获取套接字的本地地址绑定（此选项不能“设置”，只能“获取”，因为套接字是在创建时绑定的，因此不能更改本地绑定地址）。
     * 套接字的默认本地地址是 INADDR_ANY，表示多宿主主机上的任何本地地址。
     * 多宿主主机可以使用此选项仅接受到其地址之一的连接（在 ServerSocket 或 DatagramSocket 的情况下），
     * 或指定其到对等方的返回地址（对于 Socket 或 DatagramSocket）。此选项的参数是 InetAddress。
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

    /**
     * 设置套接字的SO_REUSEADDR。这仅用于MulticastSockets
     * 在java中，默认情况下为MulticastSockets设置。
     * <P>
     * Valid for: DatagramSocketImpl
     */

    public final static int SO_REUSEADDR = 0x04;

    /** 设置在哪个传出接口上发送多播数据包。
     * 适用于具有多个网络接口的主机，其中应用程序要使用系统默认值以外的其他值。获取/返回InetAddress。
     * <P>
     * Valid for Multicast: DatagramSocketImpl
     * <P>
     * @see MulticastSocket#setInterface
     * @see MulitcastSocket#getInterface
     */
     
    public final static int IP_MULTICAST_IF = 0x10;

    /**
     * 指定关闭超时时的延迟。此选项禁用/启用从TCP套接字的<B>close（）</B>立即返回。
     * 有可能此选项具有非零整数<I>超时</I>意味着<B>close（）</B>将阻塞，
     * 等待传输和确认写入到对等方的所有数据，此时套接字关闭
     * <I>优雅</I>。达到延迟超时时，套接字为使用TCP RST强制关闭<I>。使用
     * 超时为零会立即强制关闭。
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






