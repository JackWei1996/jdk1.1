/*
 * @(#)SocketInputStream.java	1.17 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;
import java.io.FileInputStream;

/**
 * 此流扩展 FileInputStream 以实现 SocketInputStream。请注意，这个类应该 NOT 是公共的。
 *
 * @version     1.17, 12/12/01
 * @author	Jonathan Payne
 * @author	Arthur van Hoff
 */
class SocketInputStream extends FileInputStream
{
    private boolean eof;
    private SocketImpl impl;
    private byte temp[] = new byte[1];

    /**
     * 创建一个新的 SocketInputStream。只能由 Socket 调用。这个方法需要挂在所有者的 Socket 上，这样 fd 就不会被关闭。
     * @param impl the implemented socket input stream
     */
    SocketInputStream(SocketImpl impl) throws IOException {
	super(impl.getFileDescriptor());
	this.impl = impl;
    }

    /** 
     * 使用接收到的套接字原语读取指定偏移量的字节数组。
     * @param b the buffer into which the data is read
     * @param off the start offset of the data
     * @param len the maximum number of bytes read
     * @return the actual number of bytes read, -1 is
     *          returned when the end of the stream is reached. 
     * @exception IOException If an I/O error has occurred.
     */
    private native int socketRead(byte b[], int off, int len)
	throws IOException;

    /** 
     * Reads into a byte array data from the socket. 
     * @param b the buffer into which the data is read
     * @return the actual number of bytes read, -1 is
     *          returned when the end of the stream is reached. 
     * @exception IOException If an I/O error has occurred. 
     */
    public int read(byte b[]) throws IOException {
	return read(b, 0, b.length);
    }

    /** 
     * Reads into a byte array <i>b</i> at offset <i>off</i>, 
     * <i>length</i> bytes of data.
     * @param b the buffer into which the data is read
     * @param off the start offset of the data
     * @param len the maximum number of bytes read
     * @return the actual number of bytes read, -1 is
     *          returned when the end of the stream is reached. 
     * @exception IOException If an I/O error has occurred.
     */
    public int read(byte b[], int off, int length) throws IOException {
	if (eof) {
	    return -1;
	}
	int n = socketRead(b, off, length);
	if (n <= 0) {
	    eof = true;
	    return -1;
	}
	return n;
    }

    /** 
     * Reads a single byte from the socket. 
     */
    public int read() throws IOException {
	if (eof) {
	    return -1;
	}

 	int n = read(temp, 0, 1);
	if (n <= 0) {
	    return -1;
	}
	return temp[0] & 0xff;
    }

    /** 
     * Skips n bytes of input.
     * @param n the number of bytes to skip
     * @return	the actual number of bytes skipped.
     * @exception IOException If an I/O error has occurred.
     */
    public long skip(long numbytes) throws IOException {
	if (numbytes <= 0) {
	    return 0;
	}
	long n = numbytes;
	int buflen = (int) Math.min(1024, n);
	byte data[] = new byte[buflen];
	while (n > 0) {
	    int r = read(data, 0, (int) Math.min((long) buflen, n));
	    if (r < 0) {
		break;
	    }
	    n -= r;
	}
	return numbytes - n;
    }

    /**
     * Returns the number of bytes that can be read without blocking.
     * @return the number of immediately available bytes
     */
    public int available() throws IOException {
	return impl.available();
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

