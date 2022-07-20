/*
 * @(#)PropertyDescriptor.java	1.38 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.lang.reflect.*;

/**
 * PropertyDescriptor 描述了 Java Bean 通过一对访问器方法导出的一个属性。
 */

public class PropertyDescriptor extends FeatureDescriptor {

    /**
     * 通过具有 getFoo 和 setFoo 访问器方法，为遵循标准 Java 约定的属性构造 PropertyDescriptor。
	 * 因此，如果参数名称是“fred”，它将假定读取器方法是“getFred”，写入器方法是“setFred”。
	 * 请注意，属性名称应以小写字符开头，在方法名称中将大写。
     *
     * @param propertyName The programmatic name of the property.
     * @param beanClass The Class object for the target bean.  For
     *		example sun.beans.OurButton.class.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public PropertyDescriptor(String propertyName, Class beanClass)
		throws IntrospectionException {
	setName(propertyName);
	String base = capitalize(propertyName);
	writeMethod = Introspector.findMethod(beanClass, "set" + base, 1);
	// If it's a boolean property check for an "isFoo" first.
	if (writeMethod.getParameterTypes()[0] == Boolean.TYPE) {
	    try {
		readMethod = Introspector.findMethod(beanClass, "is" + base, 0);
	    } catch (Exception ex) {
	    }
	}
	if (readMethod == null) {
	    readMethod = Introspector.findMethod(beanClass, "get" + base, 0);
	}
	findPropertyType();
    }

    /**
     * 此构造函数采用简单属性的名称，以及用于读取和写入该属性的方法名称。
     *
     * @param propertyName The programmatic name of the property.
     * @param beanClass The Class object for the target bean.  For
     *		example sun.beans.OurButton.class.
     * @param getterName The name of the method used for reading the property
     *		 value.  May be null if the property is write-only.
     * @param setterName The name of the method used for writing the property
     *		 value.  May be null if the property is read-only.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public PropertyDescriptor(String propertyName, Class beanClass,
		String getterName, String setterName)
		throws IntrospectionException {
	setName(propertyName);
	readMethod = Introspector.findMethod(beanClass, getterName, 0);
	writeMethod = Introspector.findMethod(beanClass, setterName, 1);
	findPropertyType();
    }

    /**
     * This constructor takes the name of a simple property, and Method
     * objects for reading and writing the property.
     *
     * @param propertyName The programmatic name of the property.
     * @param getter The method used for reading the property value.
     *		May be null if the property is write-only.
     * @param setter The method used for writing the property value.  
     *		May be null if the property is read-only.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public PropertyDescriptor(String propertyName, Method getter, Method setter)
		throws IntrospectionException {
	setName(propertyName);
	readMethod = getter;
	writeMethod = setter;
	findPropertyType(); 
    }
    
    /**
     * @return 属性的 Java 类型信息。请注意，“Class”对象可能描述了一个内置的 Java 类型，例如“int”。
	 * 如果这是一个不支持非索引访问的索引属性，则结果可能为“null”。
     * <p>
     * This is the type that will be returned by the ReadMethod.
     */
    public Class getPropertyType() {
	return propertyType;
    }

    /**
     * @return 应该用来读取属性值的方法。如果无法读取该属性，则可能返回 null。
     */
    public Method getReadMethod() {
	return readMethod;
    }

    /**
     * @return The method that should be used to write the property value.
     * May return null if the property can't be written.
     */
    public Method getWriteMethod() {
	return (writeMethod);
    }

    /**
	 * 对“bound”属性的更新将导致在属性更改时触发“PropertyChange”事件。
     *
     * @return True if this is a bound property.
     */
    public boolean isBound() {
	return (bound);
    }

    /**
     * Updates to "bound" properties will cause a "PropertyChange" event to 
     * get fired when the property is changed.
     *
     * @param bound True if this is a bound property.
     */
    public void setBound(boolean bound) {
	this.bound = bound;
    }

    /**
     * Attempted updates to "Constrained" properties will cause a "VetoableChange"
     * event to get fired when the property is changed.
     *
     * @return True if this is a constrained property.
     */
    public boolean isConstrained() {
	return (constrained);
    }

    /**
     * Attempted updates to "Constrained" properties will cause a "VetoableChange"
     * event to get fired when the property is changed.
     *
     * @param constrained True if this is a constrained property.
     */
    public void setConstrained(boolean constrained) {
	this.constrained = constrained;
    }


    /**
     * 通常使用 PropertyEditorManager 可以找到 PropertyEditor。
	 * 但是，如果由于某种原因您想将特定的 PropertyEditor 与给定的属性相关联，那么您可以使用此方法来完成。
     * @param propertyEditorClass  The Class for the desired PropertyEditor.
     */
    public void setPropertyEditorClass(Class propertyEditorClass) {
	this.propertyEditorClass = propertyEditorClass;
    }

    /**
     * @return 已为此属性注册的任何显式 PropertyEditor 类。
	 * 通常这将返回“null”，表示没有注册任何特殊的编辑器，因此应该使用 PropertyEditorManager 来定位合适的 PropertyEditor。
     */
    public Class getPropertyEditorClass() {
	return propertyEditorClass;
    }

    /*
     * 包私有构造函数。合并两个属性描述符。在它们发生冲突的地方，将第二个参数 (y) 优先于第一个参数 (x)。
     * @param x 第一个（低优先级）PropertyDescriptor @param y 第二个（高优先级）PropertyDescriptor
     */

    PropertyDescriptor(PropertyDescriptor x, PropertyDescriptor y) {
	super(x,y);
	readMethod = x.readMethod;
	propertyType = x.propertyType;
	if (y.readMethod != null) {
	    readMethod = y.readMethod;
	}
	writeMethod = x.writeMethod;
	if (y.writeMethod != null) {
	    writeMethod = y.writeMethod;
	}
	propertyEditorClass = x.propertyEditorClass;
	if (y.propertyEditorClass != null) {
	    propertyEditorClass = y.propertyEditorClass;
	}
	bound = x.bound | y.bound;
	constrained = x.constrained | y.constrained;
	try {
	    findPropertyType();
	} catch (IntrospectionException ex) {
	    // Given we're merging two valid PDs, this "should never happen".
	    throw new Error("PropertyDescriptor: internal error while merging PDs");
	}
    }

    private void findPropertyType() throws IntrospectionException {
	try {
	    propertyType = null;
	    if (readMethod != null) {
		if (readMethod.getParameterTypes().length != 0) {
		    throw new IntrospectionException("bad read method arg count");
		}
		propertyType = readMethod.getReturnType();
		if (propertyType == Void.TYPE) {
		    throw new IntrospectionException("read method " + 
					readMethod.getName() + " returns void");
		}
	    }
	    if (writeMethod != null) {
		Class params[] = writeMethod.getParameterTypes();
		if (params.length != 1) {
		    throw new IntrospectionException("bad write method arg count");
		}
		if (propertyType != null && propertyType != params[0]) {
		    throw new IntrospectionException("type mismatch between read and write methods");
		}
		propertyType = params[0];
	    }
	} catch (IntrospectionException ex) {
	    throw ex;
	}
    }

    private String capitalize(String s) {
	char chars[] = s.toCharArray();
	chars[0] = Character.toUpperCase(chars[0]);
	return new String(chars);
    }

    private Class propertyType;
    private Method readMethod;
    private Method writeMethod;
    private boolean bound;
    private boolean constrained;
    private Class propertyEditorClass;
}
