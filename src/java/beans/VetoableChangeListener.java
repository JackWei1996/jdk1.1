/*
 * @(#)VetoableChangeListener.java	1.8 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 每当 bean 更改“受约束的”属性时，都会触发 VetoableChange 事件。
 * 您可以使用源 bean 注册 VetoableChangeListener，以便在任何受约束的属性更新时收到通知。
 */
public interface VetoableChangeListener extends java.util.EventListener {
    /**
     * 更改受约束的属性时调用此方法。
     *
     * @param     evt 描述事件源和已更改属性的 PropertyChangeEvent 对象。
     * @exception java.beans.PropertyVetoException 如果接收者希望回滚属性更改。
     */
    void vetoableChange(PropertyChangeEvent evt)
				throws PropertyVetoException;
}
