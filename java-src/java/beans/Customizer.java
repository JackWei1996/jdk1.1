/*
 * @(#)Customizer.java	1.13 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 定制器类为定制目标 Java Bean 提供了完整的定制 GUI。
 *
 * 每个定制器都应该从 java.awt.Component 类继承，以便可以在 AWT 对话框或面板中对其进行实例化。
 *
 * 每个定制器都应该有一个 null 构造函数。
 */

public interface Customizer {

    /**
     * 设置要自定义的对象。在将定制器添加到任何父 AWT 容器之前，该方法只应调用一次。
     * @param bean  The object to be customized.
     */
    void setObject(Object bean);

    /**
     * 为 PropertyChange 事件注册一个侦听器。
     * 每当定制器以可能需要刷新显示的属性的方式更改目标 bean 时，它都应该触发 PropertyChange 事件。
     *
     * @param listener 触发 PropertyChange 事件时要调用的对象。
     */
     void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * 删除 PropertyChange 事件的侦听器。
     *
     * @param listener 要删除的 PropertyChange 侦听器。
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}

