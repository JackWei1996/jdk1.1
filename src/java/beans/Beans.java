/*
 * @(#)Beans.java	1.32 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.URL;
import java.lang.reflect.Array;

/**
 * 此类提供了一些通用的 bean 控制方法。
 */

public class Beans {

    /**
     * Instantiate a bean.
     *
	 * bean 是基于相对于类加载器的名称创建的。此名称应该是一个点分隔的名称，例如“a.b.c”。
     *
     * 在 Beans 1.0 中，给定名称可以指示序列化对象或类。将来可能会添加其他机制。
	 * 在 beans 1.0 中，我们首先尝试将 beanName 视为序列化对象名称，然后将其视为类名称。
     *
     * 当使用 beanName 作为序列化对象名称时，我们将给定的 beanName 转换为资源路径名并添加尾随“.ser”后缀。
	 * 然后我们尝试从该资源加载一个序列化对象。
     *
     * 例如，给定一个“x.y”的 beanName，Beans.instantiate 将首先尝试从资源“xy.ser”读取序列化对象，
	 * 如果失败，它将尝试加载类“x.y”并创建该类的实例班级。
     *
     * 如果 bean 是 java.applet.Applet 的子类型，那么它会被赋予一些特殊的初始化。
	 * 首先，它提供了一个默认的 AppletStub 和 AppletContext。
	 * 其次，如果它是从类名中实例化的，则调用 applet 的“init”方法。 （如果 bean 被反序列化，则跳过此步骤。）
     *
     * 请注意，对于作为 applet 的 bean，调用者有责任在 applet 上调用“start”。
	 * 为了获得正确的行为，这应该在将小程序添加到可见的 AWT 容器后完成。
     *
     * 请注意，通过 beans.instantiate 创建的小程序运行在与在浏览器中运行的小程序略有不同的环境中。
	 * 特别是，bean applet 无法访问“参数”，因此它们可能希望提供属性 getset 方法来设置参数值。
	 * 我们建议 bean-applet 开发人员针对 JDK appletviewer（用于参考浏览器环境）
	 * 和 BDK BeanBox（用于参考 bean 容器）测试他们的 bean-applet。
     * 
     * @param     classLoader 我们应该从中创建 bean 的类加载器。如果为空，则使用系统类加载器。
     * @param     beanName    the name of the bean within the class-loader.
     *   	              For example "sun.beanbox.foobah"
     * @exception ClassNotFoundException if the class of a serialized
     *              object could not be found.
     * @exception IOException if an I/O error occurs.
     */
    public static Object instantiate(ClassLoader cls, String beanName) 
			throws IOException, ClassNotFoundException {

	InputStream ins;
	ObjectInputStream oins = null;
	Object result = null;
	boolean serialized = false;

	// Try to find a serialized object with this name
	String serName = beanName.replace('.','/').concat(".ser");
	if (cls == null) {
	    ins = ClassLoader.getSystemResourceAsStream(serName);
	} else {
	    ins  = cls.getResourceAsStream(serName);
	}
	if (ins != null) {
	    try {
	        if (cls == null) {
		    oins = new ObjectInputStream(ins);
	        } else {
		    oins = new ObjectInputStreamWithLoader(ins, cls);
	        }
	        result = oins.readObject();
		serialized = true;
	        oins.close();
	    } catch (IOException ex) {
		ins.close();
		// For now, drop through and try opening the class.
		// throw ex;
	    } catch (ClassNotFoundException ex) {
		ins.close();
		throw ex;
	    }
	}

	if (result == null) {
	    // No serialized object, try just instantiating the class
	    Class cl;
	    if (cls == null) {
	        cl = Class.forName(beanName);
	    } else {
	        cl = cls.loadClass(beanName);
	    }
	    try {
	    	result = cl.newInstance();
	    } catch (Exception ex) {
	        throw new ClassNotFoundException();
	    }
	}
	// Ok, if the result is an applet initialize it.
	if (result != null && result instanceof Applet) {
	    Applet applet = (Applet) result;

	    // 图我们的代码库和文档库 URL。我们通过定位已知资源的 URL，然后按摩 URL 来做到这一点。

	    // 首先找到bean本身对应的“资源名”。因此，序列化的 bean“a.b.c”将暗示资源名称“abc.ser”，而类名“x.y”将暗示资源名称“xy.class”。

	    String resourceName;
	    if (serialized) {
		// Serialized bean
		resourceName = beanName.replace('.','/').concat(".ser");
	    } else {
		// Regular class
		resourceName = beanName.replace('.','/').concat(".class");
	    }
	    URL objectUrl = null;
	    URL codeBase = null;
	    URL docBase = null;

	    // Now get the URL correponding to the resource name.
	    if (cls == null) {
		objectUrl = ClassLoader.getSystemResource(resourceName);
	    } else {
		objectUrl = cls.getResource(resourceName);
	    }

	    // 如果我们找到一个 URL，我们会尝试通过获取最终路径名组件来定位文档库，并通过获取完整的资源名称来定位代码库。
	    // So if we had a resourceName of "a/b/c.class" and we got an
	    // objectURL of "file://bert/classes/a/b/c.class" then we would
	    // want to set the codebase to "file://bert/classes/" and the
	    // docbase to "file://bert/classes/a/b/"

	    if (objectUrl != null) {
		String s = objectUrl.toExternalForm();
		if (s.endsWith(resourceName)) {
  		    int ix = s.length() - resourceName.length();
		    codeBase = new URL(s.substring(0,ix));
		    docBase = codeBase;
		    ix = s.lastIndexOf('/');
		    if (ix >= 0) {
		        docBase = new URL(s.substring(0,ix+1));
		    }
		}
	    }
	    	    
	    // Setup a default context and stub.
	    BeansAppletContext context = new BeansAppletContext(applet);
	    BeansAppletStub stub = new BeansAppletStub(applet, context, codeBase, docBase);
	    applet.setStub(stub);

	    // 如果它被反序列化，那么它已经被初始化了。否则我们需要初始化它。
	    if (!serialized) {
		// 我们需要设置一个合理的初始大小，因为许多小程序在没有明确调整大小的情况下启动时会不高兴。
		applet.setSize(100,100);
		applet.init();
	    }
	    stub.active = true;
	}
	return result;
    }


    /**
     * 从给定的 bean 中，获取表示该源对象的指定类型视图的对象。
     *
     * 结果可能是相同的对象或不同的对象。如果请求的目标视图不可用，则返回给定的 bean。
     *
     * 此方法在 Beans 1.0 中作为钩子提供，以允许将来添加更灵活的 bean 行为。
     *
     * @param obj  Object from which we want to obtain a view.
     * @param targetType  The type of view we'd like to get.
     *
     */
    public static Object getInstanceOf(Object bean, Class targetType) {
    	return bean;
    }
 
    /**
     * 检查是否可以将 bean 视为给定的目标类型。如果可以在给定 bean 上使用
	 * Beans.getInstanceof 方法来获取表示指定 targetType 类型视图的对象，则结果将为 true。
     *
     * @param bean  Bean from which we want to obtain a view.
     * @param targetType  The type of view we'd like to get.
     * @return "true" if the given bean supports the given targetType.
     */
    public static boolean isInstanceOf(Object bean, Class targetType) {
	return Introspector.isSubclass(bean.getClass(), targetType);
    }


    /**
     * Test if we are in design-mode.
     *
     * @return  True if we are running in an application construction
     *		environment.
     */
    public static boolean isDesignTime() {
	return designTime;
    }

    /**
     * @return  如果我们在 bean 可以假定交互式 GUI 可用的环境中运行，则为 true，因此它们可以弹出对话框等。
	 * 这通常在窗口环境中返回 true，并且通常在服务器环境中返回 false，或者如果应用程序作为批处理作业的一部分运行。
     */
    public static boolean isGuiAvailable() {
	return guiAvailable;
    }

    /**
     * 用于指示我们是否在应用程序构建器环境中运行。请注意，此方法经过安全检查，不适用于（例如）不受信任的小程序。
     *
     * @param isDesignTime  True if we're in an application builder tool.
     */

    public static void setDesignTime(boolean isDesignTime)
			throws SecurityException {
	designTime = isDesignTime;
    }

    /**
     * Used to indicate whether of not we are running in an environment
     * where GUI interaction is available.  Note that this method is 
     * security checked and is not available to (for example) untrusted
     * applets.
     *
     * @param isGuiAvailable  True if GUI interaction is available.
     */

    public static void setGuiAvailable(boolean isGuiAvailable)
			throws SecurityException {
	guiAvailable = isGuiAvailable;
    }


    private static boolean designTime;
    private static boolean guiAvailable = true;
}

/**
 * This subclass of ObjectInputStream delegates loading of classes to
 * an existing ClassLoader.
 */

class ObjectInputStreamWithLoader extends ObjectInputStream
{
    private ClassLoader loader;

    /**
     * Loader must be non-null;
     */

    public ObjectInputStreamWithLoader(InputStream in, ClassLoader loader)
	    throws IOException, StreamCorruptedException {

	super(in);
	if (loader == null) {
            throw new IllegalArgumentException("Illegal null argument to ObjectInputStreamWithLoader");
	}
	this.loader = loader;
    }

    /**
     * Make a primitive array class
     */

    private Class primitiveType(char type) {
	switch (type) {
	case 'B': return byte.class;
        case 'C': return char.class;
	case 'D': return double.class;
	case 'F': return float.class;
	case 'I': return int.class;
	case 'J': return long.class;
	case 'S': return short.class;
	case 'Z': return boolean.class;
	default: return null;
	}
    }

    /**
     * Use the given ClassLoader rather than using the system class
     */
    protected Class resolveClass(ObjectStreamClass classDesc)
	throws IOException, ClassNotFoundException {

	String cname = classDesc.getName();
	if (cname.startsWith("[")) {
	    // An array
	    Class component;		// component class
	    int dcount;			// dimension
	    for (dcount=1; cname.charAt(dcount)=='['; dcount++) ;
	    if (cname.charAt(dcount) == 'L') {
		component = loader.loadClass(cname.substring(dcount+1,
							     cname.length()-1));
	    } else {
		if (cname.length() != dcount+1) {
		    throw new ClassNotFoundException(cname);// malformed
		}
		component = primitiveType(cname.charAt(dcount));
	    }
	    int dim[] = new int[dcount];
	    for (int i=0; i<dcount; i++) {
		dim[i]=0;
	    }
	    return Array.newInstance(component, dim).getClass();
	} else {
	    return loader.loadClass(cname);
	}
    }
}

/**
 * Package private support class.  This provides a default AppletContext
 * for beans which are applets.
 */

class BeansAppletContext implements AppletContext {
    Applet target;
    java.util.Hashtable imageCache = new java.util.Hashtable();

    BeansAppletContext(Applet target) {
        this.target = target;
    }

    public AudioClip getAudioClip(URL url) {
	// We don't currently support audio clips in the Beans.instantiate
	// applet context, unless by some luck there exists a URL content
	// class that can generate an AudioClip from the audio URL.
	try {
	    return (AudioClip) url.getContent();
  	} catch (Exception ex) {
	    return null;
	}
    }

    public synchronized Image getImage(URL url) {
	Object o = imageCache.get(url);
	if (o != null) {
	    return (Image)o;
	}
	try {
	    o = url.getContent();
	    if (o == null) {
		return null;
	    }
	    if (o instanceof Image) {
		imageCache.put(url, o);
		return (Image) o;
	    }
	    // Otherwise it must be an ImageProducer.
	    Image img = target.createImage((java.awt.image.ImageProducer)o);
	    imageCache.put(url, img);
	    return img;

  	} catch (Exception ex) {
	    return null;
	}
    }

    public Applet getApplet(String name) {
	return null;
    }

    public java.util.Enumeration getApplets() {
	java.util.Vector applets = new java.util.Vector();
	applets.addElement(target);
	return applets.elements();	
    }

    public void showDocument(URL url) {
	// We do nothing.
    }

    public void showDocument(URL url, String target) {
	// We do nothing.
    }

    public void showStatus(String status) {
	// We do nothing.
    }
}

/**
 * Package private support class.  This provides an AppletStub
 * for beans which are applets.
 */
class BeansAppletStub implements AppletStub {
    transient boolean active;
    transient Applet target;
    transient AppletContext context;
    transient URL codeBase;
    transient URL docBase;

    BeansAppletStub(Applet target,
		AppletContext context, URL codeBase, 
				URL docBase) {
        this.target = target;
	this.context = context;
	this.codeBase = codeBase;
	this.docBase = docBase;
    }

    public boolean isActive() {
	return active;
    }
    
    public URL getDocumentBase() {
	// use the root directory of the applet's class-loader
	return docBase;
    }

    public URL getCodeBase() {
	// use the directory where we found the class or serialized object.
	return codeBase;
    }

    public String getParameter(String name) {
	return null;
    }

    public AppletContext getAppletContext() {
	return context;
    }

    public void appletResize(int width, int height) {
	// we do nothing.
    }
}
