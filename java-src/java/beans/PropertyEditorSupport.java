/*
 * @(#)PropertyEditorSupport.java	1.8 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * This is a support class to help build property editors.
 * <p>
 * It can be used either as a base class or as a delagatee.
 */

import java.beans.*;

public class PropertyEditorSupport implements PropertyEditor {

    /**
     * Constructor for use by derived PropertyEditor classes.
     */

    protected PropertyEditorSupport() {
	source = this;
    }

    /**
     * Constructor for use when a PropertyEditor is delegating to us.
     * @param source  The source to use for any events we fire.
     */

    protected PropertyEditorSupport(Object source) {
	this.source = source;
    }

    /**
     * Set (or change) the object that is to be edited.
     * @param value 要编辑的新目标对象。请注意，此对象不应由 PropertyEditor 修改，
     *              而 PropertyEditor 应创建一个新对象来保存任何修改后的值。
     */
    public void setValue(Object value) {
	this.value = value;
	firePropertyChange();
    }

    /**
     * @return The value of the property.
     */

    public Object getValue() {
	return value;
    }

    //----------------------------------------------------------------------

    /**
     * @return  True if the class will honor the paintValue method.
     */

    public boolean isPaintable() {
	return false;
    }

    /**
     * 将值的表示绘制到屏幕空间的给定区域。请注意，propertyEditor 负责进行自己的剪辑，以使其适合给定的矩形。
     *
     * 如果 PropertyEditor 不支持绘制请求（请参阅 isPaintable），则此方法应该是静默 noop。
     *
     * @param gfx  Graphics object to paint into.
     * @param box  Rectangle within graphics object into which we should paint.
     */
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
    }

    //----------------------------------------------------------------------

    /**
     * 此方法旨在在生成 Java 代码以设置属性值时使用。它应该返回一段 Java 代码，该代码可用于使用当前属性值初始化变量。
     *
     * Example results are "2", "new Color(127,127,34)", "Color.orange", etc.
     *
     * @return A fragment of Java code representing an initializer for the
     *   	current value.
     */
    public String getJavaInitializationString() {
	return "???";
    }

    //----------------------------------------------------------------------

    /**
     * @return The property value as a string suitable for presentation
     *       to a human to edit.
     * <p>   Returns "null" is the value can't be expressed as a string.
     * <p>   如果返回非空值，则 PropertyEditor 应准备好在 setAsText() 中解析该字符串。
     */
    public String getAsText() {
	if (value instanceof String) {
	    return (String)value;
	}
	return ("" + value);
    }

    /**
     * 通过解析给定的字符串来设置属性值。如果字符串格式错误或此类属性无法表示为文本，
     * 则可能引发 java.lang.IllegalArgumentException。
     * @param text  The string to be parsed.
     */
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
	if (value instanceof String) {
	    setValue(text);
	    return;
	}
	throw new java.lang.IllegalArgumentException(text);
    }

    //----------------------------------------------------------------------

    /**
     * 如果属性值必须是一组已知标记值之一，则此方法应返回标记值数组。这可用于表示（例如）枚举值。
     * 如果 PropertyEditor 支持标签，那么它应该支持使用带有标签值的 setAsText 作为设置值的一种方式。
     *
     * @return 此属性的标记值。如果此属性不能表示为标记值，则可能为 null。
     *	
     */
    public String[] getTags() {
	return null;
    }

    //----------------------------------------------------------------------

    /**
     * PropertyEditor 可以选择提供编辑其属性值的完整自定义组件。
     * PropertyEditor 负责将自己连接到其编辑器组件本身并通过触发 PropertyChange 事件来报告属性值更改。
     *
     * 调用 getCustomEditor 的高级代码可以将组件嵌入到一些更大的属性表中，也可以将其放在自己的单独对话框中，或者...
     *
     * @return 允许人类直接编辑当前属性值的 java.awt.Component。如果不支持，则可能为 null。
     */

    public java.awt.Component getCustomEditor() {
	return null;
    }

    /**
     * @return  True if the propertyEditor can provide a custom editor.
     */
    public boolean supportsCustomEditor() {
	return false;
    }
  
    //----------------------------------------------------------------------

    /**
     * 为 PropertyChange 事件注册一个侦听器。每当更新值时，该类都会触发 PropertyChange 值。
     *
     * @param listener  An object to be invoked when a PropertyChange
     *		event is fired.
     */
    public synchronized void addPropertyChangeListener(
				PropertyChangeListener listener) {
	if (listeners == null) {
	    listeners = new java.util.Vector();
	}
	listeners.addElement(listener);
    }

    /**
     * Remove a listener for the PropertyChange event.
     *
     * @param listener  The PropertyChange listener to be removed.
     */
    public synchronized void removePropertyChangeListener(
				PropertyChangeListener listener) {
	if (listeners == null) {
	    return;
	}
	listeners.removeElement(listener);
    }

    /**
     * Report that we have been modified to any interested listeners.
     *
     * @param source  The PropertyEditor that caused the event.
     */
    public void firePropertyChange() {
	java.util.Vector targets;
	synchronized (this) {
	    if (listeners == null) {
	    	return;
	    }
	    targets = (java.util.Vector) listeners.clone();
	}
	// Tell our listeners that "everything" has changed.
        PropertyChangeEvent evt = new PropertyChangeEvent(source, null, null, null);

	for (int i = 0; i < targets.size(); i++) {
	    PropertyChangeListener target = (PropertyChangeListener)targets.elementAt(i);
	    target.propertyChange(evt);
	}
    }

    //----------------------------------------------------------------------

    private Object value;
    private Object source;
    private java.util.Vector listeners;
}
