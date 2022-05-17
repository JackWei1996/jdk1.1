/*
 * @(#)Connection.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * Connection 表示与特定数据库的会话。在 Connection 的上下文中，执行 SQL 语句并返回结果。
 *
 * Connection 的数据库能够提供描述其表、支持的 SQL 语法、存储过程、此连接的功能等的信息。
 * 这些信息是通过 getMetaData 方法获得的。
 *
 * Note: 默认情况下，连接会在执行每个语句后自动提交更改。
 * 如果禁用了自动提交，则必须进行显式提交，否则将不会保存数据库更改。
 *
 * @see DriverManager#getConnection
 * @see Statement 
 * @see ResultSet
 * @see DatabaseMetaData
 */
public interface Connection {

    /**
     * 不带参数的 SQL 语句通常使用 Statement 对象执行。
     * 如果多次执行相同的 SQL 语句，使用 PreparedStatement 效率更高
     *
     * @return a new Statement object 
     * @exception SQLException if a database-access error occurs.
     */
    Statement createStatement() throws SQLException;

    /**
     * 带或不带 IN 参数的 SQL 语句可以预编译并存储在 PreparedStatement 对象中。
     * 然后可以使用该对象多次有效地执行该语句。
     *
     * Note: 此方法针对处理受益于预编译的参数化 SQL 语句进行了优化。
     * 如果驱动支持预编译，prepareStatement 会将语句发送到数据库进行预编译。
     * 某些驱动程序可能不支持预编译。在这种情况下，在执行 PreparedStatement 之前，
     * 该语句可能不会发送到数据库。这对用户没有直接影响；
     * 但是，它确实会影响哪个方法抛出某些 SQLExceptions。
     *
     * @param sql 可能包含一个或多个“？”的 SQL 语句IN 参数占位符
     * @return 包含预编译语句的新 PreparedStatement 对象
     * @exception SQLException if a database-access error occurs.
     */
    PreparedStatement prepareStatement(String sql)
	    throws SQLException;

    /**
     * SQL 存储过程调用语句是通过为其创建 CallableStatement 来处理的。
     * CallableStatement 提供了设置其 IN 和 OUT 参数的方法，以及执行它的方法。
     *
     * Note: 此方法针对处理存储过程调用语句进行了优化。
     * 一些驱动程序可能会在 prepareCall 完成时将调用语句发送到数据库；
     * 其他人可能会等到 CallableStatement 被执行。这对用户没有直接影响；
     * 但是，它确实会影响哪个方法抛出某些 SQLExceptions。
     *
     * @param sql 可能包含一个或多个“？”的 SQL 语句参数占位符。通常，此语句是 JDBC 函数调用转义字符串。
     * @return 包含预编译 SQL 语句的新 CallableStatement 对象
     * @exception SQLException if a database-access error occurs.
     */
    CallableStatement prepareCall(String sql) throws SQLException;
						
    /**
     * 驱动程序可以在发送之前将 JDBC sql 语法转换为其系统的本机 SQL 语法；
     * nativeSQL 返回驱动程序将发送的语句的本机形式。
     *
     * @param sql a SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return the native form of this statement
     * @exception SQLException if a database-access error occurs.
     */
    String nativeSQL(String sql) throws SQLException;

    /**
     * 如果连接处于自动提交模式，则其所有 SQL 语句将作为单独的事务执行和提交。
     * 否则，它的 SQL 语句将被分组为由 commit() 或 rollback() 终止的事务。默认情况下，新连接处于自动提交模式。
     *
     * 提交发生在语句完成或下一次执行发生时，以先到者为准。
     * 对于返回 ResultSet 的语句，当检索到 ResultSet 的最后一行或关闭 ResultSet 时，语句完成。
     * 在高级情况下，单个语句可能会返回多个结果以及输出参数值。当检索到所有结果和输出参数值时，就会发生提交。
     *
     * @param autoCommit true enables auto-commit; false disables
     * auto-commit.  
     * @exception SQLException if a database-access error occurs.
     */
    void setAutoCommit(boolean autoCommit) throws SQLException;

    /**
     * Get the current auto-commit state.
     *
     * @return Current state of auto-commit mode.
     * @exception SQLException if a database-access error occurs.
     * @see #setAutoCommit 
     */
    boolean getAutoCommit() throws SQLException;

    /**
     * Commit 使自上次 commitrollback 以来所做的所有更改永久化，
     * 并释放 Connection 当前持有的所有数据库锁。此方法仅应在禁用自动提交时使用。
     *
     * @exception SQLException if a database-access error occurs.
     * @see #setAutoCommit 
     */
    void commit() throws SQLException;

    /**
     * 回滚删除自上次提交回滚以来所做的所有更改，并释放连接当前持有的所有数据库锁。
     * 此方法仅应在禁用自动提交时使用。
     *
     * @exception SQLException if a database-access error occurs.
     * @see #setAutoCommit 
     */
    void rollback() throws SQLException;

    /**
     * 在某些情况下，希望立即释放 Connection 的数据库和 JDBC 资源，而不是等待它们自动释放； close 方法提供了这个即时发布。
     *
     * Note: 垃圾收集时连接会自动关闭。某些致命错误也会导致连接关闭。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void close() throws SQLException;;

    /**
     * Tests to see if a Connection is closed.
     *
     * @return true if the connection is closed; false if it's still open
     * @exception SQLException if a database-access error occurs.
     */
    boolean isClosed() throws SQLException;;

    //======================================================================
    // Advanced features（高级功能）:

    /**
     * 连接的数据库能够提供描述其表、其支持的 SQL 语法、其存储过程、此连接的功能等的信息。
     * 这些信息通过 DatabaseMetaData 对象提供。
     *
     * @return a DatabaseMetaData object for this Connection 
     * @exception SQLException if a database-access error occurs.
     */
    DatabaseMetaData getMetaData() throws SQLException;;

    /**
     * 您可以将连接置于只读模式作为启用数据库优化的提示。
     *
     * Note: 在事务中间不能调用 setReadOnly。
     *
     * @param readOnly true enables read-only mode; false disables
     * read-only mode.  
     * @exception SQLException if a database-access error occurs.
     */
    void setReadOnly(boolean readOnly) throws SQLException;

    /**
     * Tests to see if the connection is in read-only mode.
     *
     * @return true if connection is read-only
     * @exception SQLException if a database-access error occurs.
     */
    boolean isReadOnly() throws SQLException;

    /**
     * 可以通过设置目录名称来选择此连接数据库的子空间。如果驱动程序不支持目录，它将默默地忽略此请求。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void setCatalog(String catalog) throws SQLException;

    /**
     * Return the Connection's current catalog name.（返回连接的当前目录名称。）
     *
     * @return the current catalog name or null
     * @exception SQLException if a database-access error occurs.
     */
    String getCatalog() throws SQLException;

    /**
     * Transactions are not supported. （不支持交易）
     */
    int TRANSACTION_NONE	     = 0;

    /**
     * Dirty reads, non-repeatable reads and phantom reads can occur.
     * 可能会出现脏读、不可重复读和幻读。
     */
    int TRANSACTION_READ_UNCOMMITTED = 1;

    /**
     * Dirty reads are prevented; non-repeatable reads and phantom
     * reads can occur.
     * 防止脏读；可能发生不可重复读取和幻读。
     */
    int TRANSACTION_READ_COMMITTED   = 2;

    /**
     * 防止脏读和不可重复读；可能发生幻读。
     */
    int TRANSACTION_REPEATABLE_READ  = 4;

    /**
     * Dirty reads, non-repeatable reads and phantom reads are prevented.
     * 防止脏读、不可重复读和幻读。
     */
    int TRANSACTION_SERIALIZABLE     = 8;

    /**
     * 您可以调用此方法来尝试使用 TRANSACTION_* 值之一更改事务隔离级别。
     *
     * Note:在事务中间不能调用 setTransactionIsolation。
     *
     * @param level 除 TRANSACTION_NONE 外的 TRANSACTION_* 隔离值之一；一些数据库可能不支持其他值
     * @exception SQLException if a database-access error occurs.
     * @see DatabaseMetaData#supportsTransactionIsolationLevel 
     */
    void setTransactionIsolation(int level) throws SQLException;

    /**
     * 获取此 Connection 当前的事务隔离模式。
     *
     * @return the current TRANSACTION_* mode value
     * @exception SQLException if a database-access error occurs.
     */
    int getTransactionIsolation() throws SQLException;

    /**
     * 返回在此 Connection 上调用报告的第一个警告。
     *
     * Note: 随后的警告将链接到此 SQLWarning。
     *
     * @return the first SQLWarning or null 
     * @exception SQLException if a database-access error occurs.
     */
    SQLWarning getWarnings() throws SQLException;

    /**
     * 在此调用之后，getWarnings 返回 null，直到为此 Connection 报告了新的警告。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void clearWarnings() throws SQLException;
}
