/*
 * @(#)SocketOutputStream.java	1.13 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 此流扩展 FileOutputStream 以实现 SocketOutputStream。请注意，这个类应该 NOT是公共的。
 *
 * @version     1.13, 12/12/01
 * @author 	Jonathan Payne
 * @author	Arthur van Hoff
 */
class SocketOutputStream extends FileOutputStream
{
    private SocketImpl impl;
    private byte temp[] = new byte[1];
    
    /**
     * 创建一个新的 SocketOutputStream。只能由 Socket 调用。这个方法需要挂在所有者的 Socket 上，这样 fd 就不会被关闭。
     * @param impl the socket output stream inplemented
     */
    SocketOutputStream(SocketImpl impl) throws IOException {
	super(impl.getFileDescriptor());
	this.impl = impl;
    }

    /**
     * Writes to the socket.
     * @param b the data to be written
     * @param off the start offset in the data
     * @param len the number of bytes that are written
     * @exception IOException If an I/O error has occurred.
     */
    private native void socketWrite(byte b[], int off, int len)
	throws IOException;

    /** 
     * Writes a byte to the socket. 
     * @param b the data to be written
     * @exception IOException If an I/O error has occurred. 
     */
    public void write(int b) throws IOException {
	temp[0] = (byte)b;
	socketWrite(temp, 0, 1);
    }

    /** 
     * Writes the contents of the buffer <i>b</i> to the socket.
     * @param b the data to be written
     * @exception SocketException If an I/O error has occurred. 
     */
    public void write(byte b[]) throws IOException {
	socketWrite(b, 0, b.length);
    }

    /** 
     * Writes <i>length</i> bytes from buffer <i>b</i> starting at 
     * offset <i>len</i>.
     * @param b the data to be written
     * @param off the start offset in the data
     * @param len the number of bytes that are written
     * @exception SocketException If an I/O error has occurred.
     */
    public void write(byte b[], int off, int len) throws IOException {
	socketWrite(b, off, len);
    }

    /**
     * Closes the stream.
     */
    public void close() throws IOException {
	impl.close();
    }

    /** 
     * Overrides finalize, the fd is closed by the Socket.
     */
    protected void finalize() {}
}
