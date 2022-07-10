/*
 * @(#)PropertyEditor.java	1.28 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * PropertyEditor 类为希望允许用户编辑给定类型的属性值的 GUI 提供支持。
 *
 * PropertyEditor 支持各种不同的显示和更新属性值的方式。大多数 PropertyEditor 只需要支持此 API 中可用的不同选项的子集。
 *
 * 简单的 PropertyEditor 可能只支持 getAsText 和 setAsText 方法，
 * 不需要支持（比如）paintValue 或 getCustomEditor。
 * 更复杂的类型可能无法支持 getAsText 和 setAsText，但会支持 paintValue 和 getCustomEditor。
 *
 * 每个 propertyEditor 都必须支持三种简单显示样式中的一种或多种。
 * 因此，它可以 (1) 支持 isPaintable 或 (2) 从 getTags() 返回一个非空 String[] 并从 getAsText 返回一个非空值，
 * 或者 (3) 从 getAsText() 简单地返回一个非空字符串.
 *
 * 每个属性编辑器都必须支持对 setValue 的调用，当参数对象的类型是 this 对应的 propertyEditor 时。
 * 此外，每个属性编辑器必须要么支持自定义编辑器，要么支持 setAsText。
 *
 * 每个 PropertyEditor 都应该有一个 null 构造函数。
 */

public interface PropertyEditor {

    /**
     * Set (or change) the object that is to be edited.  Builtin types such
     * as "int" must be wrapped as the corresponding object type such as
     * "java.lang.Integer".
     *
     * @param value The new target object to be edited.  Note that this
     *     object should not be modified by the PropertyEditor, rather 
     *     the PropertyEditor should create a new object to hold any
     *     modified value.
     */
    void setValue(Object value);

    /**
     * @return The value of the property.  Builtin types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */

    Object getValue();

    //----------------------------------------------------------------------

    /**
     * @return  True if the class will honor the paintValue method.
     */

    boolean isPaintable();

    /**
     * Paint a representation of the value into a given area of screen
     * real estate.  Note that the propertyEditor is responsible for doing
     * its own clipping so that it fits into the given rectangle.
     * <p>
     * If the PropertyEditor doesn't honor paint requests (see isPaintable)
     * this method should be a silent noop.
     * <p>
     * The given Graphics object will have the default font, color, etc of
     * the parent container.  The PropertyEditor may change graphics attributes
     * such as font and color and doesn't need to restore the old values.
     *
     * @param gfx  Graphics object to paint into.
     * @param box  Rectangle within graphics object into which we should paint.
     */
    void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box);

    //----------------------------------------------------------------------

    /**
     * This method is intended for use when generating Java code to set
     * the value of the property.  It should return a fragment of Java code
     * that can be used to initialize a variable with the current property
     * value.
     * <p>
     * Example results are "2", "new Color(127,127,34)", "Color.orange", etc.
     *
     * @return A fragment of Java code representing an initializer for the
     *   	current value.
     */
    String getJavaInitializationString();

    //----------------------------------------------------------------------

    /**
     * @return The property value as a human editable string.
     * <p>   Returns null if the value can't be expressed as an editable string.
     * <p>   If a non-null value is returned, then the PropertyEditor should
     *	     be prepared to parse that string back in setAsText().
     */
    String getAsText();

    /**
     * Set the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     * @param text  The string to be parsed.
     */
    void setAsText(String text) throws IllegalArgumentException;

    //----------------------------------------------------------------------

    /**
     * If the property value must be one of a set of known tagged values, 
     * then this method should return an array of the tags.  This can
     * be used to represent (for example) enum values.  If a PropertyEditor
     * supports tags, then it should support the use of setAsText with
     * a tag value as a way of setting the value and the use of getAsText
     * to identify the current value.
     *
     * @return The tag values for this property.  May be null if this 
     *   property cannot be represented as a tagged value.
     *	
     */
    String[] getTags();

    //----------------------------------------------------------------------

    /**
     * A PropertyEditor may choose to make available a full custom Component
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

    java.awt.Component getCustomEditor();

    /**
     * @return  True if the propertyEditor can provide a custom editor.
     */
    boolean supportsCustomEditor();
  
    //----------------------------------------------------------------------

    /**
     * Register a listener for the PropertyChange event.  When a
     * PropertyEditor changes its value it should fire a PropertyChange
     * event on all registered PropertyChangeListeners, specifying the
     * null value for the property name and itself as the source.
     *
     * @param listener  An object to be invoked when a PropertyChange
     *		event is fired.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a listener for the PropertyChange event.
     *
     * @param listener  The PropertyChange listener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
