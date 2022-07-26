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

	    // Figure our the codebase and docbase URLs.  We do this
	    // by locating the URL for a known resource, and then
	    // massaging the URL.

	    // First find the "resource name" corresponding to the bean
	    // itself.  So a serialzied bean "a.b.c" would imply a resource
	    // name of "a/b/c.ser" and a classname of "x.y" would imply
	    // a resource name of "x/y.class".

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

	    // If we found a URL, we try to locate the docbase by taking
	    // of the final path name component, and the code base by taking
   	    // of the complete resourceName.
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

	    // If it was deserialized then it was already init-ed.  Otherwise
	    // we need to initialize it.
	    if (!serialized) {
		// We need to set a reasonable initial size, as many
		// applets are unhappy if they are started without 
		// having been explicitly sized.
		applet.setSize(100,100);
		applet.init();
	    }
	    stub.active = true;
	}
	return result;
    }


    /**
     * From a given bean, obtain an object representing a specified
     * type view of that source object. 
     * <p>
     * The result may be the same object or a different object.  If
     * the requested target view isn't available then the given
     * bean is returned.
     * <p>
     * This method is provided in Beans 1.0 as a hook to allow the
     * addition of more flexible bean behaviour in the future.
     *
     * @param obj  Object from which we want to obtain a view.
     * @param targetType  The type of view we'd like to get.
     *
     */
    public static Object getInstanceOf(Object bean, Class targetType) {
    	return bean;
    }
 
    /**
     * Check if a bean can be viewed as a given target type.
     * The result will be true if the Beans.getInstanceof method
     * can be used on the given bean to obtain an object that
     * represents the specified targetType type view.
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
     * @return  True if we are running in an environment where beans
     *	   can assume that an interactive GUI is available, so they 
     *	   can pop up dialog boxes, etc.  This will normally return
     *	   true in a windowing environment, and will normally return
     *	   false in a server environment or if an application is
     *	   running as part of a batch job.
     */
    public static boolean isGuiAvailable() {
	return guiAvailable;
    }

    /**
     * Used to indicate whether of not we are running in an application
     * builder environment.  Note that this method is security checked
     * and is not available to (for example) untrusted applets.
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
