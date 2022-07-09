/*
 * @(#)FeatureDescriptor.java	1.17 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * FeatureDescriptor 类是 PropertyDescriptor、EventSetDescriptor、MethodDescriptor 等的通用基类。
 *
 * 它支持一些可以为任何自省描述符设置和检索的通用信息。
 *
 * 此外，它还提供了一种扩展机制，以便任意属性值对可以与设计特征相关联。
 */

public class FeatureDescriptor {


    public FeatureDescriptor() {
    }

    /**
     * @return The programmatic name of the property/method/event
     */
    public String getName() {
	return name;
    }

    /**
     * @param name  The programmatic name of the property/method/event
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return The localized display name for the property/method/event.
     *	This defaults to the same as its programmatic name from getName.
     */
    public String getDisplayName() {
	if (displayName == null) {
	    return getName();
	}
	return displayName;
    }

    /**
     * @param displayName  The localized display name for the
     *		property/method/event.
     */
    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    /**
     * "expert"标志用于区分那些为专家用户准备的功能和那些为普通用户准备的功能。
     *
     * @return True if this feature is intended for use by experts only.
     */
    public boolean isExpert() {
	return expert;
    }

    /**
     * The "expert" flag is used to distinguish between features that are
     * intended for expert users from those that are intended for normal users.
     *
     * @param expert True if this feature is intended for use by experts only.
     */
    public void setExpert(boolean expert) {
	this.expert = expert;
    }

    /**
     * "hidden" 标志用于识别仅供工具使用且不应暴露给人类的特征。
     *
     * @return True if this feature should be hidden from human users.
     */
    public boolean isHidden() {
	return hidden;
    }

    /**
     * The "hidden" flag is used to identify features that are intended only
     * for tool use, and which should not be exposed to humans.
     *
     * @param hidden  True if this feature should be hidden from human users.
     */
    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    /**
     * @return  A localized short description associated with this 
     *   property/method/event.  This defaults to be the display name.
     */
    public String getShortDescription() {
	if (shortDescription == null) {
	    return getDisplayName();
	}
	return shortDescription;
    }

    /**
     * 您可以将简短的描述性字符串与功能相关联。通常这些描述性字符串应该少于大约 40 个字符。
     * @param text  A (localized) short description to be associated with
     * this property/method/event.
     */
    public void setShortDescription(String text) {
	shortDescription = text;
    }

    /**
     * Associate a named attribute with this feature.
     * @param attributeName  The locale-independent name of the attribute
     * @param value  The value.
     */
    public void setValue(String attributeName, Object value) {
	if (table == null) {
	    table = new java.util.Hashtable();
	}
	table.put(attributeName, value);
    }

    /**
     * Retrieve a named attribute with this feature.
     * @param attributeName  The locale-independent name of the attribute
     * @return  The value of the attribute.  May be null if
     *	   the attribute is unknown.
     */
    public Object getValue(String attributeName) {
	if (table == null) {
	   return null;
	}
	return table.get(attributeName);
    }

    /**
     * @return 已使用 setValue 注册的任何属性的与区域设置无关的名称的枚举。
     */
    public java.util.Enumeration attributeNames() {
	if (table == null) {
	    table = new java.util.Hashtable();
	}
	return table.keys();
    }

    /**
     * 包私有构造函数，合并来自两个 FeatureDescriptor 的信息。
     * 合并的隐藏和专家标志是通过对值进行或运算形成的。
     * 在其他冲突的情况下，第二个参数 (y) 优先于第一个参数 (x)。
     * @param x  The first (lower priority) MethodDescriptor
     * @param y  The second (higher priority) MethodDescriptor
     */
    FeatureDescriptor(FeatureDescriptor x, FeatureDescriptor y) {
	expert = x.expert | y.expert;
	hidden = x.hidden | y.hidden;
	name = y.name;
	shortDescription = x.shortDescription;
	if (y.shortDescription != null) {
	    shortDescription = y.shortDescription;
	}
	displayName = x.displayName;
	if (y.displayName != null) {
	    displayName = y.displayName;
	}
	addTable(x.table);
	addTable(y.table);
    }

    private void addTable(java.util.Hashtable t) {
	if (t == null) {
	    return;
	}
	java.util.Enumeration keys = t.keys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    Object value = t.get(key);
	    setValue(key, value);
	}
    }

    private boolean expert;
    private boolean hidden;
    private String shortDescription;
    private String name;
    private String displayName;
    private java.util.Hashtable table;
}
