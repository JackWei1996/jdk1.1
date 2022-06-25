/*
 * @(#)SimpleBeanInfo.java	1.19 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 这是一个支持类，使人们更容易提供 BeanInfo 类。
 *
 * 它默认提供“noop”信息，并且可以选择性地覆盖以提供有关所选主题的更明确的信息。
 * 当自省器看到“noop”值时，它将应用低级自省和设计模式来自动分析目标 bean。
 */

public class SimpleBeanInfo implements BeanInfo {

    /**
     * 否认有关 bean 的类和定制器的知识。如果您希望提供明确的信息，您可以覆盖它。
     */
    public BeanDescriptor getBeanDescriptor() {
	return null;
    }

    /**
     * 否认属性知识。如果您希望提供明确的属性信息，您可以覆盖它。
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
	return null;
    }

    /**
     * 拒绝了解默认属性。如果你想为 bean 定义一个默认属性，你可以覆盖它。
     */
    public int getDefaultPropertyIndex() {
	return -1;
    }

    /**
     * 否认事件集的知识。如果您希望提供明确的事件集信息，您可以覆盖它。
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
	return null;
    }

    /**
     * 拒绝了解默认事件。如果您希望为 bean 定义一个默认事件，您可以覆盖它。
     */
    public int getDefaultEventIndex() {
	return -1;
    }

    /**
     * 否认方法知识。如果您希望提供明确的方法信息，您可以覆盖它。
     */
    public MethodDescriptor[] getMethodDescriptors() {
	return null;
    }

    /**
     * 声称没有其他相关的 BeanInfo 对象。如果你想（例如）返回一个基类的 BeanInfo，你可以覆盖它。
     */
    public BeanInfo[] getAdditionalBeanInfo() {
	return null;
    }

    /**
     * 声称没有可用的图标。如果你想为你的 bean 提供图标，你可以覆盖它。
     */
    public java.awt.Image getIcon(int iconKind) {
	return null;
    }

    /**
     * 这是一种帮助加载图标图像的实用方法。
     * 它获取与当前对象的类文件关联的资源文件的名称，并从该文件加载图像对象。通常图像将是 GIF。
     *
     * @param resourceName  相对于保存当前类的类文件的目录的路径名。例如，“wombat.gif”。
     * @return  an image object.  May be null if the load failed.
     */
    public java.awt.Image loadImage(String resourceName) {
	try {
	    java.net.URL url = getClass().getResource(resourceName);
	    java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
	    return tk.createImage((java.awt.image.ImageProducer) url.getContent());
	} catch (Exception ex) {
	    return null;
	}
    }

}
