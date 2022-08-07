/*
 * @(#)FileNameMap.java	1.6 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

/**
 * 一个简单的接口，它提供了一种在文件名和 MIME 类型字符串之间进行映射的机制。
 *
 * @version 	1.6, 12/12/01
 * @author  Steven B. Byrne
 * @since   JDK1.1
 */
public interface FileNameMap {
    /**
     * @since JDK1.1
     */
    public String getContentTypeFor(String fileName);
}
