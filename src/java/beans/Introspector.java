/*
 * @(#)Introspector.java	1.74 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.lang.reflect.*;

/**
 * Introspector 类为工具提供了一种了解目标 Java Bean 支持的属性、事件和方法的标准方法。
 *
 * 对于这三种信息中的每一种，Introspector 将分别分析 bean 的类和超类以查找显式或隐式信息，
 * 并使用该信息构建一个全面描述目标 bean 的 BeanInfo 对象。
 *
 * 对于每个类“Foo”，如果存在相应的“FooBeanInfo”类，该类在查询信息时提供非空值，则可以使用显式信息。
 * 我们首先通过获取目标 bean 类的完整包限定名称并附加“BeanInfo”以形成新的类名来查找 BeanInfo 类。
 * 如果这失败了，那么我们取这个名称的最终类名组件，并在 BeanInfo 包搜索路径中指定的每个包中查找该类。
 *
 * 因此，对于诸如“sun.xyz.OurButton”之类的类，我们将首先查找名为“sun.xyz.OurButtonBeanInfo”的 BeanInfo 类，
 * 如果失败，我们将在 BeanInfo 搜索路径中的每个包中查找 OurButtonBeanInfo 类。
 * 使用默认搜索路径，这意味着查找“sun.beans.infos.OurButtonBeanInfo”。
 *
 * 如果一个类提供了关于它自己的显式 BeanInfo，那么我们将它添加到我们通过分析任何派生类获得的 BeanInfo 信息中，
 * 但是我们认为显式信息对于当前类及其基类是确定的，并且不再继续往下超类链。
 *
 * 如果我们没有在类上找到显式的 BeanInfo，我们会使用低级反射来研究类的方法并应用标准设计模式来识别属性访问器、事件源或公共方法。
 * 然后我们继续分析类的超类并添加来自它的信息（并且可能在超类链上）。
 */

public class Introspector {

    //======================================================================
    // 				Public methods
    //======================================================================


    /**
     * 内省 Java bean 并了解它的所有属性、公开的方法和事件。
     *
     * @param beanClass  The bean class to be analyzed.
     * @return  A BeanInfo object describing the target bean.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public static BeanInfo getBeanInfo(Class beanClass) throws IntrospectionException {
	BeanInfo bi = (BeanInfo)beanInfoCache.get(beanClass);
	if (bi == null) {
	    bi = (new Introspector(beanClass, null)).getBeanInfo();
	    beanInfoCache.put(beanClass, bi);
	}
	return bi;
    }

    /**
     * 在给定的“停止”点下对 Java bean 进行内省并了解其所有属性、公开的方法。
     *
     * @param bean The bean class to be analyzed.
     * @param stopClass 停止分析的基类。在分析中将忽略 stopClass 或其基类中的任何方法属性事件。
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public static BeanInfo getBeanInfo(Class beanClass,	Class stopClass)
						throws IntrospectionException {
	return (new Introspector(beanClass, stopClass)).getBeanInfo();
    }

    /**
     * 获取字符串并将其转换为普通 Java 变量名大写的实用方法。这通常意味着将第一个字符从大写转换为小写，
	 * 但是在（不寻常的）特殊情况下，当有多个字符并且第一个和第二个字符都是大写时，我们不理会它。
     *
     * 因此“FooBah”变为“fooBah”，“X”变为“x”，但“URL”仍为“URL”。
     *
     * @param  name The string to be decapitalized.
     * @return  The decapitalized version of the string.
     */
    public static String decapitalize(String name) {
	if (name == null || name.length() == 0) {
	    return name;
	}
	if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
			Character.isUpperCase(name.charAt(0))){
	    return name;
	}
	char chars[] = name.toCharArray();
	chars[0] = Character.toLowerCase(chars[0]);
	return new String(chars);
    }

    /**
     * @return  将搜索以查找 BeanInfo 类的包名称数组。
     * <p>     This is initially set to {"sun.beans.infos"}.
     */

    public static String[] getBeanInfoSearchPath() {
	return searchPath;
    }

    /**
     * 更改将用于查找 BeanInfo 类的包名称列表。
     * @param path  Array of package names.
     */

    public static void setBeanInfoSearchPath(String path[]) {
	searchPath = path;
    }


    //======================================================================
    // 			Private implementation methods
    //======================================================================

    private Introspector(Class beanClass, Class stopClass)
					    throws IntrospectionException {
	this.beanClass = beanClass;

	// Check stopClass is a superClass of startClass.
	if (stopClass != null) {
	    boolean isSuper = false;
	    for (Class c = beanClass.getSuperclass(); c != null; c = c.getSuperclass()) {
	        if (c == stopClass) {
		    isSuper = true;
	        }
	    }
	    if (!isSuper) {
	        throw new IntrospectionException(stopClass.getName() + " not superclass of " + 
					beanClass.getName());
	    }
	}

	informant = findInformant(beanClass);

	if (beanClass.getSuperclass() != stopClass) {
	    if (stopClass == null) {
	        superBeanInfo = Introspector.getBeanInfo(
				beanClass.getSuperclass());
	    } else {
	        superBeanInfo = Introspector.getBeanInfo(
				beanClass.getSuperclass(), stopClass);
	    }
	}
	if (informant != null) {
	    additionalBeanInfo = informant.getAdditionalBeanInfo();
	} 
	if (additionalBeanInfo == null) {
	    additionalBeanInfo = new BeanInfo[0];
	}
    }

   
    private BeanInfo getBeanInfo() throws IntrospectionException {

	// 这里的评估顺序是导入，因为我们在查找属性之前评估事件集并定位 PropertyChangeListeners。
	BeanDescriptor bd = getTargetBeanDescriptor();
	EventSetDescriptor esds[] = getTargetEventInfo();
	int defaultEvent = getTargetDefaultEventIndex();
	PropertyDescriptor pds[] = getTargetPropertyInfo();
	int defaultProperty = getTargetDefaultPropertyIndex();
	MethodDescriptor mds[] = getTargetMethodInfo();

        return new GenericBeanInfo(bd, esds, defaultEvent, pds,
			defaultProperty, mds, informant);
	
    }

    private BeanInfo findInformant(Class beanClass) {
	String name = beanClass.getName() + "BeanInfo";
        try {
	    return (BeanInfo)instantiate(beanClass, name);
	} catch (Exception ex) {
	    // Just drop through
        }
	// Now try checking if the bean is its own BeanInfo.
        try {
	    if (isSubclass(beanClass, BeanInfo.class)) {
	        return (BeanInfo)beanClass.newInstance();
	    }
	} catch (Exception ex) {
	    // Just drop through
        }
	// Now try looking for <searchPath>.fooBeanInfo
   	while (name.indexOf('.') > 0) {
	    name = name.substring(name.indexOf('.')+1);
	}
	for (int i = 0; i < searchPath.length; i++) {
	    try {
		String fullName = searchPath[i] + "." + name;
	        return (BeanInfo)instantiate(beanClass, fullName);
	    } catch (Exception ex) {
	       // Silently ignore any errors.
	    }
	}
	return null;
    }

    /**
     * @return 描述目标 bean 支持的可编辑属性的 PropertyDescriptor 数组。
     */

    private PropertyDescriptor[] getTargetPropertyInfo() throws IntrospectionException {

	// 检查 bean 是否有自己的 BeanInfo 将提供显式信息。
        PropertyDescriptor[] explicit = null;
	if (informant != null) {
	    explicit = informant.getPropertyDescriptors();
	    int ix = informant.getDefaultPropertyIndex();
	    if (ix >= 0 && ix < explicit.length) {
		defaultPropertyName = explicit[ix].getName();
	    }
        }

	if (explicit == null && superBeanInfo != null) {
	    // We have no explicit BeanInfo properties.  Check with our parent.
	    PropertyDescriptor supers[] = superBeanInfo.getPropertyDescriptors();
	    for (int i = 0 ; i < supers.length; i++) {
		addProperty(supers[i]);
	    }
	    int ix = superBeanInfo.getDefaultPropertyIndex();
	    if (ix >= 0 && ix < supers.length) {
		defaultPropertyName = supers[ix].getName();
	    }
	}

	for (int i = 0; i < additionalBeanInfo.length; i++) {
	    PropertyDescriptor additional[] = additionalBeanInfo[i].getPropertyDescriptors();
	    if (additional != null) {
	        for (int j = 0 ; j < additional.length; j++) {
		    addProperty(additional[j]);
	        }
	    }
	}

	if (explicit != null) {
	    // Add the explicit informant data to our results.
	    for (int i = 0 ; i < explicit.length; i++) {
		addProperty(explicit[i]);
	    }

	} else {

	    // Apply some reflection to the current class.

	    // First get an array of all the beans methods at this level
	    Method methodList[] = getDeclaredMethods(beanClass);

	    // Now analyze each method.
	    for (int i = 0; i < methodList.length; i++) {
	        Method method = methodList[i];
	        // skip static and non-public methods.
		int mods = method.getModifiers();
		if (Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
		    continue;
		}
	        String name = method.getName();
	        Class argTypes[] = method.getParameterTypes();
	        Class resultType = method.getReturnType();
		int argCount = argTypes.length;
		PropertyDescriptor pd = null;

		try {

	            if (argCount == 0) {
		        if (name.startsWith("get")) {
		            // Simple getter
	                    pd = new PropertyDescriptor(decapitalize(name.substring(3)),
						method, null);
	                } else if (resultType == boolean.class && name.startsWith("is")) {
		            // Boolean getter
	                    pd = new PropertyDescriptor(decapitalize(name.substring(2)),
						method, null);
		        }
	            } else if (argCount == 1) {
		        if (argTypes[0] == int.class && name.startsWith("get")) {
		            pd = new IndexedPropertyDescriptor(
					decapitalize(name.substring(3)),
					null, null,
					method,	null);
		        } else if (resultType == void.class && name.startsWith("set")) {
		            // Simple setter
	                    pd = new PropertyDescriptor(decapitalize(name.substring(3)),
						null, method);
		            if (throwsException(method, PropertyVetoException.class)) {
			        pd.setConstrained(true);
			    }			
		        }
	            } else if (argCount == 2) {
			    if (argTypes[0] == int.class && name.startsWith("set")) {
	                    pd = new IndexedPropertyDescriptor(
						decapitalize(name.substring(3)),
						null, null,
						null, method);
		            if (throwsException(method, PropertyVetoException.class)) {
			        pd.setConstrained(true);			
			    }
			}
		    }
		} catch (IntrospectionException ex) {
		    // 如果 PropertyDescriptor 或 IndexedPropertyDescriptor 构造函数发现该方法违反了设计模式的细节，
			// 例如通过使用空名称或返回 void 的 getter 或其他方式。
		    pd = null;
		}

		if (pd != null) {
		    // 如果此类或其基类之一是 PropertyChange 源，那么我们假设我们发现的任何属性都是“绑定的”。
		    if (propertyChangeSource) {
			pd.setBound(true);
		    }
		    addProperty(pd);
		}
	    }
	}

	// Allocate and populate the result array.
	PropertyDescriptor result[] = new PropertyDescriptor[properties.size()];
	java.util.Enumeration elements = properties.elements();
	for (int i = 0; i < result.length; i++) {
	    result[i] = (PropertyDescriptor)elements.nextElement();
	    if (defaultPropertyName != null
			 && defaultPropertyName.equals(result[i].getName())) {
		defaultPropertyIndex = i;
	    }
	}

	return result;
    }

    void addProperty(PropertyDescriptor pd) {
	String name = pd.getName();
	PropertyDescriptor old = (PropertyDescriptor)properties.get(name);
	if (old == null) {
	    properties.put(name, pd);
	    return;
	}
	// If the property type has changed, use the new descriptor.
	Class opd = old.getPropertyType();
	Class npd = pd.getPropertyType();
	if (opd != null && npd != null && opd != npd) {
	    properties.put(name, pd);
	    return;
	}

	PropertyDescriptor composite;
	if (old instanceof IndexedPropertyDescriptor ||
				pd instanceof IndexedPropertyDescriptor) {
	    composite = new IndexedPropertyDescriptor(old, pd);
	} else {
	    composite = new PropertyDescriptor(old, pd);
	}
	properties.put(name, composite);
    }


    /**
     * @return EventSetDescriptor 数组，描述目标 bean 触发的事件类型。
     */
    private EventSetDescriptor[] getTargetEventInfo() throws IntrospectionException {

	// 检查 bean 是否有自己的 BeanInfo 将提供显式信息。
        EventSetDescriptor[] explicit = null;
	if (informant != null) {
	    explicit = informant.getEventSetDescriptors();
	    int ix = informant.getDefaultEventIndex();
	    if (ix >= 0 && ix < explicit.length) {
		defaultEventName = explicit[ix].getName();
	    }
	}

	if (explicit == null && superBeanInfo != null) {
	    // We have no explicit BeanInfo events.  Check with our parent.
	    EventSetDescriptor supers[] = superBeanInfo.getEventSetDescriptors();
	    for (int i = 0 ; i < supers.length; i++) {
		addEvent(supers[i]);
	    }
	    int ix = superBeanInfo.getDefaultEventIndex();
	    if (ix >= 0 && ix < supers.length) {
		defaultEventName = supers[ix].getName();
	    }
	}

	for (int i = 0; i < additionalBeanInfo.length; i++) {
	    EventSetDescriptor additional[] = additionalBeanInfo[i].getEventSetDescriptors();
	    if (additional != null) {
	        for (int j = 0 ; j < additional.length; j++) {
		    addEvent(additional[j]);
	        }
	    }
	}

	if (explicit != null) {
	    // Add the explicit informant data to our results.
	    for (int i = 0 ; i < explicit.length; i++) {
		addEvent(explicit[i]);
	    }

	} else {

	    // Apply some reflection to the current class.

	    // Get an array of all the beans methods at this level
	    Method methodList[] = getDeclaredMethods(beanClass);

	    // Find all suitable "add" and "remove" methods.
	    java.util.Hashtable adds = new java.util.Hashtable();
	    java.util.Hashtable removes = new java.util.Hashtable();
	    for (int i = 0; i < methodList.length; i++) {
	        Method method = methodList[i];
	        // skip static and non-public methods.
		int mods = method.getModifiers();
		if (Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
		    continue;
		}
	        String name = method.getName();

	        Class argTypes[] = method.getParameterTypes();
	        Class resultType = method.getReturnType();

	        if (name.startsWith("add") && argTypes.length == 1 &&
			    	resultType == Void.TYPE) {
		    String compound = name.substring(3) + ":" + argTypes[0];
		    adds.put(compound, method);
	        } else if (name.startsWith("remove") && argTypes.length == 1 &&
			    	resultType == Void.TYPE) {
		    String compound = name.substring(6) + ":" + argTypes[0];
		    removes.put(compound, method);
	        }
	    }

   	    // Now look for matching addFooListener+removeFooListener pairs.
  	    java.util.Enumeration keys = adds.keys();
	    String beanClassName = beanClass.getName();
	    while (keys.hasMoreElements()) {
	        String compound = (String) keys.nextElement();
	        // Skip any "add" which doesn't have a matching "remove".
	        if (removes.get(compound) == null) {
		    continue;
	        } 
	        // Method name has to end in "Listener"
	        if (compound.indexOf("Listener:") <= 0) {
		    continue;
	        }

	        String listenerName = compound.substring(0, compound.indexOf(':'));
	        String eventName = decapitalize(listenerName.substring(0, listenerName.length()-8));
	        Method addMethod = (Method)adds.get(compound);
	        Method removeMethod = (Method)removes.get(compound);
	        Class argType = addMethod.getParameterTypes()[0];

	        // Check if the argument type is a subtype of EventListener
	        if (!Introspector.isSubclass(argType, eventListenerType)) {
	            continue;
	        }

                // generate a list of Method objects for each of the target methods:
	        Method allMethods[] = argType.getMethods();
	        int count = 0;
	        for (int i = 0; i < allMethods.length; i++) {
	            if (isEventHandler(allMethods[i])) {
		        count++;
	            } else {
		        allMethods[i] = null;
	            }
	        }
	        Method methods[] = new Method[count];
	        int j = 0;
	        for (int i = 0; i < allMethods.length; i++) {
	            if (allMethods[i] != null) {
		        methods[j++] = allMethods[i];
	            }
 	        }

  	        EventSetDescriptor esd = new EventSetDescriptor(eventName, argType,
						methods, addMethod, removeMethod);

		// 如果 adder 方法抛出 TooManyListenersException 那么它是一个单播事件源。
		if (throwsException(addMethod,
			java.util.TooManyListenersException.class)) {
		    esd.setUnicast(true);
		}

		addEvent(esd);
	    }
	}

	// Allocate and populate the result array.
	EventSetDescriptor result[] = new EventSetDescriptor[events.size()];
	java.util.Enumeration elements = events.elements();
	for (int i = 0; i < result.length; i++) {
	    result[i] = (EventSetDescriptor)elements.nextElement();
	    if (defaultEventName != null 
			    && defaultEventName.equals(result[i].getName())) {
		defaultEventIndex = i;
	    }
	}

	return result;
    }

    void addEvent(EventSetDescriptor esd) {
	String key = esd.getName();
	if (key.equals("propertyChange")) {
	    propertyChangeSource = true;
	}
	EventSetDescriptor old = (EventSetDescriptor)events.get(key);
	if (old == null) {
	    events.put(key, esd);
	    return;
	}
	EventSetDescriptor composite = new EventSetDescriptor(old, esd);
	events.put(key, composite);
    }

    /**
     * @return 描述目标 bean 支持的私有方法的 MethodDescriptor 数组。
     */
    private MethodDescriptor[] getTargetMethodInfo() throws IntrospectionException {

	// Check if the bean has its own BeanInfo that will provide
	// explicit information.
        MethodDescriptor[] explicit = null;
	if (informant != null) {
	    explicit = informant.getMethodDescriptors();
	}

	if (explicit == null && superBeanInfo != null) {
	    // We have no explicit BeanInfo methods.  Check with our parent.
	    MethodDescriptor supers[] = superBeanInfo.getMethodDescriptors();
	    for (int i = 0 ; i < supers.length; i++) {
		addMethod(supers[i]);
	    }
	}

	for (int i = 0; i < additionalBeanInfo.length; i++) {
	    MethodDescriptor additional[] = additionalBeanInfo[i].getMethodDescriptors();
	    if (additional != null) {
	        for (int j = 0 ; j < additional.length; j++) {
		    addMethod(additional[j]);
	        }
	    }
	}

	if (explicit != null) {
	    // Add the explicit informant data to our results.
	    for (int i = 0 ; i < explicit.length; i++) {
		addMethod(explicit[i]);
	    }

	} else {

	    // Apply some reflection to the current class.

	    // First get an array of all the beans methods at this level
	    Method methodList[] = getDeclaredMethods(beanClass);

	    // Now analyze each method.
	    for (int i = 0; i < methodList.length; i++) {
	        Method method = methodList[i];
	        // skip non-public methods.
		if (!Modifier.isPublic(method.getModifiers())) {
		    continue;
		}
		MethodDescriptor md = new MethodDescriptor(method);
		addMethod(md);
	    }
	}

	// Allocate and populate the result array.
	MethodDescriptor result[] = new MethodDescriptor[methods.size()];
	java.util.Enumeration elements = methods.elements();
	for (int i = 0; i < result.length; i++) {
	    result[i] = (MethodDescriptor)elements.nextElement();
	}

	return result;
    }

    private void addMethod(MethodDescriptor md) {
	// We have to be careful here to distinguish method by both name
	// and argument lists.
	// This method gets called a *lot, so we try to be efficient.

	String name = md.getMethod().getName();

	MethodDescriptor old = (MethodDescriptor)methods.get(name);
	if (old == null) {
	    // This is the common case.
	    methods.put(name, md);
	    return;
	}	

	// We have a collision on method names.  This is rare.

	// Check if old and md have the same type.
	Class p1[] = md.getMethod().getParameterTypes();	
	Class p2[] = old.getMethod().getParameterTypes();	
	boolean match = false;
	if (p1.length == p2.length) {
	    match = true;
	    for (int i = 0; i < p1.length; i++) {
		if (p1[i] != p2[i]) {
		    match = false;
		    break;
		}
	    }
	}
	if (match) {
	    MethodDescriptor composite = new MethodDescriptor(old, md);
	    methods.put(name, composite);
	    return;
	}

	// We have a collision on method names with different type signatures.
	// This is very rare.

	String longKey = makeQualifiedMethodName(md);
	old = (MethodDescriptor)methods.get(longKey);
	if (old == null) {
	    methods.put(longKey, md);
	    return;
	}	
	MethodDescriptor composite = new MethodDescriptor(old, md);
	methods.put(longKey, composite);
    }

    private String makeQualifiedMethodName(MethodDescriptor md) {
	Method m = md.getMethod();
	StringBuffer sb = new StringBuffer();
	sb.append(m.getName());
	sb.append("=");
	Class params[] = m.getParameterTypes();
	for (int i = 0; i < params.length; i++) {
	    sb.append(":");
	    sb.append(params[i].getName());
	}
	return sb.toString();
    }

    private int getTargetDefaultEventIndex() {
	return defaultEventIndex;
    }

    private int getTargetDefaultPropertyIndex() {
	return defaultPropertyIndex;
    }

    private BeanDescriptor getTargetBeanDescriptor() throws IntrospectionException {
	// Use explicit info, if available,
	if (informant != null) {
	    BeanDescriptor bd = informant.getBeanDescriptor();
	    if (bd != null) {
		return (bd);
	    }
	}
	// OK, fabricate a default BeanDescriptor.
	return (new BeanDescriptor(beanClass));
    }

    private boolean isEventHandler(Method m) throws IntrospectionException {
	// Right now we assume that a method is an event handler if it
	// has a single argument, whose type name includes the word
	// "Event".  The real answer is that the argument type should
	// inherit from java.util.Event or somesuch, but we're not quite
	// there yet.
	try {
	    Class argTypes[] = m.getParameterTypes();
	    if (argTypes.length != 1) {
		return (false);
	    }
	    String type = "" + argTypes[0];
	    if (type.indexOf("Event") >= 0) {
		return (true);
	    } else {
		return (false);
	    }
	    
	} catch (Exception ex) {
	    throw new IntrospectionException("Unexpected reflection exception: " + ex);
	}
    }

    private static synchronized Method[] getDeclaredMethods(Class clz) {
	// Looking up Class.getDeclaredMethods is realtively expensive,
	// so we cache the results.
	if (declaredMethodCache == null) {
	   declaredMethodCache = new java.util.Hashtable();
	}
	Method[] result = (Method[])declaredMethodCache.get(clz);
	if (result == null) {
	    result = clz.getDeclaredMethods();
	    declaredMethodCache.put(clz, result);
	}
	return result;
    }

    //======================================================================
    // Package private support methods.
    //======================================================================

    /**
     * Find a target methodName on a given class.
     */

    static Method findMethod(Class cls, String methodName, int argCount) 
			throws IntrospectionException {
	if (methodName == null) {
	    return null;
	}

	// For overriden methods we need to find the most derived version.
	// So we start with the given cls and walk up the superclass chain.
	while (cls != null) {
            Method methods[] = getDeclaredMethods(cls);
	    for (int i = 0; i < methods.length; i++) {
	        Method method = methods[i];
	        // skip static and non-public methods.
		int mods = method.getModifiers();
		if (Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
		    continue;
		}
	        if (method.getName().equals(methodName) &&
			method.getParameterTypes().length == argCount) {
	            return method;
 	        }
	    }
	    cls = cls.getSuperclass();
	}

	// We failed to find a suitable method
	throw new IntrospectionException("No method \"" + methodName + 
					"\" with " + argCount + " arg(s)");
    }

    /**
     * Return true if class a is either equivalent to class b, or
     * if class a is a subclass of class b.
     * Note tht either or both "Class" objects may represent interfaces.
     */
    static  boolean isSubclass(Class a, Class b) {
	// We rely on the fact that for any given java class or
        // primtitive type there is a unqiue Class object, so
	// we can use object equivalence in the comparisons.
	if (a == b) {
	    return true;
	}
	if (a == null || b == null) {
	    return false;
	}
	for (Class x = a; x != null; x = x.getSuperclass()) {
	    if (x == b) {	
		return true;
	    }
	    if (b.isInterface()) {
		Class interfaces[] = x.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
		    if (isSubclass(interfaces[i], b)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    /**
     * Return true iff the given method throws the given exception.
     */
    private boolean throwsException(Method method, Class exception) {
	Class exs[] = method.getExceptionTypes();
	for (int i = 0; i < exs.length; i++) {
	    if (exs[i] == exception) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Try to create an instance of a named class.
     * First try the classloader of "sibling", then try the system
     * classloader.
     */

    static Object instantiate(Class sibling, String className)
		 throws InstantiationException, IllegalAccessException,
						ClassNotFoundException {
	// First check with sibling's classloader (if any). 
	ClassLoader cl = sibling.getClassLoader();
	if (cl != null) {
	    try {
	        Class cls = cl.loadClass(className);
		return cls.newInstance();
	    } catch (Exception ex) {
	        // Just drop through and try the system classloader.
	    }
        }
	// Now try the system classloader.
	Class cls = Class.forName(className);
	return cls.newInstance();
    }

    //======================================================================

    private BeanInfo informant;
    private boolean propertyChangeSource = false;
    private Class beanClass;
    private BeanInfo superBeanInfo;
    private BeanInfo additionalBeanInfo[];
    private static java.util.Hashtable beanInfoCache = new java.util.Hashtable();
    private static Class eventListenerType = java.util.EventListener.class;
    private String defaultEventName;
    private String defaultPropertyName;
    private int defaultEventIndex = -1;
    private int defaultPropertyIndex = -1;

    // Methods maps from Method objects to MethodDescriptors
    private java.util.Hashtable methods = new java.util.Hashtable();

    // Cache of Class.getDeclaredMethods:
    private static java.util.Hashtable declaredMethodCache;

    // properties maps from String names to PropertyDescriptors
    private java.util.Hashtable properties = new java.util.Hashtable();

    // events maps from String names to EventSetDescriptors
    private java.util.Hashtable events = new java.util.Hashtable();

    private static String[] searchPath = { "sun.beans.infos" };

}

//===========================================================================

/**
 * Package private implementation support class for Introspector's
 * internal use.
 */

class GenericBeanInfo extends SimpleBeanInfo {

    public GenericBeanInfo(BeanDescriptor beanDescriptor,
		EventSetDescriptor[] events, int defaultEvent,
		PropertyDescriptor[] properties, int defaultProperty,
		MethodDescriptor[] methods, BeanInfo targetBeanInfo) {
	this.beanDescriptor = beanDescriptor;
	this.events = events;
	this.defaultEvent = defaultEvent;
	this.properties = properties;
	this.defaultProperty = defaultProperty;
	this.methods = methods;
	this.targetBeanInfo = targetBeanInfo;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
	return properties;
    }

    public int getDefaultPropertyIndex() {
	return defaultProperty;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
	return events;
    }

    public int getDefaultEventIndex() {
	return defaultEvent;
    }

    public MethodDescriptor[] getMethodDescriptors() {
	return methods;
    }

    public BeanDescriptor getBeanDescriptor() {
	return beanDescriptor;
    }

    public java.awt.Image getIcon(int iconKind) {
	if (targetBeanInfo != null) {
	    return targetBeanInfo.getIcon(iconKind);
	}
	return super.getIcon(iconKind);
    }

    private BeanDescriptor beanDescriptor;
    private EventSetDescriptor[] events;
    private int defaultEvent;
    private PropertyDescriptor[] properties;
    private int defaultProperty;
    private MethodDescriptor[] methods;
    private BeanInfo targetBeanInfo;
}
