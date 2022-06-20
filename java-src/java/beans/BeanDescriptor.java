/*
 * @(#)BeanDescriptor.java	1.12 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * BeanDescriptor 提供关于“bean”的全局信息，包括它的 Java 类、它的 displayName 等。
 *
 * 这是 BeanInfo 对象返回的描述符类型之一，它还返回属性、方法和事件的描述符。
 */

public class BeanDescriptor extends FeatureDescriptor {

    /**
     * 为没有定制器的 bean 创建一个 BeanDescriptor。
     * @param beanClass 实现 bean 的 Java 类的 Class 对象。例如 sun.beans.OurButton.class。
     */
    public BeanDescriptor(Class beanClass) {
	this(beanClass, null);
    }

    /**
     * 为具有定制器的 bean 创建一个 BeanDescriptor。
     * @param beanClass 实现 bean 的 Java 类的 Class 对象。例如 sun.beans.OurButton.class。
     * @param customizerClass 实现 bean 的定制器的 Java 类的 Class 对象。
     *        例如 sun.beans.OurButtonCustomizer.class。
     */
    public BeanDescriptor(Class beanClass, Class customizerClass) {
	this.beanClass = beanClass;
	this.customizerClass = customizerClass;
	String name = beanClass.getName();
	while (name.indexOf('.') >= 0) {
	    name = name.substring(name.indexOf('.')+1);
	}
	setName(name);
    }

    /**
     * @return The Class object for the bean.
     */
    public Class getBeanClass() {
	return beanClass;
    }

    /**
     * @return bean 的定制器的 Class 对象。如果 bean 没有定制器，这可能为 null。
     */
    public Class getCustomizerClass() {
	return customizerClass;
    }

    private Class beanClass;
    private Class customizerClass;

}
