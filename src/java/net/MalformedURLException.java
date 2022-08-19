/*
 * @(#)MalformedURLException.java	1.10 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.IOException;

/**
 * 抛出表示出现了格式错误的 URL。在规范字符串中找不到合法协议，或者无法解析该字符串。
 *
 * @author  Arthur van Hoff
 * @version 1.10, 12/12/01
 * @since   JDK1.0
 */
public class MalformedURLException extends IOException {
    /**
     * Constructs a <code>MalformedURLException</code> with no detail message.
     *
     * @since   JDK1.0
     */
    public MalformedURLException() {
    }

    /**
     * Constructs a <code>MalformedURLException</code> with the 
     * specified detail message. 
     *
     * @param   msg   the detail message.
     * @since   JDK1.0
     */
    public MalformedURLException(String msg) {
	super(msg);
    }
}
