/*
 * @(#)SocketImplFactory.java	1.10 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 该接口为套接字实现定义了一个工厂。 <code>Socket<code> 和 <code>ServerSocket<code> 类使用它来创建实际的套接字实现。
 *
 * @author  Arthur van Hoff
 * @version 1.10, 12/12/01
 * @see     Socket
 * @see     ServerSocket
 * @since   JDK1.0
 */
public 
interface SocketImplFactory {
    /**
     * Creates a new <code>SocketImpl</code> instance.
     *
     * @return  a new instance of <code>SocketImpl</code>.
     * @see     java.io.SocketImpl
     * @since   JDK1.0
     */
    SocketImpl createSocketImpl();
}
