/*
 * @(#)DriverManager.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * DriverManager 提供了用于管理一组 JDBC 驱动程序的基本服务。
 *
 * 作为其初始化的一部分，DriverManager 类将尝试加载在“jdbc.drivers”系统属性中引用的驱动程序类。
 * 这允许用户自定义其应用程序使用的 JDBC 驱动程序。例如在你的
 * ~/.hotjava/properties file you might specify:
 * jdbc.drivers=foo.bah.Driver:wombat.sql.Driver:bad.taste.ourDriver
 *
 * 程序也可以随时显式加载 JDBC 驱动程序。例如，my.sql.Driver 加载了以下语句：
 * <CODE>Class.forName("my.sql.Driver");</CODE>
 *
 * 当调用 getConnection 时，DriverManager 将尝试从初始化时加载的驱动程序和使用与
 * 当前小程序或应用程序相同的类加载器显式加载的驱动程序中找到合适的驱动程序。
 *
 * @see Driver
 * @see Connection 
 */
public class DriverManager {

    /**
     * 尝试建立到给定数据库 URL 的连接。 DriverManager 尝试从已注册的 JDBC 驱动程序集中选择合适的驱动程序。
     *
     * @param url a database url of the form jdbc:subprotocol:subname
     * @param info 作为连接参数的任意字符串标记值对列表；通常至少应包含“用户”和“密码”属性
     * @return a Connection to the URL 
     * @exception SQLException if a database-access error occurs.
     */
    public static synchronized Connection getConnection(String url, 
            java.util.Properties info) throws SQLException {
	if(url == null) {
	    throw new SQLException("The url cannot be null", "08001");
	}

        println("DriverManager.getConnection(\"" + url + "\")");

        if (!initialized) {
            initialize();
        }

        // Figure out the current security context.（找出当前的安全上下文。）
        Object currentSecurityContext = getSecurityContext();

        // 浏览已加载的驱动程序以尝试建立连接。
        // 记住第一个引发的异常，以便我们可以重新引发它。
        SQLException reason = null;
        for (int i = 0; i < drivers.size(); i++) {
            DriverInfo di = (DriverInfo)drivers.elementAt(i);
            // 如果驱动程序不是基本系统的一部分并且与当前调用者不来自相同的安全上下文，则跳过它。
            if (di.securityContext != null && 
                        di.securityContext != currentSecurityContext) {
                println("    skipping: " + di);
                continue;
            }
            try {
                println("    trying " + di);
                Connection result = di.driver.connect(url, info);
                if (result != null) {
                    // Success!
                    println("getConnection returning " + di);
                    return (result);
                }
            } catch (SQLException ex) {
                if (reason == null) {
                    reason = ex;
                }
            }
        }

        // if we got here nobody could connect.
        if (reason != null)    {
            println("getConnection failed: " + reason);
            throw reason;
        }

        println("getConnection: no suitable driver");
        throw new SQLException("No suitable driver", "08001");
    }

    /**
     * 尝试建立到给定数据库 URL 的连接。 DriverManager 尝试从已注册的 JDBC 驱动程序集中选择合适的驱动程序。
     *
     * @param url a database url of the form jdbc:<em>subprotocol</em>:<em>subname</em>
     * @param user the database user on whose behalf the Connection is being made
     * @param password the user's password
     * @return a Connection to the URL 
     * @exception SQLException if a database-access error occurs.
     */
    public static synchronized Connection getConnection(String url, 
		            String user, String password) throws SQLException {
        java.util.Properties info = new java.util.Properties();
	if (user != null) {
	    info.put("user", user);
	}
	if (password != null) {
	    info.put("password", password);
	}
        return (getConnection(url, info));
    }

    /**
     * 尝试建立到给定数据库 URL 的连接。 DriverManager 尝试从已注册的 JDBC 驱动程序集中选择合适的驱动程序。
     *
     * @param url a database url of the form jdbc:<em>subprotocol</em>:<em>subname</em>
     * @return a Connection to the URL 
     * @exception SQLException if a database-access error occurs.
     */
    public static synchronized Connection getConnection(String url) 
                                    throws SQLException {
        java.util.Properties info = new java.util.Properties();
        return (getConnection(url, info));
    }

    /**
     * Attempt to locate a driver that understands the given URL.
     * The DriverManager attempts to select an appropriate driver from
     * the set of registered JDBC drivers. 
     *
     * @param url a database url of the form jdbc:<em>subprotocol</em>:<em>subname</em>
     * @return a Driver that can connect to the URL 
     * @exception SQLException if a database-access error occurs.
     */
    public static Driver getDriver(String url) throws SQLException {
        println("DriverManager.getDriver(\"" + url + "\")");

        if (!initialized) {
            initialize();
        }

        // Figure out the current security context.
        Object currentSecurityContext = getSecurityContext();

        // Walk through the loaded drivers attempting to locate someone
	// who understands the given URL.
        for (int i = 0; i < drivers.size(); i++) {
            DriverInfo di = (DriverInfo)drivers.elementAt(i);
            // If the driver isn't part of the base system and doesn't come
            // from the same security context as the current caller, skip it.
            if (di.securityContext != null && 
                        di.securityContext != currentSecurityContext) {
                println("    skipping: " + di);
                continue;
            }
            try {
                println("    trying " + di);
		if (di.driver.acceptsURL(url)) {
		    // Success!
                    println("getDriver returning " + di);
                    return (di.driver);
                }
            } catch (SQLException ex) {
		// Drop through and try the next driver.
            }
        }

        println("getDriver: no suitable driver");
        throw new SQLException("No suitable driver", "08001");
    }


    /**
     * A newly loaded driver class should call registerDriver to make itself
     * known to the DriverManager.
     *
     * @param driver the new JDBC Driver 
     * @exception SQLException if a database-access error occurs.
     */
    public static synchronized void registerDriver(Driver driver)
                        throws SQLException {
        if (!initialized) {
            initialize();
        }
        DriverInfo di = new DriverInfo();
        di.driver = driver;
        di.className = driver.getClass().getName();
        // Note our current securityContext.
        di.securityContext = getSecurityContext();
        drivers.addElement(di);
        println("registerDriver: " + di);
    }


    /**
     * Drop a Driver from the DriverManager's list.  Applets can only
     * deregister Drivers from their own classloader.
     *
     * @param driver the JDBC Driver to drop 
     * @exception SQLException if a database-access error occurs.
     */
    public static void deregisterDriver(Driver driver) throws SQLException {
        // Figure out the current security context.
        Object currentSecurityContext = getSecurityContext();
        println("DriverManager.deregisterDriver: " + driver);

        // Walk through the loaded drivers.
        int i;
        DriverInfo di = null;
        for (i = 0; i < drivers.size(); i++) {
            di = (DriverInfo)drivers.elementAt(i);
            if (di.driver == driver) {
                break;
            }
        }
        // If we can't find the driver just return.
        if (i >= drivers.size()) {
            println("    couldn't find driver to unload");
            return;
        }

            // If an applet is trying to free a driver from somewhere else
        // throw a security exception.
        if (currentSecurityContext != null &&
                di.securityContext != currentSecurityContext) {
            throw new SecurityException();
        }

        // Remove the driver.  Other entries in drivers get shuffled down.
        drivers.removeElementAt(i);
    
    }

    /**
     * Return an Enumeration of all the currently loaded JDBC drivers
     * which the current caller has access to.
     *
     * <P><B>Note:</B> The classname of a driver can be found using
     * <CODE>d.getClass().getName()</CODE>
     *
     * @return the list of JDBC Drivers loaded by the caller's class loader
     */
    public static java.util.Enumeration getDrivers() {
        java.util.Vector result = new java.util.Vector();

        if (!initialized) {
            initialize();
        }

        // Figure out the current security context.
        Object currentSecurityContext = getSecurityContext();

        // Walk through the loaded drivers.
        for (int i = 0; i < drivers.size(); i++) {
            DriverInfo di = (DriverInfo)drivers.elementAt(i);
            // if the driver isn't part of the base system and doesn't come
            // from the same security context as the current caller, skip it.
            if (di.securityContext != null && 
                        di.securityContext != currentSecurityContext) {
                println("    skipping: " + di);
                continue;
            }
            result.addElement(di.driver);
        }

        return (result.elements());
    }


    /**
     * Set the maximum time in seconds that all drivers can wait
     * when attempting to log in to a database.
     *
     * @param seconds the driver login time limit
     */
    public static void setLoginTimeout(int seconds) {
        loginTimeout = seconds;
    }

    /**
     * Get the maximum time in seconds that all drivers can wait
     * when attempting to log in to a database.
     *
     * @return the driver login time limit
     */
    public static int getLoginTimeout() {
        return (loginTimeout);
    }


    /**
     * Set the logging/tracing PrintStream that is used by the DriverManager
     * and all drivers.
     *
     * @param out the new logging/tracing PrintStream; to disable, set to null
     */
    public static void setLogStream(java.io.PrintStream out) {
        logStream = out;
    }

    /**
     * Get the logging/tracing PrintStream that is used by the DriverManager
     * and all drivers.
     *
     * @return the logging/tracing PrintStream; if disabled, is null
     */
    public static java.io.PrintStream getLogStream() {
        return (logStream);
    }

    /**
     * Print a message to the current JDBC log stream
     *
     * @param message a log or tracing message
     */
    public static void println(String message) {
        if (logStream != null) {
            logStream.println(message);
        }
    }

    //-------------------------------------------------------------------------

    private static Object getSecurityContext() {
        // Get the securityContext for our caller.  For applets this
        // will be the applet classloader base URL.
        SecurityManager security = System.getSecurityManager();    
        if (security == null) {
            return (null);
        }
        return (security.getSecurityContext());
    }

    private static void loadInitialDrivers() {
        String drivers;
        try {
            drivers = System.getProperty("jdbc.drivers");
        } catch (Exception ex) {
            drivers = null;
        }
        println("DriverManager.initialize: jdbc.drivers = " + drivers);
        if (drivers == null) {
            return;
        }
        while (drivers.length() != 0) {
            int x = drivers.indexOf(':');
            String driver;
            if (x < 0) {
                driver = drivers;
                drivers = "";
            } else {
                driver = drivers.substring(0, x);
                drivers = drivers.substring(x+1);
            }
            if (driver.length() == 0) {
                continue;
            }
            try {
                println("DriverManager.Initialize: loading " + driver);
                Class.forName(driver);
            } catch (Exception ex) {
                println("DriverManager.Initialize: load failed: " + ex);
            }
        }
    }

    // Class initialization.
    static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        loadInitialDrivers();
        println("JDBC DriverManager initialized");
    }

    // Prevent the DriverManager class from being instantiated.
    private DriverManager(){}

    private static java.util.Vector drivers = new java.util.Vector();
    private static int loginTimeout = 0;
    private static java.io.PrintStream logStream = null;
    private static boolean initialized = false;

}


// DriverInfo is a package-private support class.
class DriverInfo {
    Driver         driver;
    Object        securityContext;
    String        className;

    public String toString() {
        return ("driver[className=" + className + ",context=" +
        securityContext + "," + driver + "]");
    }
}
