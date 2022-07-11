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
     * 设置（或更改）要编辑的对象。诸如“int”之类的内置类型必须包装为相应的对象类型，例如“java.lang.Integer”。
     *
     * @param value 要编辑的新目标对象。请注意，此对象不应由 PropertyEditor 修改，
     *              而 PropertyEditor 应创建一个新对象来保存任何修改后的值。
     */
    void setValue(Object value);

    /**
     * @return 财产的价值。诸如“int”之类的内置类型将被包装为相应的对象类型，例如“java.lang.Integer”。
     */

    Object getValue();

    //----------------------------------------------------------------------

    /**
     * @return  True if the class will honor the paintValue method.
     */

    boolean isPaintable();

    /**
     * 将值的表示绘制到屏幕空间的给定区域。请注意，propertyEditor 负责进行自己的剪辑，以使其适合给定的矩形。
     *
     * 如果 PropertyEditor 不支持绘制请求（请参阅 isPaintable），则此方法应该是静默 noop。
     *
     * 给定的 Graphics 对象将具有父容器的默认字体、颜色等。 PropertyEditor 可以更改字体和颜色等图形属性，不需要恢复旧值。
     *
     * @param gfx  Graphics object to paint into.
     * @param box  Rectangle within graphics object into which we should paint.
     */
    void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box);

    //----------------------------------------------------------------------

    /**
     * 此方法旨在在生成 Java 代码以设置属性值时使用。它应该返回一段 Java 代码，该代码可用于使用当前属性值初始化变量。
     *
     * Example results are "2", "new Color(127,127,34)", "Color.orange", etc.
     *
     * @return 表示当前值的初始值设定项的 Java 代码片段。
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
     * 通过解析给定的字符串来设置属性值。如果字符串格式错误或此类属性无法表示为文本，
     * 则可能引发 java.lang.IllegalArgumentException。
     * @param text  The string to be parsed.
     */
    void setAsText(String text) throws IllegalArgumentException;

    //----------------------------------------------------------------------

    /**
     * 如果属性值必须是一组已知标记值之一，则此方法应返回标记数组。这可用于表示（例如）枚举值。
     * 如果 PropertyEditor 支持标签，那么它应该支持使用带有标签值的
     * setAsText 作为设置值的一种方式，并支持使用 getAsText 来识别当前值。
     *
     * @return 此属性的标记值。如果此属性不能表示为标记值，则可能为 null。
     *	
     */
    String[] getTags();

    //----------------------------------------------------------------------

    /**
     * PropertyEditor 可以选择提供一个完整的自定义组件来编辑其属性值。
     * PropertyEditor 负责将自己连接到其编辑器组件本身并通过触发 PropertyChange 事件来报告属性值更改。
     *
     * 调用 getCustomEditor 的高级代码可以将组件嵌入到一些更大的属性表中，也可以将其放在自己的单独对话框中，或者...
     *
     * @return 允许人类直接编辑当前属性值的 java.awt.Component。如果不支持，则可能为 null。
     */

    java.awt.Component getCustomEditor();

    /**
     * @return  True if the propertyEditor can provide a custom editor.
     */
    boolean supportsCustomEditor();
  
    //----------------------------------------------------------------------

    /**
     * 为 PropertyChange 事件注册一个侦听器。当 PropertyEditor 更改其值时，
     * 它应在所有已注册的 PropertyChangeListener 上触发 PropertyChange 事件，指定属性名称的 null 值并将其自身指定为源。
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
