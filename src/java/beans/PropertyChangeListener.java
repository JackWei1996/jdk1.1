/*
 * @(#)PropertyChangeListener.java	1.11 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 每当 bean 更改“绑定”属性时，都会触发“PropertyChange”事件。
 * 您可以使用源 bean 注册 PropertyChangeListener，以便在任何绑定的属性更新时收到通知。
 */

public interface PropertyChangeListener extends java.util.EventListener {

    /**
     * 当绑定属性改变时，他的方法被调用。
     * @param evt 描述事件源和已更改属性的 PropertyChangeEvent 对象。
     */

    void propertyChange(PropertyChangeEvent evt);

}
