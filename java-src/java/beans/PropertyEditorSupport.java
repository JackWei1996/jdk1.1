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
     * If the property value must be one of a set of known tagged values, 
     * then this method should return an array of the tag values.  This can
     * be used to represent (for example) enum values.  If a PropertyEditor
     * supports tags, then it should support the use of setAsText with
     * a tag value as a way of setting the value.
     *
     * @return The tag values for this property.  May be null if this 
     *   property cannot be represented as a tagged value.
     *	
     */
    public String[] getTags() {
	return null;
    }

    //----------------------------------------------------------------------

    /**
     * A PropertyEditor may chose to make available a full custom Component
     * that edits its property value.  It is the responsibility of the
     * PropertyEditor to hook itself up to its editor Component itself and
     * to report property value changes by firing a PropertyChange event.
     * <P>
     * The higher-level code that calls getCustomEditor may either embed
     * the Component in some larger property sheet, or it may put it in
     * its own individual dialog, or ...
     *
     * @return A java.awt.Component that will allow a human to directly
     *      edit the current property value.  May be null if this is
     *	    not supported.
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
     * Register a listener for the PropertyChange event.  The class will
     * fire a PropertyChange value whenever the value is updated.
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
