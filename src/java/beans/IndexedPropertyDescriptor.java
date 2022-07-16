/*
 * @(#)IndexedPropertyDescriptor.java	1.25 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.lang.reflect.*;

/**
 * IndexedPropertyDescriptor 描述了一个类似于数组的属性，并具有索引读取和或索引写入方法来访问数组的特定元素。
 *
 * 索引属性还可以提供简单的非索引读取和写入方法。如果这些存在，它们将读取和写入索引读取方法返回的类型的数组。
 */

public class IndexedPropertyDescriptor extends PropertyDescriptor {

    /**
     * 此构造函数通过具有 getFoo 和 setFoo 访问器方法为索引访问和数组访问构建遵循标准
	 * Java 约定的属性的 IndexedPropertyDescriptor。
     *
     * 因此，如果参数名称是“fred”，它将假定有一个索引读取器方法“getFred”，
	 * 一个非索引（数组）读取器方法也称为“getFred”，一个索引写入器方法“setFred”，最后一个非索引编写器方法“setFred”。
     *
     * @param propertyName The programmatic name of the property.
     * @param beanClass The Class object for the target bean.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public IndexedPropertyDescriptor(String propertyName, Class beanClass)
		throws IntrospectionException {
	this(propertyName, beanClass,
			 "get" + capitalize(propertyName),
			 "set" + capitalize(propertyName),
			 "get" + capitalize(propertyName),
			 "set" + capitalize(propertyName));
    }

    /**
     * This constructor takes the name of a simple property, and method
     * names for reading and writing the property, both indexed
     * and non-indexed.
     *
     * @param propertyName The programmatic name of the property.
     * @param beanClass  The Class object for the target bean.
     * @param getterName The name of the method used for reading the property
     *		 values as an array.  May be null if the property is write-only
     *		 or must be indexed.
     * @param setterName The name of the method used for writing the property
     *		 values as an array.  May be null if the property is read-only
     *		 or must be indexed.
     * @param indexedGetterName The name of the method used for reading
     *		an indexed property value.
     *		May be null if the property is write-only.
     * @param indexedSetterName The name of the method used for writing
     *		an indexed property value.  
     *		May be null if the property is read-only.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public IndexedPropertyDescriptor(String propertyName, Class beanClass,
		String getterName, String setterName,
		String indexedGetterName, String indexedSetterName)
		throws IntrospectionException {
	super(propertyName, beanClass, getterName, setterName);
	indexedReadMethod = Introspector.findMethod(beanClass, indexedGetterName, 1);
	indexedWriteMethod = Introspector.findMethod(beanClass, indexedSetterName, 2);
	findIndexedPropertyType();
    }

    /**
     * This constructor takes the name of a simple property, and Method
     * objects for reading and writing the property.
     *
     * @param propertyName The programmatic name of the property.
     * @param getter The method used for reading the property values as an array.
     *		May be null if the property is write-only or must be indexed.
     * @param setter The method used for writing the property values as an array.
     *		May be null if the property is read-only or must be indexed.
     * @param indexedGetter The method used for reading an indexed property value.
     *		May be null if the property is write-only.
     * @param indexedSetter The method used for writing an indexed property value.  
     *		May be null if the property is read-only.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public IndexedPropertyDescriptor(String propertyName, Method getter, Method setter,
 					    Method indexedGetter, Method indexedSetter)
		throws IntrospectionException {
	super(propertyName, getter, setter);
	indexedReadMethod = indexedGetter;
	indexedWriteMethod = indexedSetter;
	findIndexedPropertyType();
    }
    
    /**
     * @return The method that should be used to read an indexed
     * property value.
     * May return null if the property isn't indexed or is write-only.
     */
    public Method getIndexedReadMethod() {
	return indexedReadMethod;
    }

    /**
     * @return The method that should be used to write an indexed
     * property value.
     * May return null if the property isn't indexed or is read-only.
     */
    public Method getIndexedWriteMethod() {
	return indexedWriteMethod;
    }

    /**
     * @return The Java Class for the indexed properties type.  Note that
     * the Class may describe a primitive Java type such as "int".
     * <p>
     * This is the type that will be returned by the indexedReadMethod.
     */
    public Class getIndexedPropertyType() {
	return indexedPropertyType;
    }


    private void findIndexedPropertyType() throws IntrospectionException {
	try {
	    indexedPropertyType = null;
	    if (indexedReadMethod != null) {
		Class params[] = indexedReadMethod.getParameterTypes();
		if (params.length != 1) {
		    throw new IntrospectionException("bad indexed read method arg count");
		}
		if (params[0] != Integer.TYPE) {
		    throw new IntrospectionException("non int index to indexed read method");
		}
		indexedPropertyType = indexedReadMethod.getReturnType();
		if (indexedPropertyType == Void.TYPE) {
		    throw new IntrospectionException("indexed read method returns void");
		}
	    }
	    if (indexedWriteMethod != null) {
		Class params[] = indexedWriteMethod.getParameterTypes();
		if (params.length != 2) {
		    throw new IntrospectionException("bad indexed write method arg count");
		}
		if (params[0] != Integer.TYPE) {
		    throw new IntrospectionException("non int index to indexed write method");
		}
		if (indexedPropertyType != null && indexedPropertyType != params[1]) {
		    throw new IntrospectionException(
			"type mismatch between indexed read and indexed write methods");
		}
		indexedPropertyType = params[1];
	    }
	    if (indexedPropertyType == null) {
	        throw new IntrospectionException(
			"no indexed getter or setter");
	    }
	    Class propertyType = getPropertyType();
	    if (propertyType != null && (!propertyType.isArray() ||
			propertyType.getComponentType() != indexedPropertyType)) {
	        throw new IntrospectionException(
			"type mismatch between indexed and non-indexed methods");
	    }
	} catch (IntrospectionException ex) {
	    throw ex;
	}
    }


    /*
     * Package-private constructor.
     * Merge two property descriptors.  Where they conflict, give the
     * second argument (y) priority over the first argumnnt (x).
     * @param x  The first (lower priority) PropertyDescriptor
     * @param y  The second (higher priority) PropertyDescriptor
     */

    IndexedPropertyDescriptor(PropertyDescriptor x, PropertyDescriptor y) {
	super(x,y);
	if (x instanceof IndexedPropertyDescriptor) {
	    IndexedPropertyDescriptor ix = (IndexedPropertyDescriptor)x;
	    indexedReadMethod = ix.indexedReadMethod;
	    indexedWriteMethod = ix.indexedWriteMethod;
	    indexedPropertyType = ix.indexedPropertyType;
	}
	if (y instanceof IndexedPropertyDescriptor) {
	    IndexedPropertyDescriptor iy = (IndexedPropertyDescriptor)y;
	    if (iy.indexedReadMethod != null) {
	        indexedReadMethod = iy.indexedReadMethod;
	    }
	    if (iy.indexedWriteMethod != null) {
	        indexedWriteMethod = iy.indexedWriteMethod;
	    }
	    indexedPropertyType = iy.indexedPropertyType;
	}
	
    }


    private static String capitalize(String s) {
	char chars[] = s.toCharArray();
	chars[0] = Character.toUpperCase(chars[0]);
	return new String(chars);
    }

    private Class indexedPropertyType;
    private Method indexedReadMethod;
    private Method indexedWriteMethod;
}
