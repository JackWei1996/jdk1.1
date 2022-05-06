/*
 * @(#)Driver.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * Java SQL 框架允许多个数据库驱动程序。
 *
 * 每个驱动程序都应提供一个实现驱动程序接口的类。
 *
 * DriverManager 将尝试加载尽可能多的驱动程序，然后对于任何给定的连接请求，
 * 它将依次要求每个驱动程序尝试连接到目标 URL。
 *
 * 强烈建议每个 Driver 类应该是小而独立的，
 * 这样 Driver 类就可以被加载和查询，而不需要引入大量的支持代码。
 *
 * 当一个 Driver 类被加载时，它应该创建一个自身的实例并将其注册到 DriverManager。
 * 这意味着用户可以通过执行 Class.forName("foo.bah.Driver") 来加载和注册驱动程序。
 *
 * @see DriverManager
 * @see Connection 
 */
public interface Driver {

    /**
     * 尝试与给定 URL 建立数据库连接。如果驱动程序意识到连接到给定 URL 的驱动程序类型错误，则应返回“null”。
     * 这很常见，因为当要求 JDBC 驱动程序管理器连接到给定的 URL 时，它会依次将 URL 传递给每个加载的驱动程序。
     *
     * 如果驱动程序是连接到给定 URL 的正确驱动程序，
     * 则驱动程序应引发 SQLException，但连接到数据库时遇到问题。
     *
     * java.util.Properties 参数可用于传递任意字符串标记值对作为连接参数。
     * 通常至少“用户”和“密码”属性应该包含在属性中。
     *
     * @param url 要连接的数据库的 URL
     * @param info 作为连接参数的任意字符串标记值对列表；通常至少应包含“用户”和“密码”属性
     * @return 与 URL 的连接
     * @exception SQLException 如果发生数据库访问错误。
     */
    Connection connect(String url, java.util.Properties info)
        throws SQLException;

    /**
     * 如果驱动程序认为它可以打开到给定 URL 的连接，则返回 true。
     * 如果驱动程序理解 URL 中指定的子协议，通常会返回 true，否则返回 false。
     *
     * @param url 数据库的 URL。
     * @return 如果此驱动程序可以连接到给定的 URL，则为真。
     * @exception SQLException 如果发生数据库访问错误。
     */
    boolean acceptsURL(String url) throws SQLException;


    /**
     * getPropertyInfo 方法旨在允许通用 GUI 工具发现它应该提示人类哪些属性，
     * 以便获得足够的信息来连接到数据库。请注意，根据迄今为止人类提供的值，
     * 可能需要额外的值，因此可能需要通过多次调用 getPropertyInfo 进行迭代。
     *
     * @param url 要连接的数据库的 URL。
     * @param info 将在连接打开时发送的标记值对的建议列表。
     * @return 描述可能属性的 DriverPropertyInfo 对象数组。
     *         如果不需要属性，则此数组可能是一个空数组。
     * @exception SQLException 如果发生数据库访问错误。
     */
    DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info)
			 throws SQLException;


    /**
     * 获取驱动程序的主要版本号。最初这应该是 1。
     */
    int getMajorVersion();

    /**
     * 获取驱动程序的次要版本号。最初这应该是 0。
     */
    int getMinorVersion();


    /**
     * 报告驱动程序是否是真正的 JDBC COMPLIANT (tm) 驱动程序。
     * 如果驱动程序通过 JDBC 合规性测试，则只能在此处报告“true”，否则需要返回 false。
     *
     * JDBC 合规性要求完全支持 JDBC API 和完全支持 SQL 92 入门级。
     * 预计所有主要商业数据库都可以使用符合 JDBC 的驱动程序。
     *
     * 此方法并非旨在鼓励开发不兼容 JDBC 的驱动程序，
     * 而是承认一些供应商有兴趣将 JDBC API 和框架用于不支持完整数据库功能的轻量级数据库
     * 或特殊数据库例如文档信息检索，其中 SQL 实现可能不可行。
     */
    boolean jdbcCompliant();
} 
