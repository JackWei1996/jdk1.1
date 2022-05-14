/*
 * @(#)Statement.java	1.8 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * Statement 对象用于执行静态 SQL 语句并获取其产生的结果。
 *
 * 在任何时间点，每个 Statement 只能打开一个 ResultSet。
 * 因此，如果一个 ResultSet 的读取与另一个 ResultSet 的读取交错，
 * 那么每个 ResultSet 肯定是由不同的 Statement 生成的。
 * 如果存在打开的 ResultSet，所有语句执行方法都会隐式关闭语句的当前 ResultSet。
 *
 * @see Connection#createStatement
 * @see ResultSet 
 */
public interface Statement {

    /**
     * 执行返回单个 ResultSet 的 SQL 语句。
     *
     * @param sql 通常这是一个静态 SQL SELECT 语句
     * @return 包含查询产生的数据的 ResultSet；从不为空
     * @exception SQLException 如果发生数据库访问错误。
     */
    ResultSet executeQuery(String sql) throws SQLException;

    /**
     * 执行 SQL INSERT、UPDATE 或 DELETE 语句。此外，可以执行不返回任何内容的 SQL 语句，例如 SQL DDL 语句。
     *
     * @param sql SQL INSERT、UPDATE 或 DELETE 语句或不返回任何内容的 SQL 语句
     * @return INSERT、UPDATE 或 DELETE 的行数或不返回任何内容的 SQL 语句的 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    int executeUpdate(String sql) throws SQLException;

    /**
     * 在许多情况下，最好立即释放 Statements 的数据库和 JDBC 资源，而不是等待它自动关闭时发生； close 方法提供了这个即时发布。
     *
     * Note: 垃圾回收时，Statement 会自动关闭。当一个 Statement 被关闭时，它的当前 ResultSet（如果存在）也被关闭。
     *
     * @exception SQLException 如果发生数据库访问错误。
     */
    void close() throws SQLException;

    //----------------------------------------------------------------------

    /**
     * maxFieldSize 限制（以字节为单位）是为任何列值返回的最大数据量；
     * 它仅适用于 BINARY、VARBINARY、LONGVARBINARY、CHAR、VARCHAR
     * 和 LONGVARCHAR 列。如果超出限制，多余的数据将被静默丢弃。
     *
     * @return 当前最大列大小限制；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    int getMaxFieldSize() throws SQLException;
    
    /**
     * maxFieldSize 限制（以字节为单位）设置为限制任何列值可以返回的数据大小；
     * 它仅适用于 BINARY、VARBINARY、LONGVARBINARY、CHAR、VARCHAR 和 LONGVARCHAR 字段。
     * 如果超出限制，多余的数据将被静默丢弃。要获得最大可移植性，请使用大于 256 的值。
     *
     * @param max 新的最大列大小限制；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    void setMaxFieldSize(int max) throws SQLException;

    /**
     * maxRows 限制是 ResultSet 可以包含的最大行数。如果超出限制，则会静默删除多余的行。
     *
     * @return 当前最大行数限制；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    int getMaxRows() throws SQLException;

    /**
     * maxRows 限制设置为限制任何 ResultSet 可以包含的行数。如果超出限制，则会静默删除多余的行。
     *
     * @param max 新的最大行数限制；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    void setMaxRows(int max) throws SQLException;

    /**
     * 如果转义扫描打开（默认），驱动程序将在将 SQL 发送到数据库之前进行转义替换。
     *
     * 注意：由于准备好的语句通常在进行此调用之前已被解析，因此禁用准备好的语句的转义处理将不会有任何影响。
     *
     * @param enable true to enable; false to disable
     * @exception SQLException 如果发生数据库访问错误。
     */
    void setEscapeProcessing(boolean enable) throws SQLException;

    /**
     * queryTimeout 限制是驱动程序等待语句执行的秒数。如果超出限制，则会引发 SQLException。
     *
     * @return 当前查询超时限制（以秒为单位）；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    int getQueryTimeout() throws SQLException;

    /**
     * queryTimeout 限制是驱动程序等待语句执行的秒数。如果超出限制，则会引发 SQLException。
     *
     * @param seconds 以秒为单位的新查询超时限制；零意味着无限
     * @exception SQLException 如果发生数据库访问错误。
     */
    void setQueryTimeout(int seconds) throws SQLException;

    /**
     * 一个线程可以使用 Cancel 来取消另一个线程正在执行的语句。
     *
     * @exception SQLException 如果发生数据库访问错误。
     */
    void cancel() throws SQLException;

    /**
     * 返回调用此语句报告的第一个警告。 Statment 的执行方法会清除其 SQLWarning 链。随后的语句警告将链接到此 SQLWarning。
     *
     * 每次（重新）执行语句时，警告链都会自动清除。
     *
     * Note: 如果您正在处理 ResultSet，则与 ResultSet 读取相关的任何警告都将链接在 ResultSet 对象上。
     *
     * @return 第一个 SQLWarning 或 null
     * @exception SQLException 如果发生数据库访问错误。
     */
    SQLWarning getWarnings() throws SQLException;

    /**
     * 在此调用之后，getWarnings 将返回 null，直到针对此 Statement 报告新的警告。
     *
     * @exception SQLException 如果发生数据库访问错误。
     */
    void clearWarnings() throws SQLException;

    /**
     * setCursorname 定义将由后续 Statement 执行方法使用的 SQL 游标名称。
     * 然后可以在 SQL 定位的 updatedelete 语句中使用此名称来标识此语句生成的 ResultSet 中的当前行。
     * 如果数据库不支持定位更新删除，则此方法是一个 noop。
     *
     * Note: 根据定义，定位更新删除的执行必须由与生成用于定位的 ResultSet 的语句不同的语句完成。
     * 此外，游标名称在连接中必须是唯一的。
     *
     * @param name the new cursor name.  
     * @exception SQLException if a database-access error occurs.
     */
    void setCursorName(String name) throws SQLException;
	
    //----------------------- Multiple Results --------------------------

    /**
     * 执行可能返回多个结果的 SQL 语句。在某些（不常见的）情况下，
     * 单个 SQL 语句可能会返回多个结果集和/或更新计数。
     * 通常你可以忽略这一点，除非你正在执行一个你知道可能返回多个结果的存储过程，
     * 或者除非你正在动态执行一个未知的 SQL 字符串。
     * 12v b“execute”、“getMoreResults”、“getResultSet”和“getUpdateCount”方法让您可以浏览多个结果。
     *
     * The "execute" method executes a SQL statement and indicates the
     * form of the first result.  You can then use getResultSet or
     * getUpdateCount to retrieve the result, and getMoreResults to
     * move to any subsequent result(s).
     *
     * @param sql any SQL statement
     * @return true if the next result is a ResultSet; false if it is
     * an update count or there are no more results
     * @exception SQLException if a database-access error occurs.
     * @see #getResultSet
     * @see #getUpdateCount
     * @see #getMoreResults 
     */
    boolean execute(String sql) throws SQLException;
	
    /**
     *  getResultSet returns the current result as a ResultSet.  It
     *  should only be called once per result.
     *
     * @return the current result as a ResultSet; null if the result
     * is an update count or there are no more results
     * @exception SQLException if a database-access error occurs.
     * @see #execute 
     */
    ResultSet getResultSet() throws SQLException; 

    /**
     *  getUpdateCount returns the current result as an update count;
     *  if the result is a ResultSet or there are no more results, -1
     *  is returned.  It should only be called once per result.
     * 
     * @return the current result as an update count; -1 if it is a
     * ResultSet or there are no more results
     * @exception SQLException if a database-access error occurs.
     * @see #execute 
     */
    int getUpdateCount() throws SQLException; 

    /**
     * getMoreResults moves to a Statement's next result.  It returns true if 
     * this result is a ResultSet.  getMoreResults also implicitly
     * closes any current ResultSet obtained with getResultSet.
     *
     * There are no more results when (!getMoreResults() &&
     * (getUpdateCount() == -1)
     *
     * @return true if the next result is a ResultSet; false if it is
     * an update count or there are no more results
     * @exception SQLException if a database-access error occurs.
     * @see #execute 
     */
    boolean getMoreResults() throws SQLException; 
}	
