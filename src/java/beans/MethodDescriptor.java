/*
 * @(#)MethodDescriptor.java	1.18 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.lang.reflect.*;

/**
 * MethodDescriptor 描述了 Java Bean 支持从其他组件进行外部访问的特定方法。
 */

public class MethodDescriptor extends FeatureDescriptor {

    /**
     * @param method    The low-level method information.
     */
    public MethodDescriptor(Method method) {
	this.method = method;
	setName(method.getName());
    }


    /**
     * @param method    The low-level method information.
     * @param parameterDescriptors  每个方法参数的描述信息。
     */
    public MethodDescriptor(Method method, 
		ParameterDescriptor parameterDescriptors[]) {
	this.method = method;
	this.parameterDescriptors = parameterDescriptors;
	setName(method.getName());
    }

    /**
     * @return The low-level description of the method
     */
    public Method getMethod() {
	return method;
    }


    /**
     * @return 参数的与语言环境无关的名称。如果参数名称未知，则可能返回空数组。
     */
    public ParameterDescriptor[] getParameterDescriptors() {
	return parameterDescriptors;
    }

    /*
     * 包私有构造函数合并两个方法描述符。如果它们发生冲突，则将第二个参数 (y) 优先于第一个参数 (x)。
     * @param x 第一个（低优先级）MethodDescriptor @param y 第二个（高优先级）MethodDescriptor
     */

    MethodDescriptor(MethodDescriptor x, MethodDescriptor y) {
	super(x,y);
	method = x.method;
	parameterDescriptors = x.parameterDescriptors;
	if (y.parameterDescriptors != null) {
	    parameterDescriptors = y.parameterDescriptors;
	}
    }

    private Method method;
    private ParameterDescriptor parameterDescriptors[];
}
