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
     * 此构造函数采用简单属性的名称，以及用于读取和写入属性的方法名称，包括索引的和非索引的。
     *
     * @param propertyName The programmatic name of the property.
     * @param beanClass  The Class object for the target bean.
     * @param getterName 用于将属性值作为数组读取的方法的名称。如果属性是只写的或必须被索引，则可能为 null。
     * @param setterName 用于将属性值写入数组的方法的名称。如果属性是只读的或必须被索引，则可能为 null。
     * @param indexedGetterName 用于读取索引属性值的方法的名称。如果属性是只写的，则可能为 null。
     * @param indexedSetterName 用于写入索引属性值的方法的名称。如果属性是只读的，则可能为 null。
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
     * 此构造函数采用简单属性的名称，以及用于读取和写入该属性的 Method 对象。
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
     * @return 应该用于读取索引属性值的方法。如果属性未编入索引或为只写，则可能返回 null。
     */
    public Method getIndexedReadMethod() {
	return indexedReadMethod;
    }

    /**
     * @return 用于写入索引属性值的方法。如果属性未编入索引或为只读，则可能返回 null。
     */
    public Method getIndexedWriteMethod() {
	return indexedWriteMethod;
    }

    /**
     * @return 索引属性类型的 Java 类。请注意，该类可以描述原始 Java 类型，例如“int”。
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
     * 包私有构造函数。合并两个属性描述符。在它们发生冲突的地方，将第二个参数 (y) 优先于第一个参数 (x)。
     * @param x 第一个（低优先级）PropertyDescriptor @param y 第二个（高优先级）PropertyDescriptor
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
