/*
 * @(#)PreparedStatement.java	1.10 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

import java.math.BigDecimal;

/**
 * SQL 语句被预编译并存储在 PreparedStatement 对象中。然后可以使用该对象多次有效地执行该语句。
 *
 * Note: 用于设置 IN 参数值的 setXXX 方法必须指定与输入参数的已定义 SQL 类型兼容的类型。
 * 例如，如果 IN 参数的 SQL 类型为 Integer，则应使用 setInt。
 *
 * 如果需要任意参数类型转换，则 setObject 方法应与目标 SQL 类型一起使用。
 *
 * @see Connection#prepareStatement
 * @see ResultSet 
 */

public interface PreparedStatement extends Statement {

    /**
     * 执行准备好的 SQL 查询并返回其 ResultSet。
     *
     * @return 包含查询产生的数据的 ResultSet；从不为空
     * @exception SQLException if a database-access error occurs.
     */
    ResultSet executeQuery() throws SQLException;

    /**
     * 执行 SQL INSERT、UPDATE 或 DELETE 语句。此外，可以执行不返回任何内容的 SQL 语句，例如 SQL DDL 语句。
     *
     * @return INSERT、UPDATE 或 DELETE 的行数；或 0 表示不返回任何内容的 SQL 语句
     * @exception SQLException if a database-access error occurs.（如果发生数据库访问错误）
     */
    int executeUpdate() throws SQLException;

    /**
     * Set a parameter to SQL NULL.（将参数设置为 SQL NULL。）
     *
     * Note: You must specify the parameter's SQL type.（您必须指定参数的 SQL 类型）
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @param sqlType SQL type code defined by java.sql.Types（java.sql.Types 定义的 SQL 类型代码）
     * @exception SQLException if a database-access error occurs.
     */
    void setNull(int parameterIndex, int sqlType) throws SQLException;

    /**
     * 将参数设置为 Java 布尔值。驱动程序在将其发送到数据库时将其转换为 SQL BIT 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setBoolean(int parameterIndex, boolean x) throws SQLException;

    /**
     * 将参数设置为 Java 字节值。驱动程序在将其发送到数据库时将其转换为 SQL TINYINT 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setByte(int parameterIndex, byte x) throws SQLException;

    /**
     * 将参数设置为 Java 短值。驱动程序在将其发送到数据库时将其转换为 SQL SMALLINT 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setShort(int parameterIndex, short x) throws SQLException;

    /**
     * 将参数设置为 Java int 值。驱动程序在将其发送到数据库时将其转换为 SQL INTEGER 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setInt(int parameterIndex, int x) throws SQLException;

    /**
     * 将参数设置为 Java long 值。驱动程序在将其发送到数据库时将其转换为 SQL BIGINT 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setLong(int parameterIndex, long x) throws SQLException;

    /**
     * 将参数设置为 Java 浮点值。驱动程序在将其发送到数据库时将其转换为 SQL FLOAT 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setFloat(int parameterIndex, float x) throws SQLException;

    /**
     * 将参数设置为 Java 双精度值。驱动程序在将其发送到数据库时将其转换为 SQL DOUBLE 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setDouble(int parameterIndex, double x) throws SQLException;

    /**
     * 将参数设置为 java.lang.BigDecimal 值。驱动程序在将其发送到数据库时将其转换为 SQL NUMERIC 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException;

    /**
     * 将参数设置为 Java 字符串值。驱动程序在将其发送到数据库时将其转换为 SQL VARCHAR
     * 或 LONGVARCHAR 值（取决于相对于驱动程序对 VARCHAR 的限制的参数大小）。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setString(int parameterIndex, String x) throws SQLException;

    /**
     * 将参数设置为 Java 字节数组。驱动程序在将其发送到数据库时将其转换为 SQL VARBINARY
     * 或 LONGVARBINARY（取决于参数的大小相对于驱动程序对 VARBINARY 的限制）。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value 
     * @exception SQLException if a database-access error occurs.
     */
    void setBytes(int parameterIndex, byte x[]) throws SQLException;

    /**
     * 将参数设置为 java.sql.Date 值。驱动程序在将其发送到数据库时将其转换为 SQL DATE 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setDate(int parameterIndex, Date x)
	    throws SQLException;

    /**
     * Set a parameter to a java.sql.Time value.  The driver converts this
     * to a SQL TIME value when it sends it to the database.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setTime(int parameterIndex, Time x)
	    throws SQLException;

    /**
     * Set a parameter to a java.sql.Timestamp value.  The driver
     * converts this to a SQL TIMESTAMP value when it sends it to the
     * database.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value 
     * @exception SQLException if a database-access error occurs.
     */
    void setTimestamp(int parameterIndex, Timestamp x)
	    throws SQLException;

    /**
     * When a very large ASCII value is input to a LONGVARCHAR
     * parameter, it may be more practical to send it via a
     * java.io.InputStream. JDBC will read the data from the stream
     * as needed, until it reaches end-of-file.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the java input stream which contains the ASCII parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database-access error occurs.
     */
    void setAsciiStream(int parameterIndex, java.io.InputStream x, int length)
	    throws SQLException;

    /**
     * When a very large UNICODE value is input to a LONGVARCHAR
     * parameter, it may be more practical to send it via a
     * java.io.InputStream. JDBC will read the data from the stream
     * as needed, until it reaches end-of-file.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...  
     * @param x the java input stream which contains the
     * UNICODE parameter value 
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database-access error occurs.
     */
    void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length)
	    throws SQLException;

    /**
     * When a very large binary value is input to a LONGVARBINARY
     * parameter, it may be more practical to send it via a
     * java.io.InputStream. JDBC will read the data from the stream
     * as needed, until it reaches end-of-file.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the java input stream which contains the binary parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database-access error occurs.
     */
    void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) 
	    throws SQLException;

    /**
     * <P>In general, parameter values remain in force for repeated use of a
     * Statement. Setting a parameter value automatically clears its
     * previous value.  However, in some cases it is useful to immediately
     * release the resources used by the current parameter values; this can
     * be done by calling clearParameters.
     *
     * @exception SQLException if a database-access error occurs.
     */
    void clearParameters() throws SQLException;

    //----------------------------------------------------------------------
    // Advanced features:

    /**
     * <p>Set the value of a parameter using an object; use the
     * java.lang equivalent objects for integral values.
     *
     * <p>The given Java object will be converted to the targetSqlType
     * before being sent to the database.
     *
     * <p>Note that this method may be used to pass datatabase-
     * specific abstract data types. This is done by using a Driver-
     * specific Java type and using a targetSqlType of
     * java.sql.types.OTHER.
     *
     * @param parameterIndex The first parameter is 1, the second is 2, ...
     * @param x The object containing the input parameter value
     * @param targetSqlType The SQL type (as defined in java.sql.Types) to be 
     * sent to the database. The scale argument may further qualify this type.
     * @param scale For java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types
     *          this is the number of digits after the decimal.  For all other
     *          types this value will be ignored,
     * @exception SQLException if a database-access error occurs.
     * @see Types 
     */
    void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
            throws SQLException;

   /**
     * This method is like setObject above, but assumes a scale of zero.
     *
     * @exception SQLException if a database-access error occurs.
     */
    void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException;

    /**
     * <p>Set the value of a parameter using an object; use the
     * java.lang equivalent objects for integral values.
     *
     * <p>The JDBC specification specifies a standard mapping from
     * Java Object types to SQL types.  The given argument java object
     * will be converted to the corresponding SQL type before being
     * sent to the database.
     *
     * <p>Note that this method may be used to pass datatabase
     * specific abstract data types, by using a Driver specific Java
     * type.
     *
     * @param parameterIndex The first parameter is 1, the second is 2, ...
     * @param x The object containing the input parameter value 
     * @exception SQLException if a database-access error occurs.
     */
    void setObject(int parameterIndex, Object x) throws SQLException;

    /**
     * Some prepared statements return multiple results; the execute
     * method handles these complex statements as well as the simpler
     * form of statements handled by executeQuery and executeUpdate.
     *
     * @exception SQLException if a database-access error occurs.
     * @see Statement#execute
     */
    boolean execute() throws SQLException;
}
