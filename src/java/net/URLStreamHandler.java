/*
 * @(#)URLStreamHandler.java	1.24 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 抽象类 URLStreamHandler 是所有流协议处理程序的通用超类。
 * 流协议处理程序知道如何为特定协议类型建立连接，例如 http、ftp 或 gopher。
 *
 * 在大多数情况下，URLStreamHandler子类的实例不是由应用程序直接创建的。
 * 相反，在构造 URL 时第一次遇到协议名称时，会自动加载适当的流协议处理程序。
 *
 * @author  James Gosling
 *
 * @version 1.24, 12/12/01
 *
 * @see URL#URL(String, String, int,
 * String)
 *
 * @since JDK1.0 
 */
public abstract class URLStreamHandler {
    /**
     * Opens a connection to the object referenced by the 
     * <code>URL</code> argument. 
     * This method should be overridden by a subclass.
     *
     * @param      u   the URL that this connects to.
     *
     * @return a <code>URLConnection</code> object for the
     * <code>URL</code>.
     *
     * @exception  IOException  if an I/O error occurs while opening the
     *               connection.
     * @since JDK1.0 
     */
    abstract protected URLConnection openConnection(URL u) throws IOException;

    /** 
     * 将 URL< 的字符串表示形式解析为 URL 对象。
     *
     * 如果有任何继承的上下文，那么它已经被复制到 URL 参数中。
     *
     * URLStreamHandler 的 parseURL 方法解析字符串表示，就好像它是 http 规范一样。
	 * 大多数 URL 协议系列都有类似的解析。具有不同语法的协议的流协议处理程序必须覆盖此例程。
     *
     * 如果 URL 参数的文件组件包含问号（与 CGI HTTP URL 一样），则上下文被认为是 URL 的文件组件，
	 * 直到问号之前的第一个，不包括问号或它之前的目录。例如，如果 URL 是：
     * <br><pre>    http://www.foo.com/dir/cgi-bin?foo=bar/baz</pre>
     *
     * 并且规范论点是
     * <br><pre>    quux.html</pre>
     *
     * 生成的 URL 将是：
     * <br><pre>    http://www.foo.com/dir/quux.html</pre>.
     * 
     *
     * @param u the <code>URL</code> to receive the result of parsing
     * the spec.
     *
     * @param spec the <code>String</code> representing the URL that
     * must be parsed.
     *
     * @param start the character index at which to begin
     * parsing. This is just past the '<code>:</code>' (if there is
     * one) that specifies the determination of the protocol name.
     *
     * @param limit the character position to stop parsing at. This is
     * the end of the string or the position of the "<code>#</code>"
     * character, if present. All information after the sharp sign
     * indicates an anchor.
     *
     * @since JDK1.0 
     */
    protected void parseURL(URL u, String spec, int start, int limit) {
	String protocol = u.getProtocol();
	String host = u.getHost();
	int port = u.getPort();
	String file = u.getFile();
	String ref = u.getRef();

	int i;
	if ((start <= limit - 2) && (spec.charAt(start) == '/') &&
	    (spec.charAt(start + 1) == '/')) {
	    start += 2;
	    i = spec.indexOf('/', start);
	    if (i < 0) {
		i = limit;
	    }
	    int prn = spec.indexOf(':', start);
	    port = -1;
	    if ((prn < i) && (prn >= 0)) {
		try {
		    port = Integer.parseInt(spec.substring(prn + 1, i));
		} catch(Exception e) {
		    // ignore bogus port numbers
		}
		if (prn > start) {
		    host = spec.substring(start, prn);
		}
	    } else {
		host = spec.substring(start, i);
	    }
	    start = i;
	    file = null;
	} else if (host == null) {
	    host = "";
	}
	if (start < limit) {
	    /* 
	     * If the context URL is a CGI URL, the context to be the
	     * URL's file up to the / before ? character.
	     */
	    if (file != null) {
		int questionMarkIndex = file.indexOf('?');
		if (questionMarkIndex > -1) {
		    int lastSlashIndex = 
			file.lastIndexOf('?', questionMarkIndex);
		    file = file.substring(0, ++lastSlashIndex);
		}
	    }
	    if (spec.charAt(start) == '/') {
		file = spec.substring(start, limit);
	    } else if (file != null && file.length() > 0) {
		/* relative to the context file - use either 
		 * Unix separators || platform separators */
		int ind = Math.max(file.lastIndexOf('/'), 
				   file.lastIndexOf(File.separatorChar));

		file = file.substring(0, ind) + "/" + spec.substring(start, 
								     limit);
	    } else {
		file = "/" + spec.substring(start, limit);
	    }
	}
	if ((file == null) || (file.length() == 0)) {
	    file = "/"; 
	}
	while ((i = file.indexOf("/./")) >= 0) {
	    file = file.substring(0, i) + file.substring(i + 2);
	}
	while ((i = file.indexOf("/../")) >= 0) {
	    if ((limit = file.lastIndexOf('/', i - 1)) >= 0) {
		file = file.substring(0, limit) + file.substring(i + 3);
	    } else {
		file = file.substring(i + 3);
	    }
	}

	setURL(u, protocol, host, port, file, ref);
    }

    /**
     * Converts a <code>URL</code> of a specific protocol to a 
     * <code>String</code>. 
     *
     * @param   u   the URL.
     * @return  a string representation of the <code>URL</code> argument.
     * @since   JDK1.0
     */
    protected String toExternalForm(URL u) {
	String result = u.getProtocol() + ":";
	if ((u.getHost() != null) && (u.getHost().length() > 0)) {
	    result = result + "//" + u.getHost();
	    if (u.getPort() != -1) {
		result += ":" + u.getPort();
	    }
	}
	result += u.getFile();
	if (u.getRef() != null) {
	    result += "#" + u.getRef();
	}
	return result;
    }

    /**
     * Sets the fields of the <code>URL</code> argument to the
     * indicated values.  Only classes derived from URLStreamHandler
     * are supposed to be able to call the set method on a URL.
     *
     * @param   u         the URL to modify.
     * @param   protocol  the protocol name.
     * @param   host      the remote host value for the URL.
     * @param   port      the port on the remote machine.
     * @param   file      the file.
     * @param   ref       the reference.
     *
     * @see URL#set(String, String, int,
     * String, String)
     *
     * @since JDK1.0 
     */
    protected void setURL(URL u, String protocol, String host, int port,
			  String file, String ref) {
	if (this != u.handler) {
	    throw new SecurityException("handler for url different from " +
					"this handler");
	}
	// ensure that no one can reset the protocol on a given URL.
        u.set(u.getProtocol(), host, port, file, ref);
    }
}
