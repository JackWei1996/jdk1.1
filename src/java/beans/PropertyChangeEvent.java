/*
 * @(#)PropertyChangeEvent.java	1.23 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 每当 bean 更改“绑定”或“约束”属性时，都会传递“PropertyChange”事件。
 * PropertyChangeEvent 对象作为参数发送到 PropertyChangeListener 和 VetoableChangeListener 方法。
 *
 * 通常，PropertyChangeEvents 伴随着名称以及更改的属性的旧值和新值。
 * 如果新值是内置类型（例如 int 或 boolean），则必须将其包装为相应的 java.lang。对象类型（例如 Integer 或 Boolean）。
 *
 * 如果不知道它们的真实值，则可以为旧值和新值提供空值。
 *
 * 事件源可以发送一个空对象作为名称，以指示其属性是否已更改的任意集合。在这种情况下，旧值和新值也应该为空。
 */

public class PropertyChangeEvent extends java.util.EventObject {

    /**
     * @param source  The bean that fired the event.
     * @param propertyName  The programmatic name of the property
     *		that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     */
    public PropertyChangeEvent(Object source, String propertyName,
				     Object oldValue, Object newValue) {
	super(source);
	this.propertyName = propertyName;
	this.newValue = newValue;
	this.oldValue = oldValue;
    }

    /**
     * @return  已更改的属性的编程名称。如果多个属性已更改，则可能为 null。
     */
    public String getPropertyName() {
	return propertyName;
    }
    
    /**
     * @return  属性的新值，表示为 Object。如果多个属性已更改，则可能为 null。
     */
    public Object getNewValue() {
	return newValue;
    }

    /**
     * @return  属性的旧值，表示为 Object。如果多个属性已更改，则可能为 null
     */
    public Object getOldValue() {
	return oldValue;
    }

    /**
     * @param propagationId  The propagationId object for the event.
     */
    public void setPropagationId(Object propagationId) {
	this.propagationId = propagationId;
    }

    /**
     * “propagationId”字段保留供将来使用。在 Beans 1.0 中，唯一的要求是，
     * 如果侦听器捕获一个 PropertyChangeEvent，然后触发它自己的 PropertyChangeEvent，
     * 那么它应该确保它将传播Id 字段从其传入事件传播到其传出事件。
     *
     * @return 与 boundconstrained 属性更新关联的propagationId 对象。
     */
    public Object getPropagationId() {
	return propagationId;
    }

    private String propertyName;
    private Object newValue;
    private Object oldValue;
    private Object propagationId;
}
