/*
 * @(#)IntrospectionException.java	1.8 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;
 
/**
 * 在内省期间发生异常时抛出。
 *
 * 典型原因包括无法将字符串类名称映射到 Class 对象、无法解析字符串方法名称或指定具有错误类型签名的方法名称用于其预期用途。
 */

public
class IntrospectionException extends Exception {

    /**
     * @param mess Descriptive message
     */
    public IntrospectionException(String mess) {
        super(mess);
    }
}
