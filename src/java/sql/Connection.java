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
     * A SQL stored procedure call statement is handled by creating a
     * CallableStatement for it. The CallableStatement provides
     * methods for setting up its IN and OUT parameters, and
     * methods for executing it.
     *
     * <P><B>Note:</B> This method is optimized for handling stored
     * procedure call statements. Some drivers may send the call
     * statement to the database when the prepareCall is done; others
     * may wait until the CallableStatement is executed. This has no
     * direct affect on users; however, it does affect which method
     * throws certain SQLExceptions.
     *
     * @param sql a SQL statement that may contain one or more '?'
     * parameter placeholders. Typically this  statement is a JDBC
     * function call escape string.
     * @return a new CallableStatement object containing the
     * pre-compiled SQL statement 
     * @exception SQLException if a database-access error occurs.
     */
    CallableStatement prepareCall(String sql) throws SQLException;
						
    /**
     * A driver may convert the JDBC sql grammar into its system's
     * native SQL grammar prior to sending it; nativeSQL returns the
     * native form of the statement that the driver would have sent.
     *
     * @param sql a SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return the native form of this statement
     * @exception SQLException if a database-access error occurs.
     */
    String nativeSQL(String sql) throws SQLException;

    /**
     * If a connection is in auto-commit mode, then all its SQL
     * statements will be executed and committed as individual
     * transactions.  Otherwise, its SQL statements are grouped into
     * transactions that are terminated by either commit() or
     * rollback().  By default, new connections are in auto-commit
     * mode.
     *
     * The commit occurs when the statement completes or the next
     * execute occurs, whichever comes first. In the case of
     * statements returning a ResultSet, the statement completes when
     * the last row of the ResultSet has been retrieved or the
     * ResultSet has been closed. In advanced cases, a single
     * statement may return multiple results as well as output
     * parameter values. Here the commit occurs when all results and
     * output param values have been retrieved.
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
     * Commit makes all changes made since the previous
     * commit/rollback permanent and releases any database locks
     * currently held by the Connection. This method should only be
     * used when auto commit has been disabled.
     *
     * @exception SQLException if a database-access error occurs.
     * @see #setAutoCommit 
     */
    void commit() throws SQLException;

    /**
     * Rollback drops all changes made since the previous
     * commit/rollback and releases any database locks currently held
     * by the Connection. This method should only be used when auto
     * commit has been disabled.
     *
     * @exception SQLException if a database-access error occurs.
     * @see #setAutoCommit 
     */
    void rollback() throws SQLException;

    /**
     * In some cases, it is desirable to immediately release a
     * Connection's database and JDBC resources instead of waiting for
     * them to be automatically released; the close method provides this
     * immediate release. 
     *
     * <P><B>Note:</B> A Connection is automatically closed when it is
     * garbage collected. Certain fatal errors also result in a closed
     * Connection.
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
    // Advanced features:

    /**
     * A Connection's database is able to provide information
     * describing its tables, its supported SQL grammar, its stored
     * procedures, the capabilities of this connection, etc. This
     * information is made available through a DatabaseMetaData
     * object.
     *
     * @return a DatabaseMetaData object for this Connection 
     * @exception SQLException if a database-access error occurs.
     */
    DatabaseMetaData getMetaData() throws SQLException;;

    /**
     * You can put a connection in read-only mode as a hint to enable 
     * database optimizations.
     *
     * <P><B>Note:</B> setReadOnly cannot be called while in the
     * middle of a transaction.
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
     * A sub-space of this Connection's database may be selected by setting a
     * catalog name. If the driver does not support catalogs it will
     * silently ignore this request.
     *
     * @exception SQLException if a database-access error occurs.
     */
    void setCatalog(String catalog) throws SQLException;

    /**
     * Return the Connection's current catalog name.
     *
     * @return the current catalog name or null
     * @exception SQLException if a database-access error occurs.
     */
    String getCatalog() throws SQLException;

    /**
     * Transactions are not supported. 
     */
    int TRANSACTION_NONE	     = 0;

    /**
     * Dirty reads, non-repeatable reads and phantom reads can occur.
     */
    int TRANSACTION_READ_UNCOMMITTED = 1;

    /**
     * Dirty reads are prevented; non-repeatable reads and phantom
     * reads can occur.
     */
    int TRANSACTION_READ_COMMITTED   = 2;

    /**
     * Dirty reads and non-repeatable reads are prevented; phantom
     * reads can occur.     
     */
    int TRANSACTION_REPEATABLE_READ  = 4;

    /**
     * Dirty reads, non-repeatable reads and phantom reads are prevented.
     */
    int TRANSACTION_SERIALIZABLE     = 8;

    /**
     * You can call this method to try to change the transaction
     * isolation level using one of the TRANSACTION_* values.
     *
     * <P><B>Note:</B> setTransactionIsolation cannot be called while
     * in the middle of a transaction.
     *
     * @param level one of the TRANSACTION_* isolation values with the
     * exception of TRANSACTION_NONE; some databases may not support
     * other values
     * @exception SQLException if a database-access error occurs.
     * @see DatabaseMetaData#supportsTransactionIsolationLevel 
     */
    void setTransactionIsolation(int level) throws SQLException;

    /**
     * Get this Connection's current transaction isolation mode.
     *
     * @return the current TRANSACTION_* mode value
     * @exception SQLException if a database-access error occurs.
     */
    int getTransactionIsolation() throws SQLException;

    /**
     * The first warning reported by calls on this Connection is
     * returned.  
     *
     * <P><B>Note:</B> Subsequent warnings will be chained to this
     * SQLWarning.
     *
     * @return the first SQLWarning or null 
     * @exception SQLException if a database-access error occurs.
     */
    SQLWarning getWarnings() throws SQLException;

    /**
     * After this call, getWarnings returns null until a new warning is
     * reported for this Connection.  
     *
     * @exception SQLException if a database-access error occurs.
     */
    void clearWarnings() throws SQLException;
}
