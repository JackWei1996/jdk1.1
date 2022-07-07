/*
 * @(#)PropertyEditorManager.java	1.28 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * PropertyEditorManager 可用于定位任何给定类型名称的属性编辑器。
 * 此属性编辑器必须支持 java.beans.PropertyEditor 接口以编辑给定对象。
 *
 * PropertyEditorManager 使用三种技术来定位给定类型的编辑器。
 * 首先，它提供了一个 registerEditor 方法，允许为给定类型专门注册一个编辑器。
 * 其次，它尝试通过将“Editor”添加到给定类型的全限定类名（例如“foo.bah.FozEditor”）来定位合适的类。
 * 最后，它采用简单的类名（没有包名）向其添加“编辑器”，并在包的搜索路径中查找匹配的类。
 *
 * 因此，对于输入类 foo.bah.Fred，PropertyEditorManager
 * 将首先查看其表以查看是否已为 foo.bah.Fred 注册了编辑器，如果已注册，则使用它。
 * 然后它会寻找一个 foo.bah.FredEditor 类。然后它会寻找（比如说）standardEditorsPackage.FredEditor 类。
 *
 * 将为 Java 内置类型“boolean”、“byte”、“short”、“int”、“long”、“float”和“double”提供默认 PropertyEditor；
 * 对于 java.lang.String 类。 java.awt.Color 和 java.awt.Font。
 */

public class PropertyEditorManager {

    /**
     * 注册一个编辑器类以用于编辑给定目标类的值。
     * @param targetType the Class object of the type to be edited
     * @param editorClass the Class object of the editor class.  If
     *	   this is null, then any existing definition will be removed.
     */

    public static void registerEditor(Class targetType, Class editorClass) {
	initialize();
	if (editorClass == null) {
	    registry.remove(targetType);
	} else {
	    registry.put(targetType, editorClass);
	}
    }

    /**
     * Locate a value editor for a given target type.
     * @param targetType  The Class object for the type to be edited
     * @return An editor object for the given target class. 
     * The result is null if no suitable editor can be found.
     */

    public static PropertyEditor findEditor(Class targetType) {
	initialize();
	Class editorClass = (Class)registry.get(targetType);
	if (editorClass != null) {
	    try {
		Object o = editorClass.newInstance();
        	return (PropertyEditor)o;
	    } catch (Exception ex) {
	 	System.err.println("Couldn't instantiate type editor \"" +
			editorClass.getName() + "\" : " + ex);
	    }
	}

	// Now try adding "Editor" to the class name.

	String editorName = targetType.getName() + "Editor";
	try {
	    return instantiate(targetType, editorName);
	} catch (Exception ex) {
	   // Silently ignore any errors.
	}

	// Now try looking for <searchPath>.fooEditor
	editorName = targetType.getName();
   	while (editorName.indexOf('.') > 0) {
	    editorName = editorName.substring(editorName.indexOf('.')+1);
	}
	for (int i = 0; i < searchPath.length; i++) {
	    String name = searchPath[i] + "." + editorName + "Editor";
	    try {
	        return instantiate(targetType, name);
	    } catch (Exception ex) {
	       // Silently ignore any errors.
	    }
	}

	// We couldn't find a suitable Editor.
	return (null);
    }

    private static PropertyEditor instantiate(Class sibling, String className)
		 throws InstantiationException, IllegalAccessException,
						ClassNotFoundException {
	// First check with siblings classloader (if any). 
	ClassLoader cl = sibling.getClassLoader();
	if (cl != null) {
	    try {
	        Class cls = cl.loadClass(className);
	    	Object o = cls.newInstance();
	        PropertyEditor ed = (PropertyEditor)o;
		return ed;
	    } catch (Exception ex) {
	        // Just drop through
	    }
        }
	// Now try the system classloader.
	Class cls = Class.forName(className);
	Object o = cls.newInstance();
        PropertyEditor ed = (PropertyEditor)o;
	return ed;
    }

    /**
     * @return  将搜索以查找属性编辑器的包名称数组。
     * <p>     This is initially set to {"sun.beans.editors"}.
     */

    public static String[] getEditorSearchPath() {
	return searchPath;
    }

    /**
     * Change the list of package names that will be used for
     *		finding property editors.
     * @param path  Array of package names.
     */

    public static void setEditorSearchPath(String path[]) {
	if (path == null) {
	    path = new String[0];
	}
	searchPath = path;
    }

    private static void load(Class targetType, String name) {
	String editorName = name;
	for (int i = 0; i < searchPath.length; i++) {
	    try {
	        editorName = searchPath[i] + "." + name;
	        Class cls = Class.forName(editorName);
	        registry.put(targetType, cls);
		return;
	    } catch (Exception ex) {
		// Drop through and try next package.
	    }
	}
	// This shouldn't happen.
	System.err.println("load of " + editorName + " failed");
    }


    private static synchronized void initialize() {
	if (registry != null) {
	    return;
	}
	registry = new java.util.Hashtable();
	load(Byte.TYPE, "ByteEditor");
	load(Short.TYPE, "ShortEditor");
	load(Integer.TYPE, "IntEditor");
	load(Long.TYPE ,"LongEditor");
	load(Boolean.TYPE, "BoolEditor");
	load(Float.TYPE, "FloatEditor");
	load(Double.TYPE, "DoubleEditor");
    }

    private static String[] searchPath = { "sun.beans.editors" };
    private static java.util.Hashtable registry;
}
