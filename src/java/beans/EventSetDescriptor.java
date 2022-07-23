/*
 * @(#)EventSetDescriptor.java	1.41 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.lang.reflect.*;

/**
 * EventSetDescriptor 描述给定 Java bean 触发的一组事件。
 *
 * 给定的事件组都作为单个事件侦听器接口上的方法调用传递，并且可以通过对事件源提供的注册方法的调用来注册事件侦听器对象。
 */


public class EventSetDescriptor extends FeatureDescriptor {

    /**
     * 此构造函数创建一个 EventSetDescriptor 假设您遵循最简单的标准设计模式，
	 * 其中命名事件“fred”
	 * （1）作为对接口 FredListener 的单个方法的调用传递，
	 * （2）有一个 FredEvent 类型的参数，
	 * (3)其中FredListener可以通过对源组件的addFredListener方法的调用来注册并且通过对removeFredListener方法的调用来移除。
     *
     * @param sourceClass  The class firing the event.
     * @param eventSetName  事件的程序名称。例如。 “弗雷德”。请注意，这通常应以小写字符开头。
     * @param listenerType  事件将被传递到的目标接口。
     * @param listenerMethodName  当事件被传递到其目标侦听器接口时将调用的方法。
     * @exception IntrospectionException 如果自省期间发生异常。
     */
    public EventSetDescriptor(Class sourceClass, String eventSetName,
		Class listenerType, String listenerMethodName) 
		throws IntrospectionException {

   	setName(eventSetName);

	// Get a Class object for the listener class.
    	this.listenerType = listenerType;
	
	listenerMethods = new Method[1];
	listenerMethods[0] = Introspector.findMethod(listenerType,
						listenerMethodName, 1);

	String listenerClassName = listenerType.getName();
	String tail = listenerClassName.substring(listenerClassName.lastIndexOf('.') + 1);

	String addMethodName = "add" + tail;
	addMethod = Introspector.findMethod(sourceClass, addMethodName, 1);

	String removeMethodName = "remove" + tail;
	removeMethod = Introspector.findMethod(sourceClass, removeMethodName, 1);
					

    }

    /**
     * 此构造函数使用字符串名称从头开始创建 EventSetDescriptor。
     *
     * @param sourceClass  The class firing the event.
     * @param eventSetName 事件集的编程名称。请注意，这通常应以小写字符开头。
     * @param listenerType  事件将被传递到的目标接口的类。
     * @param listenerMethodNames 当事件被传递到其目标侦听器接口时将被调用的方法的名称。
     * @param addListenerMethodName  事件源上可用于注册事件侦听器对象的方法的名称。
     * @param removeListenerMethodName  事件源上可用于注销事件侦听器对象的方法的名称。
     * @exception IntrospectionException 如果自省期间发生异常。
     */
    public EventSetDescriptor(Class sourceClass,
		String eventSetName, 
		Class listenerType,
		String listenerMethodNames[],
		String addListenerMethodName,
		String removeListenerMethodName)
		throws IntrospectionException {
	setName(eventSetName);
	listenerMethods = new Method[listenerMethodNames.length];
	for (int i = 0; i < listenerMethods.length; i++) {
	    listenerMethods[i] = Introspector.findMethod(listenerType,
					listenerMethodNames[i], 1);
	}

	this.addMethod = Introspector.findMethod(sourceClass,
					addListenerMethodName, 1);
	this.removeMethod = Introspector.findMethod(sourceClass,
					removeListenerMethodName, 1);

	this.listenerType = listenerType;
    }

    /**
     * 此构造函数使用 java.lang.reflect.Method 和 java.lang.Class 对象从头开始创建 EventSetDescriptor。
     *
     * @param eventSetName The programmatic name of the event set.
     * @param listenerType The Class for the listener interface.
     * @param listenerMethods  描述目标侦听器中每个事件处理方法的 Method 对象数组。
     * @param addListenerMethod  事件源上可用于注册事件侦听器对象的方法。
     * @param removeListenerMethod  事件源上可用于注销事件侦听器对象的方法。
     * @exception IntrospectionException 如果自省期间发生异常。
     */
    public EventSetDescriptor(String eventSetName, 
		Class listenerType,
		Method listenerMethods[],
		Method addListenerMethod,
		Method removeListenerMethod) 
		throws IntrospectionException {
	setName(eventSetName);
	this.listenerMethods = listenerMethods;
	this.addMethod = addListenerMethod;
	this.removeMethod = removeListenerMethod;
	this.listenerType = listenerType;
    }

    /**
     * 此构造函数使用 java.lang.reflect.MethodDescriptor 和 java.lang.Class 对象从头开始创建 EventSetDescriptor。
     *
     * @param eventSetName The programmatic name of the event set.
     * @param listenerType The Class for the listener interface.
     * @param listenerMethodDescriptors  An array of MethodDescriptor objects
     *		 describing each of the event handling methods in the
     *           target listener.
     * @param addListenerMethod  The method on the event source
     *		that can be used to register an event listener object.
     * @param removeListenerMethod  The method on the event source
     *		that can be used to de-register an event listener object.
     * @exception IntrospectionException if an exception occurs during
     *              introspection.
     */
    public EventSetDescriptor(String eventSetName, 
		Class listenerType,
		MethodDescriptor listenerMethodDescriptors[],
		Method addListenerMethod,
		Method removeListenerMethod) 
		throws IntrospectionException {
	setName(eventSetName);
	this.listenerMethodDescriptors = listenerMethodDescriptors;
	this.addMethod = addListenerMethod;
	this.removeMethod = removeListenerMethod;
	this.listenerType = listenerType;
    }

    /** 
     * @return The Class object for the target interface that will
     * get invoked when the event is fired.
     */
    public Class getListenerType() {
	return listenerType;
    }

    /** 
     * @return An array of Method objects for the target methods
     * within the target listener interface that will get called when
     * events are fired.
     */
    public Method[] getListenerMethods() {
	if (listenerMethods == null && listenerMethodDescriptors != null) {
	    // Create Method array from MethodDescriptor array.
	    listenerMethods = new Method[listenerMethodDescriptors.length];
	    for (int i = 0; i < listenerMethods.length; i++) {
		listenerMethods[i] = listenerMethodDescriptors[i].getMethod();
	    }
	}
	return listenerMethods;
    }


    /** 
     * @return An array of MethodDescriptor objects for the target methods
     * within the target listener interface that will get called when
     * events are fired.
     */
    public MethodDescriptor[] getListenerMethodDescriptors() {
	if (listenerMethodDescriptors == null && listenerMethods != null) {
	    // Create MethodDescriptor array from Method array.
	    listenerMethodDescriptors = 
				new MethodDescriptor[listenerMethods.length];
	    for (int i = 0; i < listenerMethods.length; i++) {
		listenerMethodDescriptors[i] = 
				new MethodDescriptor(listenerMethods[i]);
	    }
	}
	return listenerMethodDescriptors;
    }

    /** 
     * @return The method used to register a listener at the event source.
     */
    public Method getAddListenerMethod() {
	return addMethod;
    }

    /** 
     * @return The method used to register a listener at the event source.
     */
    public Method getRemoveListenerMethod() {
	return removeMethod;
    }

    /**
     * Mark an event set as unicast (or not).
     *
     * @param unicast  True if the event set is unicast.
     */

    public void setUnicast(boolean unicast) {
	this.unicast = unicast;
    }
    
    /**
     * Normally event sources are multicast.  However there are some 
     * exceptions that are strictly unicast.
     *
     * @return  True if the event set is unicast.  Defaults to "false".
     */

    public boolean isUnicast() {
	return unicast;
    }

    /**
     * Mark an event set as being in the "default" set (or not).
     * By default this is true.
     *
     * @param unicast  True if the event set is unicast.
     */

    public void setInDefaultEventSet(boolean inDefaultEventSet) {
	this.inDefaultEventSet = inDefaultEventSet;
    }
    
    /**
     * Report if an event set is in the "default set".
     *
     * @return  True if the event set is in the "default set".  Defaults to "true".
     */

    public boolean isInDefaultEventSet() {
	return inDefaultEventSet;
    }

    /*
     * Package-private constructor
     * Merge two event set descriptors.  Where they conflict, give the
     * second argument (y) priority over the first argument (x).
     * @param x  The first (lower priority) EventSetDescriptor
     * @param y  The second (higher priority) EventSetDescriptor
     */

    EventSetDescriptor(EventSetDescriptor x, EventSetDescriptor y) {
	super(x,y);
	listenerMethodDescriptors = x.listenerMethodDescriptors;
	if (y.listenerMethodDescriptors != null) {
	    listenerMethodDescriptors = y.listenerMethodDescriptors;
	}
	if (listenerMethodDescriptors == null) {
	    listenerMethods = y.listenerMethods;
	}
	addMethod = y.addMethod;
	removeMethod = y.removeMethod;
	unicast = y.unicast;
	listenerType = y.listenerType;
	if (!x.inDefaultEventSet || !y.inDefaultEventSet) {
	    inDefaultEventSet = false;
	}
    }

    private Class listenerType;
    private Method[] listenerMethods;
    private MethodDescriptor[] listenerMethodDescriptors;
    private Method addMethod;
    private Method removeMethod;
    private boolean unicast;
    private boolean inDefaultEventSet = true;
}
