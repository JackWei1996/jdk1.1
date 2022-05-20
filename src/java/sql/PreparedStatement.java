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
     * 将参数设置为 java.sql.Time 值。驱动程序在将其发送到数据库时将其转换为 SQL TIME 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @exception SQLException if a database-access error occurs.
     */
    void setTime(int parameterIndex, Time x)
	    throws SQLException;

    /**
     * 将参数设置为 java.sql.Timestamp 值。驱动程序在将其发送到数据库时将其转换为 SQL TIMESTAMP 值。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value 
     * @exception SQLException if a database-access error occurs.
     */
    void setTimestamp(int parameterIndex, Timestamp x)
	    throws SQLException;

    /**
     * 当向 LONGVARCHAR 参数输入非常大的 ASCII 值时，通过 java.io.InputStream 发送它可能更实际。
     * JDBC 将根据需要从流中读取数据，直到到达文件末尾。 JDBC 驱动程序将执行从 ASCII 到数据库字符格式的任何必要转换。
     * 
     * Note: 此流对象可以是标准的 Java 流对象，也可以是您自己的实现标准接口的子类。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the java input stream which contains the ASCII parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database-access error occurs.
     */
    void setAsciiStream(int parameterIndex, java.io.InputStream x, int length)
	    throws SQLException;

    /**
     * 当一个非常大的 UNICODE 值输入到 LONGVARCHAR 参数时，通过 java.io.InputStream 发送它可能更实际。
     * JDBC 将根据需要从流中读取数据，直到到达文件末尾。 JDBC 驱动程序将执行从 UNICODE 到数据库字符格式的任何必要转换。
     * 
     * <P><B>Note: 此流对象可以是标准的 Java 流对象，也可以是您自己的实现标准接口的子类。
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
     * 当一个非常大的二进制值输入到 LONGVARBINARY 参数时，通过 java.io.InputStream 发送它可能更实际。
     * JDBC 将根据需要从流中读取数据，直到到达文件末尾。
     * 
     * Note: 此流对象可以是标准的 Java 流对象，也可以是您自己的实现标准接口的子类。
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the java input stream which contains the binary parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database-access error occurs.
     */
    void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) 
	    throws SQLException;

    /**
     * 一般来说，参数值对语句的重复使用仍然有效。设置参数值会自动清除其先前的值。
     * 但是，在某些情况下，立即释放当前参数值使用的资源是有用的；这可以通过调用 clearParameters 来完成。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void clearParameters() throws SQLException;

    //----------------------------------------------------------------------
    // Advanced features:

    /**
     * 使用对象设置参数的值；将 java.lang 等效对象用于整数值。
     *
     * 给定的 Java 对象将在发送到数据库之前转换为 targetSqlType。
     *
     * 请注意，此方法可用于传递特定于数据库的抽象数据类型。
     * 这是通过使用特定于驱动程序的 Java 类型和使用 java.sql.types.OTHER 的目标 Sql 类型来完成的。
     *
     * @param parameterIndex The first parameter is 1, the second is 2, ...
     * @param x The object containing the input parameter value
     * @param targetSqlType 要发送到数据库的 SQL 类型（在 java.sql.Types 中定义）。 scale 参数可以进一步限定这种类型。
     * @param scale 对于 java.sql.Types.DECIMAL 或 java.sql.Types.NUMERIC 类型，这是小数点后的位数。
     *              对于所有其他类型，此值将被忽略，
     * @exception SQLException if a database-access error occurs.
     * @see Types 
     */
    void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
            throws SQLException;

   /**
     * 此方法类似于上面的 setObject，但假定比例为零。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException;

    /**
     * 使用对象设置参数的值；将 java.lang 等效对象用于整数值。
     *
     * JDBC 规范指定了从 Java 对象类型到 SQL 类型的标准映射。
     * 给定的参数 java 对象将在发送到数据库之前转换为相应的 SQL 类型。
     *
     * 请注意，此方法可用于通过使用特定于驱动程序的 Java 类型来传递特定于数据库的抽象数据类型。
     *
     * @param parameterIndex The first parameter is 1, the second is 2, ...
     * @param x The object containing the input parameter value 
     * @exception SQLException if a database-access error occurs.
     */
    void setObject(int parameterIndex, Object x) throws SQLException;

    /**
     * 一些准备好的语句返回多个结果； execute 方法处理这些复杂的语句以及由 executeQuery
     * 和 executeUpdate 处理的更简单的语句形式。
     *
     * @exception SQLException if a database-access error occurs.
     * @see Statement#execute
     */
    boolean execute() throws SQLException;
}
